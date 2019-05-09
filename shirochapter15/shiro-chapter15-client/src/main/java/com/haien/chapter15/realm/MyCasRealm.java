package com.haien.chapter15.realm;

import com.haien.chapter15.service.UserService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @Author haien
 * @Description 自定义CasRealm
 * @Date 2019/3/11
 **/
public class MyCasRealm extends CasRealm {
    private UserService userService;

    public void setUserService(UserService userService){
        this.userService=userService;
    }

    /**
     * @Author haien
     * @Description 根据CAS服务器返回的用户身份获取相应的权限信息
     * @Date 2019/3/11
     * @Param [principals]
     * @return org.apache.shiro.authz.AuthorizationInfo
     **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username=(String)principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.findRoles(username));
        authorizationInfo.setStringPermissions(userService.findPermissions(username));
        return authorizationInfo;
    }
}




































