package com.xjy.mua.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String channelId;
    private String userId;
    private String name;
    private int level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "level=" + level +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", id=" + id +
                '}';
    }
}
