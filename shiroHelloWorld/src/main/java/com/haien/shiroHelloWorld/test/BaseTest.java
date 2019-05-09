package com.haien.shiroHelloWorld.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;

/**
 * @Author haien
 * @Description 抽象化测试中常用的方法
 * @Date 2019/2/18
 **/
public abstract class BaseTest {
    /**
     * @Author haien
     * @Description protected:只能被本类及其子类、本包的方法访问
     * @Date 2019/2/18
     * @Param [configFile, username, password]
     * @return void
     **/
    protected void login(String configFile,String username,String password){
        //1、获取SecurityManager工厂
        Factory<SecurityManager> factory=new IniSecurityManagerFactory(configFile);
        //2、得到SecurityManager实例，并绑定给SecurityUtils
        SecurityManager securityManager=factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名、密码身份凭证Token
        Subject subject=SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken(username,password);

        subject.login(token);
    }

    public Subject subject(){
        return SecurityUtils.getSubject();
    }

    @After
    public void tearDown() throws Exception{
        ThreadContext.unbindSubject(); //退出时解除绑定Subject到线程，否则对下次测试造成影响
    }
}
























