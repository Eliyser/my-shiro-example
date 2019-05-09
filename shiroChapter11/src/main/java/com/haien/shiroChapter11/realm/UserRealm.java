package com.haien.shiroChapter11.realm;

import com.haien.shiroChapter11.entity.User;
import com.haien.shiroChapter11.service.UserService;
import com.haien.shiroChapter11.service.impl.UserServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * @Author haien
 * @Description 虽然没有配置JdbcRealm，但此自定义Realm也是通过service层从数据库查找用户信息的
 * @Date 2019/2/26
 * @Param
 * @return
 **/
public class UserRealm extends AuthorizingRealm {
    private UserService userService=new UserServiceImpl();

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) { //PrincipalCollection是一个身份集合，因为我们现在就一个Realm，所以直接调用getPrimaryPrincipal得到之前传入的用户名即可
        String username=(String)principals.getPrimaryPrincipal();

        //查询用户对应角色、权限
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.findRoles(username));
        authorizationInfo.setStringPermissions(userService.findPermissions(username));

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        String username=(String)token.getPrincipal();

        User user=userService.findByUsername(username);
        //用户不存在
        if(user==null)
            throw new UnknownAccountException();
        //账号锁定
        if(Boolean.TRUE.equals(user.getLocked()))
            throw new LockedAccountException();
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，也可以自定义实现
        SimpleAuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo(
                user.getUsername(),user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()), //salt=username+salt
                        getName());

        return authenticationInfo;
    }

    //重写方法并改为public，否则测试无法调用这些Protected的方法
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo(){
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}





















