package com.haien.shiroHelloWorld.principalTest.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

public class MyRealm1 implements Realm {
    @Override
    public String getName() {
        return "a"; //realm name 为 “a”
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        //仅支持UsernamePasswordToken
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        return new SimpleAuthenticationInfo(
                "zhang", //身份 字符串类型
                "123",   //凭据
                getName() //Realm Name
        );
    }
}

