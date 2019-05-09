package com.haien.shiroChapter11.service.impl;

import com.haien.shiroChapter11.dao.PermissionDao;
import com.haien.shiroChapter11.dao.impl.PermissionDaoImpl;
import com.haien.shiroChapter11.entity.Permission;
import com.haien.shiroChapter11.service.PermissionService;

public class PermissionServiceImpl implements PermissionService {

    private PermissionDao permissionDao = new PermissionDaoImpl();

    @Override
    public Permission createPermission(Permission permission) {
        return permissionDao.createPermission(permission);
    }

    @Override
    public void deletePermission(Long permissionId) {
        permissionDao.deletePermission(permissionId);
    }
}
