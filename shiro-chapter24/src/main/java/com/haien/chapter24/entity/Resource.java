package com.haien.chapter24.entity;

import java.io.Serializable;

public class Resource implements Serializable {
    private Long id; //编号
    private String name; //资源名称，如用户管理
    private ResourceType type = ResourceType.menu; //资源类型（枚举类）
    private String url; //资源路径，如/user
    private String permission; //权限字符串，如user:*
    private Long parentId; //父编号，如一个菜单下多个按钮
    private String parentIds; //父编号列表，如0/1/2/
    private Boolean available = Boolean.FALSE;

    //非抽象枚举类默认被final修饰，不允许继承
    public static enum ResourceType {
        //自动添加public static final修饰（static：直接由类调用；final：不许修改）
        menu("菜单"), button("按钮");

        //属性，final修饰不被改变的话更安全
        private final String info;

        //由于构造器只允许类内调用，因此必须声明为private
        private ResourceType(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * @Author haien
     * @Description 是否顶层菜单
     * @Date 2019/3/12
     * @Param []
     * @return boolean
     **/
    public boolean isRootNode() {
        return parentId == 0;
    }

    public String makeSelfAsParentIds() {
        return getParentIds() + getId() + "/";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        if (id != null ? !id.equals(resource.id) : resource.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", permission='" + permission + '\'' +
                ", parentId=" + parentId +
                ", parentIds='" + parentIds + '\'' +
                ", available=" + available +
                '}';
    }
}
