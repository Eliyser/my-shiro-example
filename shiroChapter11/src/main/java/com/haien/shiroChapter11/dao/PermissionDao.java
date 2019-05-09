package com.haien.shiroChapter11.dao;

import com.haien.shiroChapter11.entity.Permission;

public interface PermissionDao {
    public Permission createPermission(Permission permission);

    public void deletePermission(Long permissionId);
}
