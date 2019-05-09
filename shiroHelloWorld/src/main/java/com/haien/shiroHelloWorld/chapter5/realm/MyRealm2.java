package com.haien.shiroHelloWorld.chapter5.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;

/**
 * @Author haien
 * @Description 用户名+加密后的密码作为盐
 * @Date 2019/2/23
 **/
public class MyRealm2 extends AuthenticatingRealm { //不需要授权，继承AuthenticatingRealm即可
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {
        //用户库
        String username="liu";
        String password="123";

        //返回后，AuthenticatingRealm只会进行密码比对，不对用户名进行验证，因此用户验证需要在这里实现
        if(!username.equals(authenticationToken.getPrincipal())){
            throw new UnknownAccountException();
        }

        //密码加密；则ini配置文件应制定相同的加密方式来对登录用户进行加密后再与realm比对
        String algorithmName="md5";
        String salt2=new SecureRandomNumberGenerator().nextBytes().toHex(); //随机数
        int hashIterations=2;
        SimpleHash hash=new SimpleHash(algorithmName,password,
                username+salt2,hashIterations);
        String encodedPassword=hash.toHex(); //加密后的密码

        //封装
        SimpleAuthenticationInfo ai=
                new SimpleAuthenticationInfo(username,encodedPassword,getName());
        ai.setCredentialsSalt(ByteSource.Util.bytes(username+salt2)); //盐=用户名+随机数
        //返回给AuthenticatingRealm后，它调用HashedCredentialsMatcher的方法，将login密码加密，并与此身份凭证中的密码比对
        return ai;
    }
}


































