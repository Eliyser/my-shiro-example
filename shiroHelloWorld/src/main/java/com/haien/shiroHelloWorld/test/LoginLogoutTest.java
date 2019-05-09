package com.haien.shiroHelloWorld.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author haien
 * @Description 运用shiro测试登录登出
 * @Date 2019/2/15
 **/
public class LoginLogoutTest {
    @Test
    public void testHelloWorld(){
        //1、获取SecurityManager工厂，并使用ini配置文件初始化SecurityManager
         Factory<SecurityManager> factory=
                new IniSecurityManagerFactory("classpath:config/shiro.ini");
        //2、得到SecurityManager实例，并绑定SecurityUtils
        SecurityManager securityManager=factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject，创建用户名、密码身份验证Token（即用户身份凭证）
        Subject subject=SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken("zhang"
                ,"123");

        try {
            //4、登录，即身份验证
            subject.login(token);
        }catch (AuthenticationException e){
            //5、身份验证失败
        }

        Assert.assertEquals(true,subject.isAuthenticated()); //断言用户是否已登录

        //6、退出
        subject.logout();
    }

}




























