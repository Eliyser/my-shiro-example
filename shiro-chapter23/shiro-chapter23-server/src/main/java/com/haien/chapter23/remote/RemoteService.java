package com.haien.chapter23.remote;

import com.haien.chapter23.service.AuthorizationService;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @Author haien
 * @Description 会话crud
 * @Date 2019/4/25
 **/
public class RemoteService implements RemoteServiceInterface {

    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private SessionDAO sessionDAO;

    @Override
    public Session getSession(String appKey, Serializable sessionId) {
        return sessionDAO.readSession(sessionId);
    }

    @Override
    public Serializable createSession(Session session) {
        return sessionDAO.create(session);
    }

    @Override
    public void updateSession(String appKey, Session session) {
        sessionDAO.update(session);
    }

    @Override
    public void deleteSession(String appKey, Session session) {
        sessionDAO.delete(session);
    }

    /**
     * @Author haien
     * @Description 通过AuthorizationService查找用户角色和权限，设置到perssionContext中，再返回
     * @Date 2019/4/25
     * @Param [appKey, username]
     * @return com.haien.chapter23.remote.PermissionContext
     **/
    @Override
    public PermissionContext getPermissions(String appKey, String username) {
        PermissionContext permissionContext = new PermissionContext();
        permissionContext.setRoles(authorizationService.findRoles(appKey, username));
        permissionContext.setPermissions(authorizationService.findPermissions(appKey, username));
        return permissionContext;
    }
}
