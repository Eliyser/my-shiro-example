package com.haien.chapter17.oauth2;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author haien
 * @Description 拦截器，调用服务端接口控制code、token的获取
 * @Date 2019/3/30
 **/
public class OAuth2AuthenticationFilter extends AuthenticatingFilter {
    //auth code参数名
    private String authcCodeParam="code";
    private String clientId;
    //服务器端登录成功后重定向地址
    private String redirectUrl; //好像用不上，因为登录成功会自动返回原本的url，外部也没有注入
    //失败后重定向地址
    private String failureUrl;
    private String responseType="code";

    /**
     * @Author haien
     * @Description 用code创建OAuth2Token
     * @Date 2019/3/30
     * @Param [request, response]
     * @return org.apache.shiro.authc.AuthenticationToken
     **/
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response)
            throws Exception{
        HttpServletRequest httpRequest=(HttpServletRequest)request;
        String code=httpRequest.getParameter(authcCodeParam);
        return new OAuth2Token(code);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) {
        return false; //返回false是为了只让onAccessdeny()起作用，因为onPreHandle()返回两者的或结果
    }

    /**
     * @Author haien
     * @Description 访问某需要权限的方法前则会执行此方法（而不是被拒绝访问后才会执行），
     *              本例中的方法最多只需要登录，所以转到服务端请求授权即可。
     *              返回结果表示当访问拒绝时是否已经处理了，true表示需要继续处理，否则直接返回即可。
     *              不知道什么情况下会被拒绝？
     * @Date 2019/3/30
     * @Param [request, response]
     * @return boolean
     **/
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        String error=request.getParameter("error");
        String errorDescription=request.getParameter("error_description");
        //如果服务端返回了错误参数
        if(!StringUtils.isEmpty(error)){
            //重定向到失败页面
            WebUtils.issueRedirect(request,response,
                    failureUrl+"?error="+error+"error_description="+errorDescription);
            return false;
        }

        //判断用户是否已身份验证（记住我不算）
        Subject subject=getSubject(request,response);
        if(!subject.isAuthenticated()){
            //如果用户未身份验证且没有code
            if(StringUtils.isEmpty(request.getParameter(authcCodeParam))){
                //重定向到服务端授权，根据ShiroFilter配置的loginUrl，
                //是http://localhost:8080/chapter17-server/authorize?带各参数
                saveRequestAndRedirectToLogin(request,response);
                return false;
            }

        /*未身份验证但已经重定向到server端登录页面并成功登录拿到code
        （由于拿到code后OAuthresponse.buildQueryMessage()重定向回/oauth2-login又触发该过滤器，
        因为server端和client端并不共享一个Subject，所以那边登录后这边仍未登录），
        则执行父类的登录逻辑，它会调用createToken()用code创建OAuth2Token并交给Subject.login()登录，
        login()将调用OAuth2Realm进行身份验证；
        登录成功将回调onLoginSuccess()重定向到成功页面；失败则onLoginFailure()重定向到失败页面*/
            return executeLogin(request,response);
        }

        return false;
    }

    /**
     * @Author haien
     * @Description 登录成功后的回调方法，重定向之前地址或默认成功页面,；被上面executeLogin()调用
     * @Date 2019/3/30
     * @Param [token, subject, request, response]
     * @return boolean
     **/
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response)
            throws Exception {
        //把原先的url：/oauth2-login清空，跳转默认成功页面，不然又会触发过滤器，而执行onAccessdeny()，
        //这次直接返回false，而给前端呈现了一个空白的/oauth2-login页面
        WebUtils.getAndClearSavedRequest(request);
        issueSuccessRedirect(request,response);
        return false;
    }

    /**
     * @Author haien
     * @Description 登录失败后的回调；被上面executeLogin()调用
     * @Date 2019/3/30
     * @Param [token, e, request, response]
     * @return boolean
     **/
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae,
                                     ServletRequest request, ServletResponse response) {
        Subject subject=getSubject(request,response);
        //如果用户其实已登录过了
        if(subject.isAuthenticated()||subject.isRemembered()){
            //重定向到成功页面
            try {
                issueSuccessRedirect(request,response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //如果用户未登录且目前登录失败
        else {
            try {
                //重定向到失败页面
                WebUtils.issueRedirect(request,response,failureUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public void setAuthcCodeParam(String authcCodeParam) {
        this.authcCodeParam = authcCodeParam;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
}





























