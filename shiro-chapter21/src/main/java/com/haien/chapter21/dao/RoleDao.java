package com.haien.chapter21.dao;

import com.haien.chapter21.entity.Role;

import java.util.List;

public interface RoleDao {

    public Role createRole(Role role);
    public Role updateRole(Role role);
    public void deleteRole(Long roleId);

    public Role findOne(Long roleId);
    public List<Role> findAll();
}
