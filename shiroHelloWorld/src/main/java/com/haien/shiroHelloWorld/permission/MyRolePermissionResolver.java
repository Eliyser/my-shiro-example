package com.haien.shiroHelloWorld.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.Arrays;
import java.util.Collection;

/**
 * @Author haien
 * @Description 根据角色字符串解析用户权限集合
 * @Date 2019/2/19
 **/
public class MyRolePermissionResolver implements RolePermissionResolver {
    /**
     * @Author haien
     * @Description 如果用户拥有role1，则返回menu:*权限，添加到用户权限集合中
     * @Date 2019/2/19
     * @Param [roleString]
     * @return java.util.Collection<org.apache.shiro.authz.Permission>
     **/
    @Override
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        if("role1".equals(roleString)){
            //role1的权限（在MyRealm中添加也一样）
            /*
            return Arrays.asList(new WildcardPermission("menu:*"),
                    new BitPermission("+user1+10"),
                    new WildcardPermission("user1:*"),
                    new BitPermission("+user2+10"),
                    new WildcardPermission("user2:*"));
            */
            return Arrays.asList((Permission)new WildcardPermission("menu:*"));
        }
        return null;
    }
}
