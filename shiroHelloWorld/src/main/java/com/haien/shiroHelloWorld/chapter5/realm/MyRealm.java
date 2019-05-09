package com.haien.shiroHelloWorld.chapter5.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class MyRealm extends AuthorizingRealm {
    public PasswordService passwordService;

    public void setPasswordService(PasswordService passwordService){
        this.passwordService=passwordService;
    }

    /**
     * @Author haien
     * @Description 被间接父类Authenticating调用，获取到AuthenticationInfo后会使用
     *              credentialsMatcher来验证凭证是否匹配，否则抛出IncorrectCredentialsException
     * @Date 2019/2/22
     * @Param [token]
     * @return org.apache.shiro.authc.AuthenticationInfo
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        return new SimpleAuthenticationInfo("wu",
                passwordService.encryptPassword("123"),getName());
    }

    /**
     * @Author haien
     * @Description 不需要授权就直接返回null就好了
     * @Date 2019/2/22
     * @Param [principalCollection]
     * @return org.apache.shiro.authz.AuthorizationInfo
     **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
}
