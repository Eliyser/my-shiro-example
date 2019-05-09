package com.haien.chapter22.entity;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Role implements Serializable {
    private Long id; //编号
    private String role; //角色标识 程序中判断使用,如"admin"
    private String description; //角色描述,UI界面显示使用，如“管理员”
    private List<Long> resourceIds; //拥有的资源，相当于权限
    private Boolean available = Boolean.FALSE; //是否可用,如果不可用将不会添加给用户

    public Role() {
    }

    public Role(String role, String description, Boolean available) {
        this.role = role;
        this.description = description;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getResourceIds() {
        //资源为空则返回空集合
        if(resourceIds == null) {
            resourceIds = new ArrayList<Long>();
        }
        return resourceIds;
    }

    public void setResourceIds(List<Long> resourceIds) {
        this.resourceIds = resourceIds;
    }

    /**
     * @Author haien
     * @Description 资源集合转为以逗号分隔的字符串
     * @Date 2019/3/12
     * @Param []
     * @return java.lang.String
     **/
    public String getResourceIdsStr() {
        //为空则返回空字符串
        if(CollectionUtils.isEmpty(resourceIds)) {
            return "";
        }

        //否则遍历集合元素并以逗号连接
        StringBuilder s = new StringBuilder();
        for(Long resourceId : resourceIds) {
            s.append(resourceId);
            s.append(",");
        }
        return s.toString();
    }

    /**
     * @Author haien
     * @Description 将资源字符串拆分存入集合
     * @Date 2019/3/12
     * @Param [resourceIdsStr]
     * @return void
     **/
    public void setResourceIdsStr(String resourceIdsStr) {
        //为空直接返回
        if(StringUtils.isEmpty(resourceIdsStr)) {
            return;
        }

        //拆分，存入集合
        String[] resourceIdStrs = resourceIdsStr.split(",");
        for(String resourceIdStr : resourceIdStrs) {
            if(StringUtils.isEmpty(resourceIdStr)) { //单个资源id再判下空
                continue;
            }
            getResourceIds().add(Long.valueOf(resourceIdStr)); //存入集合
        }
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * @Author haien
     * @Description id相同即为相同
     * @Date 2019/3/12
     * @Param [o]
     * @return boolean
     **/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (id != null ? !id.equals(role.id) : role.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", resourceIds=" + resourceIds +
                ", available=" + available +
                '}';
    }
}
