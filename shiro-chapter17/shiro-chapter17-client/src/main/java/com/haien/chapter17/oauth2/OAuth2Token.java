package com.haien.chapter17.oauth2;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Author haien
 * @Description 本类用于存储OAuth2服务端返回的auth code；
 * AuthenticationToken接口用于收集用户提交的信息，最常用的实现是UsernamePasswordToken
 * @Date 2019/3/30
 **/
public class OAuth2Token implements AuthenticationToken {
    private String authCode;
    private String principal;

    public OAuth2Token(String authCode) {
        this.authCode = authCode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return authCode;
    }
}

























