package com.haien.shiroHelloWorld.chapter6.service;

import com.haien.shiroHelloWorld.chapter6.entity.Role;

public interface RoleService {
    public Role createRole(Role role);

    public void deleteRole(Long roleId);

    /**
     * @Author haien
     * @Description 添加角色—权限之间的关系
     * @Date 2019/2/24
     * @Param [roleId, permissionIds]
     * @return void
     **/
    public void correlationPermissions(Long roleId,Long... permissionIds);

    /**
     * @Author haien
     * @Description 删除角色-权限之间的关系
     * @Date 2019/2/24
     * @Param [roleId, permissionIds]
     * @return void
     **/
    public void uncorrelationPermissions(Long roleId,Long... permissionIds);
}
