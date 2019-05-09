package com.haien.shiroHelloWorld.test;

import org.apache.shiro.authz.UnauthorizedException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author haien
 * @Description 测试细粒度的授权
 * @Date 2019/2/18
 **/
public class PermissionTest extends BaseTest{
    @Test
    public void testIsPermitted(){
        login("classpath:config/shiro-permission.ini","zhang","123");
        //判断拥有权限user:create
        Assert.assertTrue(subject().isPermitted("user:create"));
        //user:update and user:delete
        Assert.assertTrue(subject().isPermittedAll("user:update","user:delete"));
        //!user:view
        Assert.assertFalse(subject().isPermitted("user:view"));
    }

    @Test(expected = UnauthorizedException.class)
    public void testCheckPermission(){
        login("classpath:config/shiro-permission.ini","zhang","123");
        //user:create
        subject().checkPermission("user:create");
        //user:create and user:update
        subject().checkPermissions("user:create","user:update");
        //!user:view,失败抛异常
        subject().checkPermissions("user:view");
    }

}
