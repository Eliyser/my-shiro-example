package com.haien.shirochapter12.service;

import com.haien.shirochapter12.entity.Permission;

public interface PermissionService {
    public Permission createPermission(Permission permission);

    public void deletePermission(Long permissionId);
}
