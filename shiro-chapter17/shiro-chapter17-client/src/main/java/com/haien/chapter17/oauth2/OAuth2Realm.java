package com.haien.chapter17.oauth2;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class OAuth2Realm extends AuthorizingRealm {
    //等待外部注入
    private String clientId;
    private String clientSecret;
    private String accessTokenUrl;
    private String userInfoUrl;
    private String redirectUrl;

    /**
     * @Author haien
     * @Description 只支持OAuth2Token类型
     * @Date 2019/3/30
     * @Param [token]
     * @return boolean
     **/
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * @Author haien
     * @Description 为登录成功的用户授权
     * @Date 2019/3/30
     * @Param [principals]
     * @return org.apache.shiro.authz.AuthorizationInfo
     **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //暂时不添加任何权限
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        return authorizationInfo;
    }

    /**
     * @Author haien
     * @Description 登录验证
     * @Date 2019/3/30
     * @Param [token]
     * @return org.apache.shiro.authc.AuthenticationInfo
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        OAuth2Token oAuth2Token=(OAuth2Token)token;
        String code=oAuth2Token.getAuthCode();
        String username=extractUsername(code); //利用code发起请求获取用户名
        SimpleAuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo(
                username,code,getName());
        return authenticationInfo;
    }

    /**
     * @Author haien
     * @Description 利用code发起请求换取token，利用token发起请求换取username
     * @Date 2019/3/30
     * @Param [code]
     * @return java.lang.String
     **/
    private String extractUsername(String code){
        try {
            //构建token请求
            OAuthClient oAuthClient=new OAuthClient(new URLConnectionClient());
            OAuthClientRequest request=OAuthClientRequest
                    .tokenLocation(accessTokenUrl)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(clientId).setClientSecret(clientSecret)
                    .setCode(code)
                    .setRedirectURI(redirectUrl) //是null
                    .buildQueryMessage();
            //发起请求，获取响应
            OAuthAccessTokenResponse response=oAuthClient.accessToken(request,
                    OAuth.HttpMethod.POST);
            //获取token
            String accessToken=response.getAccessToken();
            Long expiresIn=response.getExpiresIn();

            //构建userInfo请求
            OAuthClientRequest userInfoRequest=new OAuthBearerClientRequest(userInfoUrl)
                    .setAccessToken(accessToken)
                    .buildQueryMessage();
            //发起userInfo请求
            OAuthResourceResponse userInfoResponse=oAuthClient.resource(
                    userInfoRequest,OAuth.HttpMethod.GET,OAuthResourceResponse.class);
            //获取用户名
            String username=userInfoResponse.getBody();
            return username;
        } catch (Exception e) {
            throw new OAuth2AuthenticationException(e);
        }
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
    }

    public void setUserInfoUrl(String userInfoUrl) {
        this.userInfoUrl = userInfoUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
