package com.haien.shiroChapter11.service;

import com.haien.shiroChapter11.entity.Permission;

public interface PermissionService {
    public Permission createPermission(Permission permission);

    public void deletePermission(Long permissionId);
}
