package com.haien.chapter15.service;

import com.haien.chapter15.entity.Permission;

public interface PermissionService {
    public Permission createPermission(Permission permission);

    public void deletePermission(Long permissionId);
}
