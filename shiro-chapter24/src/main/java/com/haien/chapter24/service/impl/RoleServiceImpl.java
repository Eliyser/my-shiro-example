package com.haien.chapter24.service.impl;

import com.haien.chapter24.dao.RoleDao;
import com.haien.chapter24.entity.Role;
import com.haien.chapter24.service.ResourceService;
import com.haien.chapter24.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("RoleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ResourceService resourceService;

    @Override
    public Role createRole(Role role) {
        return roleDao.createRole(role);
    }

    @Override
    public Role updateRole(Role role) {
        return roleDao.updateRole(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        roleDao.deleteRole(roleId);
    }

    @Override
    public Role findOne(Long roleId) {
        return roleDao.findOne(roleId);
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    /**
     * @Author haien
     * @Description 根据角色id得到角色标识符（如"admin"）列表
     * @Date 2019/3/14
     * @Param [roleIds]
     * @return java.util.Set<java.lang.String>
     **/
    @Override
    public Set<String> findRoles(Long... roleIds) {
        Set<String> roles = new HashSet<String>();
        for(Long roleId : roleIds) {
            Role role = findOne(roleId);
            if(role != null) {
                roles.add(role.getRole());
            }
        }
        return roles;
    }

    /**
     * @Author haien
     * @Description 根据角色id得到权限字符串列表
     * @Date 2019/3/14
     * @Param [roleIds]
     * @return java.util.Set<java.lang.String>
     **/
    @Override
    public Set<String> findPermissions(Long[] roleIds) {
        Set<Long> resourceIds = new HashSet<Long>();
        for(Long roleId : roleIds) {
            //根据角色id获取角色实体
            Role role = findOne(roleId);
            if(role != null) {
                resourceIds.addAll(role.getResourceIds()); //获取对应资源id
            }
        }
        return resourceService.findPermissions(resourceIds); //获取资源对应的权限字符串
    }
}
