package com.haien.shiroHelloWorld.chapter6.entity;

import java.io.Serializable;

public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String salt;
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

    public String getCredentialsSalt() { //盐=用户名+盐
        return username + salt;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; //比较的是引用（地址），不同对象地址肯定不同，相同则肯定是同一对象
        if (o == null || getClass() != o.getClass()) return false; //类型不同也肯定不同

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false; //再判断唯一id是否相同

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0; //用id构造一个hash出来
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", locked=" + locked +
                '}';
    }
}
