package com.haien.chapter17.web.controller;

import com.haien.chapter17.Constants;
import com.haien.chapter17.service.OAuthService;
import com.haien.chapter17.service.UserService;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author haien
 * @Description 令牌控制器，控制用户拿code去换token的过程
 * @Date 2019/3/26
 **/
@Controller
public class AccessTokenController {
    @Resource
    private OAuthService oAuthService;
    @Resource
    private UserService userService;

    @RequestMapping("/accessToken")
    public HttpEntity token(HttpServletRequest request) throws OAuthSystemException {
        try {
            //1. 把当前请求封装成OAuth的请求
            OAuthTokenRequest oAuthTokenRequest=new OAuthTokenRequest(request);

            //2. 检查client_id、客户端key(client_secret)、许可证类型是否正确
            if(!oAuthService.checkClientId(oAuthTokenRequest.getClientId())){
                OAuthResponse response=OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage(); //响应体将被前端解析为json格式
                return new ResponseEntity(response.getBody(),
                        HttpStatus.valueOf(response.getResponseStatus()));
            }
            //客户端key
            if(!oAuthService.checkClientSecret(oAuthTokenRequest.getClientSecret())){
                OAuthResponse response=OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
                return new ResponseEntity(response.getBody(),
                        HttpStatus.valueOf(response.getResponseStatus()));
            }
            //许可证类型,此处只支持code类型，其他还有password和refresh_token
            String authCode=oAuthTokenRequest.getParam(OAuth.OAUTH_CODE);
            //从request中获取grant_type参数,判断是不是code类型
            if(oAuthTokenRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
                    GrantType.AUTHORIZATION_CODE.toString())){
                //检查code是否正确
                if(!oAuthService.checkAuthCode(authCode)){
                    //生成错误响应
                    OAuthResponse response=OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription("错误的授权码")
                            .buildJSONMessage();
                    return new ResponseEntity(response.getBody(),
                            HttpStatus.valueOf(response.getResponseStatus()));
                }
            }

            //3. 生成Access Token
            OAuthIssuer oauthissuer=new OAuthIssuerImpl(new MD5Generator()); //指定生成算法
            final String accessToken=oauthissuer.accessToken(); //生成token
            //加入缓存
            oAuthService.addAccessToken(accessToken,
                    oAuthService.getUsernameByAuthCode(authCode));

            //4. 重定向回去（不知道为什么这里没有把请求中的redirect_uri放进去）
            OAuthResponse response=OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(String.valueOf(oAuthService.getExpireIn())) //Long转String
                    .buildJSONMessage(); //不需要return，自然会作为response带回去
            return new ResponseEntity(response.getBody(),
                    HttpStatus.valueOf(response.getResponseStatus()));

        } catch (OAuthProblemException e) { //比如规定的参数必须要有，至少GrantType、clientId、clientSecret、code、redirectUrl
            //生成错误响应
            OAuthResponse response=OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .error(e)
                    .buildJSONMessage();
            /*不会重定向到任何地方，因为还在过滤链中，执行完此方法后，
            抛出AuthenticationException，触发onLoginFailure()*/
            return new ResponseEntity(response.getBody(),
                    HttpStatus.valueOf(response.getResponseStatus()));
        }
    }
}






































