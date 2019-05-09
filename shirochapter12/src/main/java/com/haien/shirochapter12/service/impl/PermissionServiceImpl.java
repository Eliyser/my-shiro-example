package com.haien.shirochapter12.service.impl;

import com.haien.shirochapter12.dao.PermissionDao;
import com.haien.shirochapter12.entity.Permission;
import com.haien.shirochapter12.service.PermissionService;

public class PermissionServiceImpl implements PermissionService {

    private PermissionDao permissionDao;

    public void setPermissionDao(PermissionDao permissionDao){
        this.permissionDao=permissionDao;
    }

    @Override
    public Permission createPermission(Permission permission) {
        return permissionDao.createPermission(permission);
    }

    @Override
    public void deletePermission(Long permissionId) {
        permissionDao.deletePermission(permissionId);
    }
}
