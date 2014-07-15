package com.xjy.mua.model;

public class Friend {
    private int friendId;
    private String name;
    private int level;
    private int status;
    private String specialName;

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSpecialName() {
        return specialName;
    }

    public void setSpecialName(String specialName) {
        this.specialName = specialName;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "friendId=" + friendId +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", status=" + status +
                ", specialName='" + specialName + '\'' +
                '}';
    }
}
