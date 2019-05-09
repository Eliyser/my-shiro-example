package com.haien.chapter19.dao;

import com.haien.chapter19.entity.User;

import java.util.List;

public interface UserDao {

    public User createUser(User user);
    public User updateUser(User user);
    public void deleteUser(Long userId);

    User findOne(Long userId);

    List<User> findAll();

    User findByUsername(String username);

}
