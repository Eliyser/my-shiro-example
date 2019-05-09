package com.haien.shiroHelloWorld.test;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Author haien
 * @Description 授权测试
 * @Date 2019/2/19
 **/
public class AuthorizerTest extends BaseTest {
    /**
     * @Author haien
     * @Description 测试MyRealm自己设置角色权限的授权方式
     * @Date 2019/2/19
     * @Param []
     * @return void
     **/
    @Test
    public void testIsPermitted(){
        login("classpath:config/shiro-jdbc-authorizer.ini","zhang","123");
        Assert.assertTrue(subject().isPermitted("user1:update"));
        Assert.assertTrue(subject().isPermitted("user2:update"));
        Assert.assertTrue(subject().isPermitted("+user1+2")); //新增
        Assert.assertTrue(subject().isPermitted("+user1+8")); //查看
        Assert.assertTrue(subject().isPermitted("+user2+10")); //新增与查看
        Assert.assertFalse(subject().isPermitted("+user1+4")); //没有删除权限
        //MyRolePermissionResolver解析得到的权限
        Assert.assertTrue(subject().isPermitted("menu:view"));
    }
}






























