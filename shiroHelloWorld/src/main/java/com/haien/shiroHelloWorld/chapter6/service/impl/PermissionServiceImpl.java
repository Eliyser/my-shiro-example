package com.haien.shiroHelloWorld.chapter6.service.impl;

import com.haien.shiroHelloWorld.chapter6.dao.PermissionDao;
import com.haien.shiroHelloWorld.chapter6.dao.impl.PermissionDaoImpl;
import com.haien.shiroHelloWorld.chapter6.entity.Permission;
import com.haien.shiroHelloWorld.chapter6.service.PermissionService;

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
