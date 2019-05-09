package com.haien.shirochapter12.service;

import com.haien.shirochapter12.entity.User;

import java.util.Set;

public interface UserService {
    public User createUser(User user);

    public void changePassword(Long userId, String newPassword);

    /**
     * @Author haien
     * @Description 添加用户-角色关系
     * @Date 2019/2/24
     * @Param [userId, rolesIds]
     * @return void
     **/
    public void correlationRoles(Long userId, Long... rolesIds);

    /**
     * @Author haien
     * @Description 删除用户-角色关系
     * @Date 2019/2/24
     * @Param [userId, roleIds]
     * @return void
     **/
    public void uncorrelationRoles(Long userId, Long... roleIds);

    public User findByUsername(String username);

    public Set<String> findRoles(String username);

    public Set<String> findPermissions(String username);
}
