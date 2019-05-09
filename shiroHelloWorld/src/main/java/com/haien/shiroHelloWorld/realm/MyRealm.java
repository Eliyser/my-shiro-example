package com.haien.shiroHelloWorld.realm;

import com.haien.shiroHelloWorld.permission.BitPermission;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @Author haien
 * @Description 自定义Realm
 * @Date 2019/2/19
 **/
public class MyRealm extends AuthorizingRealm { //继承AuthorizingRealm实现Realm接口
    /**
     * @Author haien
     * @Description 获取身份验证信息（和MyRealm1一样）
     * @Date 2019/2/19
     * @Param [authenticationToken]
     * @return org.apache.shiro.authc.AuthenticationInfo
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {
        String username=(String)authenticationToken.getPrincipal(); //得到用户名
        String password=new String((char[])authenticationToken.getCredentials()); //得到密码
        //String password=(String)authenticationToken.getCredentials();
        //若用户名错误
        if(!"zhang".equals(username)){
            throw new UnknownAccountException();
        }
        //若密码错误
        if(!"123".equals(password)){
            throw new IncorrectCredentialsException();
        }
        //身份验证成功，返回一个AuthenticationInfo实现
        return new SimpleAuthenticationInfo(username,password,getName());
    }

    /**
     * @Author haien
     * @Description 根据用户身份获取授权信息
     * @Date 2019/2/19
     * @Param [principalCollection]
     * @return org.apache.shiro.authz.AuthorizationInfo
     **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();

        /*不从数据库查找，自己设置权限进去*/
        //用户拥有两个角色
        authorizationInfo.addRole("role1"); //添加角色后调用MyRolePermissionResolver解析出权限
        authorizationInfo.addRole("role2"); //解析出来后应该是添加回authorizationInfo的权限集合

        //四个权限
        authorizationInfo.addObjectPermission(new BitPermission("+user1+10"));
        authorizationInfo.addObjectPermission(new WildcardPermission("user1:*"));
        authorizationInfo.addStringPermission("+user2+10"); //10代表2新增与8查看
        authorizationInfo.addStringPermission("user2:*"); //字符串权限，等下会调用BitAndWildPermissionResolver解析
        return authorizationInfo;
    }


}





























