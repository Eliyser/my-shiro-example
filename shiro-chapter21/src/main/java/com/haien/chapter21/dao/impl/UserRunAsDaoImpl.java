package com.haien.chapter21.dao.impl;

import com.haien.chapter21.dao.UserRunAsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRunAsDaoImpl implements UserRunAsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @Author haien
     * @Description 授予身份，实际就是新增记录
     * @Date 2019/4/10
     * @Param [fromUserId, toUserId]
     * @return void
     **/
    @Override
    public void grantRunAs(Long fromUserId, Long toUserId) {
        String sql = "insert into sys_user_runas(from_user_id, to_user_id) values (?,?)";
        if(!exists(fromUserId, toUserId)) {
            jdbcTemplate.update(sql, fromUserId, toUserId);
        }
    }

    /**
     * @Author haien
     * @Description 关系存在判断，实际就是查找
     * @Date 2019/4/10
     * @Param [fromUserId, toUserId]
     * @return boolean
     **/
    @Override
    public boolean exists(Long fromUserId, Long toUserId) {
        String sql = "select count(1) from sys_user_runas where from_user_id=? and to_user_id=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, fromUserId, toUserId) != 0;
    }

    /**
     * @Author haien
     * @Description 回收身份，实际就是删除记录
     * @Date 2019/4/10
     * @Param [fromUserId, toUserId]
     * @return void
     **/
    @Override
    public void revokeRunAs(Long fromUserId, Long toUserId) {
        String sql = "delete from sys_user_runas where from_user_id=? and to_user_id=?";
        jdbcTemplate.update(sql, fromUserId, toUserId);
    }

    /**
     * @Author haien
     * @Description 根据秘书查找其代理的老板
     * @Date 2019/4/10
     * @Param [toUserId]
     * @return java.util.List<java.lang.Long>
     **/
    @Override
    public List<Long> findFromUserIds(Long toUserId) {
        String sql = "select from_user_id from sys_user_runas where to_user_id=?";
        return jdbcTemplate.queryForList(sql, Long.class, toUserId);
    }

    /**
     * @Author haien
     * @Description 根据老板查找其委托的秘书
     * @Date 2019/4/10
     * @Param [fromUserId]
     * @return java.util.List<java.lang.Long>
     **/
    @Override
    public List<Long> findToUserIds(Long fromUserId) {
        String sql = "select to_user_id from sys_user_runas where from_user_id=?";
        return jdbcTemplate.queryForList(sql, Long.class, fromUserId);
    }
}
