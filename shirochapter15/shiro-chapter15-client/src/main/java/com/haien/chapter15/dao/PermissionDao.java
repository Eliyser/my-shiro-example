package com.haien.chapter15.dao;

import com.haien.chapter15.entity.Permission;

public interface PermissionDao {
    public Permission createPermission(Permission permission);

    public void deletePermission(Long permissionId);
}
