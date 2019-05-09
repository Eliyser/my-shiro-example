package com.haien.chapter15.dao.impl;

import com.haien.chapter15.dao.UserDao;
import com.haien.chapter15.entity.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDaoImpl extends JdbcDaoSupport implements UserDao {
    //继承JdbcDaoSupport后，自动注入JdbcTemplate，不用手动注入
    //private JdbcTemplate jdbcTemplate = JdbcTemplateUtils.jdbcTemplate();

    /**
     * @Author haien
     * @Description sys_users表以声明username为唯一索引，故不用检查username是否重复，
     *              重复的话mysql会自动抛异常
     * @Date 2019/2/25
     * @Param [user]
     * @return com.haien.shiroHelloWorld.chapter6.entity.User
     **/
    @Override
    public User createUser(final User user) {
        final String sql = "insert into sys_users(username, password, salt, locked) " +
                "values(?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        //jdbcTemplate.update()
        getJdbcTemplate().update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                //没有第二个参数不能获取到任何自生值
                PreparedStatement psst = connection.prepareStatement(sql,
                        new String[] { "id" });
                psst.setString(1, user.getUsername());
                psst.setString(2, user.getPassword());
                psst.setString(3, user.getSalt());
                psst.setBoolean(4, user.getLocked());
                return psst;
            }
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public void updateUser(User user) {
        String sql = "update sys_users set username=?, password=?, salt=?, locked=? " +
                "where id=?";
        getJdbcTemplate().update(sql, user.getUsername(), user.getPassword(), user.getSalt(),
                user.getLocked(), user.getId());
    }

    @Override
    public void deleteUser(Long userId) {
        String sql = "delete from sys_users where id=?";
        getJdbcTemplate().update(sql, userId);
    }

    @Override
    public void correlationRoles(Long userId, Long... roleIds) {
        if(roleIds == null || roleIds.length == 0) {
            return;
        }
        String sql = "insert into sys_users_roles(user_id, role_id) values(?,?)";
        for(Long roleId : roleIds) {
            if(!exists(userId, roleId)) {
                getJdbcTemplate().update(sql, userId, roleId);
            }
        }
    }

    @Override
    public void uncorrelationRoles(Long userId, Long... roleIds) {
        if(roleIds == null || roleIds.length == 0) {
            return;
        }
        String sql = "delete from sys_users_roles where user_id=? and role_id=?";
        for(Long roleId : roleIds) {
            if(exists(userId, roleId)) {
                getJdbcTemplate().update(sql, userId, roleId);
            }
        }
    }

    private boolean exists(Long userId, Long roleId) {
        String sql = "select count(1) from sys_users_roles where user_id=? and role_id=?";
        //Integer是要求返回的字段的类型
        return getJdbcTemplate().queryForObject(sql, Integer.class, userId, roleId) != 0;
    }


    @Override
    public User findOne(Long userId) {
        String sql = "select id, username, password, salt, locked from sys_users where id=?";
        List<User> userList = getJdbcTemplate().query(sql,
                new BeanPropertyRowMapper(User.class), userId); //将获取到的字段封装为User类
        if(userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }

    @Override
    public User findByUsername(String username) {
        String sql = "select id, username, password, salt, locked from sys_users where username=?";
        List<User> userList = getJdbcTemplate().query(sql,
                new BeanPropertyRowMapper(User.class), username);
        if(userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }

    @Override
    public Set<String> findRoles(String username) {
        String sql = "select role from sys_users u, sys_roles r,sys_users_roles ur " +
                "where u.username=? and u.id=ur.user_id and r.id=ur.role_id";
        return new HashSet(getJdbcTemplate().queryForList(sql, String.class, username));
    }

    @Override
    public Set<String> findPermissions(String username) {
        //TODO 此处可以优化，比如查询到role后，一起获取roleId，然后直接根据roleId获取即可
        String sql = "select permission from sys_users u, sys_roles r, sys_permissions p, " +
                "sys_users_roles ur, sys_roles_permissions rp " +
                "where u.username=? and u.id=ur.user_id and r.id=ur.role_id " +
                "and r.id=rp.role_id and p.id=rp.permission_id";
        return new HashSet(getJdbcTemplate().queryForList(sql, String.class, username));
    }
}