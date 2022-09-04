package com.university.chat.data.model;

public class ChatModel {
    private String username, message, time, userId, image, replyMessage, replyUsername, key, replyMessageKey;
    private int usernameColor, replyUsernameColor;
    private boolean userAdmin, replyUserAdmin, userBan;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public String getReplyUsername() {
        return replyUsername;
    }

    public void setReplyUsername(String replyUsername) {
        this.replyUsername = replyUsername;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getUsernameColor() {
        return usernameColor;
    }

    public void setUsernameColor(int usernameColor) {
        this.usernameColor = usernameColor;
    }

    public int getReplyUsernameColor() {
        return replyUsernameColor;
    }

    public void setReplyUsernameColor(int replyUsernameColor) {
        this.replyUsernameColor = replyUsernameColor;
    }

    public boolean isUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(boolean userAdmin) {
        this.userAdmin = userAdmin;
    }

    public boolean isReplyUserAdmin() {
        return replyUserAdmin;
    }

    public void setReplyUserAdmin(boolean replyUserAdmin) {
        this.replyUserAdmin = replyUserAdmin;
    }

    public String getReplyMessageKey() {
        return replyMessageKey;
    }

    public void setReplyMessageKey(String replyMessageKey) {
        this.replyMessageKey = replyMessageKey;
    }

    public boolean isUserBan() {
        return userBan;
    }

    public void setUserBan(boolean userBan) {
        this.userBan = userBan;
    }
}
