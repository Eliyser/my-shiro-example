package com.haien.chapter23.client;

import com.haien.chapter23.remote.PermissionContext;
import com.haien.chapter23.remote.RemoteServiceInterface;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @Author haien
 * @Description 授予用户在不同客户端的权限
 * @Date 2019/5/6
 **/
public class ClientRealm extends AuthorizingRealm {
    //远程会话维护，远程指的是server模块
    private RemoteServiceInterface remoteService;
    //应用key，结合用户名可找到用户在该app下的权限
    private String appKey;

    /**
     * @Author haien
     * @Description 不知道设置这个干什么
     * @Date 2019/5/6
     * @Param [remoteService]
     * @return void
     **/
    public void setRemoteService(RemoteServiceInterface remoteService) {
        this.remoteService = remoteService;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    /**
     * @Author haien
     * @Description 为登录成功的用户获取权限;访需要权限的页面时执行，如/app1/hello
     * @Date 2019/4/27
     * @Param [principals]
     * @return org.apache.shiro.authz.AuthorizationInfo
     **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        //通过用户名获取用户在该应用中的角色和权限
        PermissionContext context = remoteService.getPermissions(appKey, username);
        authorizationInfo.setRoles(context.getRoles());
        authorizationInfo.setStringPermissions(context.getPermissions());

        return authorizationInfo;
    }

    /**
     * @Author haien
     * @Description 身份验证已交给server实现
     * @Date 2019/4/27
     * @Param [token]
     * @return org.apache.shiro.authc.AuthenticationInfo
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        //永远不会被调用
        throw new UnsupportedOperationException("不应执行doGetAuthenticationInfo");
    }
}
