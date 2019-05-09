package com.haien.chapter17.web.controller;

import com.haien.chapter17.Constants;
import com.haien.chapter17.service.ClientService;
import com.haien.chapter17.service.OAuthService;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author haien
 * @Description 授权控制器，初始登录和获取code的控制器
 * @Date 2019/3/26
 **/
@Controller
public class AuthorizeController {
    @Resource
    private OAuthService oAuthService;
    @Resource
    private ClientService clientService;

    /**
     * @Author haien
     * @Description 映射授权页面：/authorize?clien_id=xxx & response_type=code &
     *              redirect_uri=http://localhost:8080/chapter17-client/oauth2-login
     * @Date 2019/3/26
     * @Param [model, request]
     * @return java.lang.Object
     **/
    @RequestMapping("/authorize")
    public Object authorize(Model model, HttpServletRequest request)
            throws OAuthSystemException,URISyntaxException {

        try {
            //1. 把当前请求封装成OAth请求
            OAuthAuthzRequest oAuthAuthzRequest=new OAuthAuthzRequest(request);
            //2. 检查client_id是否正确
            if(!oAuthService.checkClientId(oAuthAuthzRequest.getClientId())){
                //错误则构造错误响应
                OAuthResponse response=OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST) //错误响应
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
                //获取上面构造好的响应数据
                return new ResponseEntity(
                        response.getBody(),HttpStatus.valueOf(response.getResponseStatus())
                ); //response.getBody()：获取响应体，返回string，包含两个键值对，error和errorDescription
            }

            //3. 判断用户是否已登录
            Subject subject=SecurityUtils.getSubject();
            //若用户尚未登录，跳转登录页面
            if(!subject.isAuthenticated()){
                //未登录则跳转回登录页面
                /*首次访问/authorize，进入login()是表单参数为空，返回false，
                进入该if转发到oauth2login登录页面，填写表单参数后，由于转发的url不变，
                又将表单参数提交到该类，再次执行login()，此次表单参数不为空，验证通过后即登录成功，
                则不会进入该if，而是跳出去构建成功响应*/
                if(!login(subject,request)) {
                    model.addAttribute("client",
                            clientService.findByClientId(oAuthAuthzRequest.getClientId()));
                    return "oauth2login";
                }
            }

            //4. 登录成功或已登录则生成授权码code
            String username=(String)subject.getPrincipal();
            String authorizationCode=null;
            /*获取请求中的response_type参数，OAUTH_RESPONSE_TYPE值应为response_type；
            responseType目前只支持code和token*/
            String responseType=oAuthAuthzRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            if(responseType.equals(ResponseType.CODE.toString())){
                //生成authCode
                OAuthIssuerImpl oauthIssuerImpl=new OAuthIssuerImpl(new MD5Generator());
                authorizationCode=oauthIssuerImpl.authorizationCode();
                //添加缓存条目：key=authCode.value=username
                oAuthService.addAuthCode(authorizationCode,username);
            }

            //5. 重定向回客户端地址，http://localhost:9080/chapter17-client/oauth2-login?code=xxx
            String redirectURI=oAuthAuthzRequest.getParam(OAuth.OAUTH_REDIRECT_URI); //获取请求中的重定向地址，OAUTH_REDIRECT_URI应为redirect_uri
            final OAuthResponse response= OAuthASResponse
                    .authorizationResponse(request,HttpServletResponse.SC_FOUND)
                    .setCode(authorizationCode) //设置授权码
                    .location(redirectURI)
                    .buildQueryMessage(); //会对重定向地址发起请求
            //获取response数据
            HttpHeaders headers=new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri())); //响应报头域中location为重定向的url
            return new ResponseEntity(headers,HttpStatus.valueOf(response.getResponseStatus()));
        } catch (OAuthProblemException e) { //new OAuthAuthzRequest(request)抛出的，如缺少参数
            //异常处理
            String redirectUri=e.getRedirectUri();
            //如果客户端没有传入redirectUri
            if(OAuthUtils.isEmpty(redirectUri)) {
                //直接返回原页面，打印以下body
                return new ResponseEntity(
                        "OAuth callback url needs to be provided by client!",
                        HttpStatus.NOT_FOUND);
            }

            //否则返回其他错误信息
            final OAuthResponse response=OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_FOUND)
                    .error(e)
                    .location(redirectUri)
                    .buildQueryMessage();
            //获取response数据
            HttpHeaders headers=new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
            //不会返回原页面，而是重定向至redirectUri，response将作为响应送至该redirectUri
            return new ResponseEntity(headers,HttpStatus.valueOf(response.getResponseStatus()));
        }
    }

    /**
     * @Author haien
     * @Description shiro的登录方法
     * @Date 2019/3/26
     * @Param [subject, request]
     * @return boolean
     **/
    private boolean login(Subject subject,HttpServletRequest request){
        //不处理get请求
        if("get".equalsIgnoreCase(request.getMethod()))
            return false;

        String username=request.getParameter("username");
        String password=request.getParameter("password");
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)) //未登录时获得的参数为空
            return false;

        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        try {
            subject.login(token);
            return true;
        } catch(Exception e){
            request.setAttribute("error","登录失败："+e.getClass().getName());
            return false;
        }
    }
}


















































