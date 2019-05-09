package com.haien.chapter24.service.impl;

import com.haien.chapter24.dao.UserDao;
import com.haien.chapter24.entity.User;
import com.haien.chapter24.service.RoleService;
import com.haien.chapter24.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordHelper passwordHelper;
    @Autowired
    private RoleService roleService;

    @Override
    public User createUser(User user) {
        //加密密码
        passwordHelper.encryptPassword(user);
        return userDao.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userDao.deleteUser(userId);
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        User user =userDao.findOne(userId);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);
        userDao.updateUser(user);
    }

    @Override
    public User findOne(Long userId) {
        return userDao.findOne(userId);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * @Author haien
     * @Description 根据用户名查找角色标识符
     * @Date 2019/3/14
     * @Param [username]
     * @return java.util.Set<java.lang.String>
     **/
    @Override
    public Set<String> findRoles(String username) {
        //根据用户名查找用户
        User user =findByUsername(username);
        if(user == null) {
            return Collections.EMPTY_SET;
        }

        //user.getRoleIds()返回List<Long>，toArray转为数组，findRoles根据角色id找到角色标识符
        return roleService.findRoles(user.getRoleIds().toArray(new Long[0]));
    }

    /**
     * @Author haien
     * @Description 根据用户名查找权限字符串
     * @Date 2019/3/14
     * @Param [username]
     * @return java.util.Set<java.lang.String>
     **/
    @Override
    public Set<String> findPermissions(String username) {
        User user =findByUsername(username);
        if(user == null) {
            return Collections.EMPTY_SET;
        }
        return roleService.findPermissions(user.getRoleIds().toArray(new Long[0]));
    }

}
