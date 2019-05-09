package com.haien.shiroHelloWorld.chapter4.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @Author haien
 * @Description 代替ini配置文件
 * @Date 2019/2/19
 **/
public class NonConfigurationCreateTest {
    @Test
    public void test(){
        DefaultSecurityManager securityManager=new DefaultSecurityManager();

        //设置authenticator
        ModularRealmAuthenticator authenticator=new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        securityManager.setAuthenticator(authenticator);

        //设置authorizer
        ModularRealmAuthorizer authorizer=new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        securityManager.setAuthorizer(authorizer);

        //设置Realm
        DruidDataSource ds=new DruidDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhosr:3306/shiro");
        ds.setUsername("root");
        ds.setPassword("123456");
        JdbcRealm jdbcRealm=new JdbcRealm();
        jdbcRealm.setDataSource(ds);
        jdbcRealm.setPermissionsLookupEnabled(true);
        securityManager.setRealms(Arrays.asList((Realm)jdbcRealm));

        //SecurityManager设置到SecurityUtils，方便全局使用
        SecurityUtils.setSecurityManager(securityManager);

        //测试
        Subject subject=SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken("zhang","123");
        subject.login(token);
        Assert.assertTrue(subject.isAuthenticated()); //是否验证通过
    }
}























