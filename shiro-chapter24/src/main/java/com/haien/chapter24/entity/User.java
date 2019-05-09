package com.haien.chapter24.entity;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private Long id; //编号
    private Long organizationId; //所属公司
    private String username; //用户名
    private String password; //密码
    private String salt; //加密密码的盐=username+此salt
    private List<Long> roleIds; //拥有的角色列表
    private Boolean locked = Boolean.FALSE;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCredentialsSalt() {
        return username + salt;
    }

    public List<Long> getRoleIds() {
        //如果权限集合为空则返回一个空集合
        if(roleIds == null) {
            roleIds = new ArrayList<Long>();
        }
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }


    /**
     * @Author haien
     * @Description 将权限集合转为逗号分隔的字符串
     * @Date 2019/3/12
     * @Param []
     * @return java.lang.String
     **/
    public String getRoleIdsStr() {
        if(CollectionUtils.isEmpty(roleIds)) {
            return ""; //返回空字符串
        }
        StringBuilder s = new StringBuilder();
        for(Long roleId : roleIds) {
            s.append(roleId);
            s.append(","); //逗号连接起来
        }
        return s.toString();
    }

    /**
     * @Author haien
     * @Description 将role_ids字符串拆分存入集合
     * @Date 2019/3/12
     * @Param [roleIdsStr]
     * @return void
     **/
    public void setRoleIdsStr(String roleIdsStr) {
        //为空则直接返回
        if(StringUtils.isEmpty(roleIdsStr)) {
            return;
        }

        //拆分，存入集合
        String[] roleIdStrs = roleIdsStr.split(",");
        for(String roleIdStr : roleIdStrs) {
            //单个id再判下空
            if(StringUtils.isEmpty(roleIdStr)) {
                continue;
            }
            //存入集合
            getRoleIds().add(Long.valueOf(roleIdStr));
        }
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    /**
     * @Author haien
     * @Description id相同即为相同用户
     * @Date 2019/3/12
     * @Param [o]
     * @return boolean
     **/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", organizationId=" + organizationId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", roleIds=" + roleIds +
                ", locked=" + locked +
                '}';
    }
}
