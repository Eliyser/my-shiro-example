package com.haien.shiroHelloWorld.chapter6.realm;

import com.haien.shiroChapter11.realm.UserRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.junit.Test;

/**
 * @Author haien
 * @Description 测试类
 * @Date 2019/2/26
 **/
public class UserRealmTest extends BaseTest { //数据准备工作在BaseTest中实现
    @Test
    public void testClearCachedAuthenticationInfo() {
        //1、登录成功，此时会缓存AuthenticationInfo
        login(u1.getUsername(), password);
        //2、修改密码
        userService.changePassword(u1.getId(), password + "1");
        //3、清空之前的缓存，否则下次登录还会获取到未修改密码的AuthenticationInfo
        RealmSecurityManager securityManager =
                (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm) securityManager.getRealms().iterator().next();
        userRealm.clearCachedAuthenticationInfo(subject().getPrincipals());
        //4、再次登录，检测到AuthenticationInfo已清空，故重新缓存
        login(u1.getUsername(), password + "1");
    }
}




































