package com.haien.chapter23.session.dao;

import com.haien.chapter23.utils.SerializableUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.util.List;

/**
 * @Author haien
 * @Description 会话dao，将会话持久到mysql（应该是我们看不到的一张表）；并且也自带缓存，会先从缓存找
 * @Date 2019/4/26
 **/
public class MySqlSessionDAO extends CachingSessionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @Author haien
     * @Description 新增会话
     * @Date 2019/4/26
     * @Param [session]
     * @return java.io.Serializable
     **/
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        String sql = "insert into sessions(id, session) values(?,?)";
        jdbcTemplate.update(sql, sessionId, SerializableUtils.serialize(session));
        return session.getId();
    }

    /**
     * @Author haien
     * @Description 更新会话
     * @Date 2019/4/26
     * @Param [session]
     * @return void
     **/
    @Override
    protected void doUpdate(Session session) {
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
            return; //如果会话过期/停止 没必要再更新了
        }
        String sql = "update sessions set session=? where id=?";
        jdbcTemplate.update(sql, SerializableUtils.serialize(session), session.getId());
    }

    /**
     * @Author haien
     * @Description 删除会话
     * @Date 2019/4/26
     * @Param [session]
     * @return void
     **/
    @Override
    protected void doDelete(Session session) {
        String sql = "delete from sessions where id=?";
        jdbcTemplate.update(sql, session.getId());
    }

    /**
     * @Author haien
     * @Description 查询会话
     * @Date 2019/4/26
     * @Param [sessionId]
     * @return org.apache.shiro.session.Session
     **/
    @Override
    protected Session doReadSession(Serializable sessionId) {
        String sql = "select session from sessions where id=?";
        List<String> sessionStrList = jdbcTemplate.queryForList(sql, String.class, sessionId);
        if(sessionStrList.size() == 0) return null;
        return SerializableUtils.deserialize(sessionStrList.get(0));
    }
}
