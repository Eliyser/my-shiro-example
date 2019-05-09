package com.haien.shiroHelloWorld.chapter6.dao;

import com.haien.shiroHelloWorld.chapter6.entity.Permission;

public interface PermissionDao {
    public Permission createPermission(Permission permission);

    public void deletePermission(Long permissionId);
}
