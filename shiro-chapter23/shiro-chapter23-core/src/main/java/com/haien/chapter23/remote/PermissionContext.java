package com.haien.chapter23.remote;

import java.io.Serializable;
import java.util.Set;

/**
 * @Author haien
 * @Description 定义权限上下文类，用于设置和获取角色、权限字符串集合，用于RemoteService
 * @Date 2019/4/25
 **/
public class PermissionContext implements Serializable {
    private Set<String> roles;
    private Set<String> permissions;

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }


    @Override
    public String toString() {
        return "PermissionContext{" +
                ", roles=" + roles +
                ", permissions=" + permissions +
                '}';
    }
}