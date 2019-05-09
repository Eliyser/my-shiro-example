package com.haien.chapter23.remote;

import org.apache.shiro.session.Session;

import java.io.Serializable;

/**
 * @Author haien
 * @Description 会话的crud
 * @Date 2019/4/25
 **/
public interface RemoteServiceInterface {
    public Session getSession(String appKey, Serializable sessionId);
    Serializable createSession(Session session);
    public void updateSession(String appKey, Session session);
    public void deleteSession(String appKey, Session session);

    /**
     * @Author haien
     * @Description 根据应用key和用户名获取权限上下文
     * @Date 2019/4/25
     * @Param [appKey, username]
     * @return com.haien.chapter23.remote.PermissionContext
     **/
    public PermissionContext getPermissions(String appKey, String username);
}
