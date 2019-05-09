package com.haien.shiroHelloWorld.chapter6.dao.impl;

import com.haien.shiroHelloWorld.chapter6.dao.UserDao;
import com.haien.shiroHelloWorld.chapter6.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@Transactional
public class UserDaoImplTest {
    private UserDao userDao=new UserDaoImpl();

    @Test
    public void createUser() {
        User user=new User("test3","123456");
        user.setSalt("123456");
        user=userDao.createUser(user);
        Assert.assertThat(user.getId(),is(2L));
    }
}