package com.haien.chapter23.client;

import com.haien.chapter23.remote.RemoteServiceInterface;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;

import java.io.Serializable;

/**
 * @Author haien
 * @Description 会话的维护通过远程暴露接口实现，本地不维护
 * @Date 2019/4/27
 **/
public class ClientSessionDAO extends CachingSessionDAO {
    //会话dao的远程调用接口
    private RemoteServiceInterface remoteService;
    //应用唯一key
    private String appKey;

    public void setRemoteService(RemoteServiceInterface remoteService) {
        this.remoteService = remoteService;
    }
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    protected void doDelete(Session session) {
        remoteService.deleteSession(appKey, session);
    }
    @Override
    protected void doUpdate(Session session) {
        remoteService.updateSession(appKey, session);
    }
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = remoteService.createSession(session);
        assignSessionId(session, sessionId);
        return sessionId;
    }

    /**
     * @Author haien
     * @Description 获取会话，几乎all请求都需要获取会话，好像只要要身份验证的都需要，
     *              还有“设置会话属性”也需要
     * @Date 2019/5/6
     * @Param [sessionId]
     * @return org.apache.shiro.session.Session
     **/
    @Override
    protected Session doReadSession(Serializable sessionId) {
        return remoteService.getSession(appKey, sessionId);
    }
}
