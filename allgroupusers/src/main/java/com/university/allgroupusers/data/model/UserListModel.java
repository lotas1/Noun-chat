package com.university.allgroupusers.data.model;

public class UserListModel {
    String username;
    boolean userBan;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isUserBan() {
        return userBan;
    }

    public void setUserBan(boolean userBan) {
        this.userBan = userBan;
    }
}
