package com.haien.shiroHelloWorld.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

public class AuthenticatorTest {
    /**
     * @Author haien
     * @Description 通用化登录逻辑
     * @Date 2019/2/16
     * @Param [configFile]
     * @return void
     **/
    private void login(String configFile){
        Factory<SecurityManager> factory=new IniSecurityManagerFactory(configFile);
        SecurityManager securityManager=factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject=SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken("zhang",
                "123"); //登陆者统一为zhang

        subject.login(token);
    }

    /**
     * @Author haien
     * @Description 测试AllSuccessfulStrategy成功
     * @Date 2019/2/16
     * @Param []
     * @return void
     **/
    @Test
    public void testAllSuccessfulStrategyWithSuccess(){
        //登录成功（因为ini配置文件中没有使用身份必须为wang的myRealm2）
        login("classpath:config/shiro-authenticator-all-success.ini");
        Subject subject=SecurityUtils.getSubject();

        //得到一个身份集合，其包含了Realm验证成功的身份信息
        PrincipalCollection principalCollection=subject.getPrincipals();
        //包含了zhang和zhang@163.com的身份凭证
        Assert.assertEquals(2,principalCollection.asList().size());
    }

    /**
     * @Author haien
     * @Description 测试AllSuccessfulStrategy失败
     * @Date 2019/2/17
     * @Param []
     * @return void
     **/
    @Test(expected = UnknownAccountException.class) //括号里表明当方法抛出此异常时测试成功
    public void testAllSuccessfulAccountWithFail(){
        //但实际登录失败（配置文件使用了myRealm2）
        login("classpath:config/shiro-authenticator-all-fail.ini");
        Subject subject=SecurityUtils.getSubject();
    }
}






















