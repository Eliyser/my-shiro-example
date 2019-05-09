package com.haien.shiroHelloWorld.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;

/**
 * @Author haien
 * @Description 用户名、密码为wang、123时成功，且返回身份凭证为wang、123
 * @Date 2019/2/16
 **/
public class MyRealm2 extends AuthenticatingRealm {

    /**
     * @Author haien
     * @Description 根据Token获得Token信息
     * @Date 2019/2/15
     * @Param [authenticationToken表单参数封装类]
     * @return org.apache.shiro.authc.AuthenticationInfo身份信息封装类
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {

        String username=(String)authenticationToken.getPrincipal(); //得到用户名
        String password=new String((char[])authenticationToken.getCredentials()); //得到密码
        //String password=(String)authenticationToken.getCredentials();
        //若用户名错误
        if(!"wang".equals(username)){
            throw new UnknownAccountException();
        }
        //若密码错误
        if(!"123".equals(password)){
            throw new IncorrectCredentialsException();
        }
        //身份验证成功，返回一个AuthenticationInfo实现
        return new SimpleAuthenticationInfo(username,"123",getName());
    }
}
































