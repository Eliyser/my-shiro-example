package com.haien.chapter15.service.impl;

import com.haien.chapter15.dao.UserDao;
import com.haien.chapter15.entity.User;
import com.haien.chapter15.service.UserService;

import java.util.Set;

public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private PasswordHelper passwordHelper=new PasswordHelper();

    public void setUserDao(UserDao userDao){
        this.userDao=userDao;
    }

    public void setPasswordHelper(PasswordHelper passwordHelper) {
        this.passwordHelper = passwordHelper;
    }

    @Override
    public User createUser(User user) {
        //加密密码
        passwordHelper.encryptPassword(user);
        return userDao.createUser(user);
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        User user=userDao.findOne(userId);
        user.setPassword(newPassword);
        //加密密码
        passwordHelper.encryptPassword(user);
        userDao.updateUser(user);
    }

    @Override
    public void correlationRoles(Long userId, Long... roleIds) {
        userDao.correlationRoles(userId, roleIds);
    }

    @Override
    public void uncorrelationRoles(Long userId, Long... roleIds) {
        userDao.uncorrelationRoles(userId, roleIds);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Set<String> findRoles(String username) {
        return userDao.findRoles(username);
    }

    @Override
    public Set<String> findPermissions(String username) {
        return userDao.findPermissions(username);
    }
}
