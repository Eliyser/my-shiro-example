package com.haien.chapter21.service;

import com.haien.chapter21.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    public User createUser(User user);
    public User updateUser(User user);
    public void deleteUser(Long userId);

    public void changePassword(Long userId, String newPassword);

    User findOne(Long userId);
    List<User> findAll();

    public User findByUsername(String username);

    /**
     * 根据用户名查找其角色标识符
     * @param username
     * @return
     */
    public Set<String> findRoles(String username);

    /**
     * 根据用户名查找其权限字符串
     * @param username
     * @return
     */
    public Set<String> findPermissions(String username);

}