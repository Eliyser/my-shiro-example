package com.haien.shiroHelloWorld.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

/**
 * @Author haien
 * @Description 用户名、密码为zhang、123时成功，且返回身份凭证为zhang@163.com、123
 * @Date 2019/2/16
 **/
public class MyRealm3 implements Realm {

    /**
     * @Author haien
     * @Description 获得唯一的Realm名字
     * @Date 2019/2/15
     * @Param []
     * @return java.lang.String
     **/
    @Override
    public String getName() {
        return "myrealm1";
    }

    /**
     * @Author haien
     * @Description 判断此Realm是否支持此Token
     * @Date 2019/2/15
     * @Param [authenticationToken]
     * @return boolean
     **/
    @Override
    public boolean supports(AuthenticationToken token) {
        //仅支持UsernamePasswordToken
        return token instanceof UsernamePasswordToken;
    }

    /**
     * @Author haien
     * @Description 根据Token获得Token信息
     * @Date 2019/2/15
     * @Param [authenticationToken表单参数封装类]
     * @return org.apache.shiro.authc.AuthenticationInfo身份信息封装类
     **/
    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {

        String username=(String)authenticationToken.getPrincipal(); //得到用户名
        String password=new String((char[])authenticationToken.getCredentials()); //得到密码
        //String password=(String)authenticationToken.getCredentials();
        //若用户名错误
        if(!"zhang".equals(username)){
            throw new UnknownAccountException();
        }
        //若密码错误
        if(!"123".equals(password)){
            throw new IncorrectCredentialsException();
        }
        //身份验证成功，返回一个AuthenticationInfo实现
        return new SimpleAuthenticationInfo(username+"@163.com",password,getName());
    }
}
































