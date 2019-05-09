package com.haien.shirochapter12.dao;

import com.haien.shirochapter12.entity.Permission;

public interface PermissionDao {
    public Permission createPermission(Permission permission);

    public void deletePermission(Long permissionId);
}
