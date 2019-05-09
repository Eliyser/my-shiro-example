package com.haien.chapter16.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class PasswordHelperTest {
    @Resource
    private PasswordHelper passwordHelper;

    @Test
    public void getterTest(){
        Assert.assertEquals("sha1",passwordHelper.getAlgorithmName());
        Assert.assertEquals("4",passwordHelper.getHashIterations());
    }
}