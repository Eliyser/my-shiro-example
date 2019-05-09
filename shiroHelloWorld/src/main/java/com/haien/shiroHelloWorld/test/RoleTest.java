package com.haien.shiroHelloWorld.test;

import org.apache.shiro.authz.UnauthorizedException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @Author haien
 * @Description 测试粗粒度的授权
 * @Date 2019/2/18
 **/
public class RoleTest extends BaseTest{
    @Test
    public void testHasRole(){
        //zhang拥有role1、role2
        login("classpath:config/shiro-role.ini","zhang","123");
        //判断是否拥有角色：role1
        Assert.assertTrue(subject().hasRole("role1"));
        //role1、role2
        Assert.assertTrue(subject().hasAllRoles(Arrays.asList("role1","role2")));
        //role1、role2和!role3
        boolean[] result=subject().hasRoles(Arrays.asList("role1","role2","role3"));
        Assert.assertEquals(true,result[0]);
        Assert.assertEquals(true,result[1]);
        Assert.assertEquals(false,result[2]);
    }

    @Test(expected = UnauthorizedException.class)
    public void testCheckRole(){
        login("classpath:config/shiro-role.ini","zhang","123");
        //断言拥有角色role1
        subject().checkRole("role1");
        //断言拥有角色role1.role3，失败抛异常
        subject().checkRoles("role1","role3");
    }
}

























