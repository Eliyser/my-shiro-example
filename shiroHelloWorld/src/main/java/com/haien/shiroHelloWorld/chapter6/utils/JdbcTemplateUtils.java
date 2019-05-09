package com.haien.shiroHelloWorld.chapter6.utils;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @Author haien
 * @Description 获取JdbcTemplate对象，单例模式
 * @Date 2019/2/24
 **/
public class JdbcTemplateUtils {
    private static JdbcTemplate jdbcTemplate;

    /**
     * @Author haien
     * @Description 单例模式
     * @Date 2019/2/24
     * @Param []
     * @return org.springframework.jdbc.core.JdbcTemplate
     **/
    public static JdbcTemplate jdbcTemplate(){
        if(jdbcTemplate==null)
            jdbcTemplate=createJdbcTemplate();

        return jdbcTemplate;
    }

    /**
     * @Author haien
     * @Description 配置数据源
     * @Date 2019/2/25
     * @Param []
     * @return org.springframework.jdbc.core.JdbcTemplate
     **/
    private static JdbcTemplate createJdbcTemplate(){
        DruidDataSource ds=new DruidDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://127.0.0.1:3306/shiro");
        ds.setUsername("root");
        ds.setPassword("123456");

        return new JdbcTemplate(ds);
    }
}























