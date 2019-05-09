package com.haien.shiroHelloWorld.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * @Author haien
 * @Description 解析权限字符串：根据字符串是否以+开头解析为BitPermission或WildcardPermission
 * @Date 2019/2/19
 **/
public class BitAndWildPermissionResolver implements PermissionResolver {
    /**
     * @Author haien
     * @Description 根据字符串是否以+开头解析为BitPermission或WildcardPermission
     * @Date 2019/2/19
     * @Param [s]
     * @return org.apache.shiro.authz.Permission
     **/
    @Override
    public Permission resolvePermission(String permissionString) {
        if(permissionString.startsWith("+")){
            return new BitPermission(permissionString);
        }
        return new WildcardPermission(permissionString);
    }
}



























