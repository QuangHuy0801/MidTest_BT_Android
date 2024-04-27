package com.example.midtest.Model;

public class User {
    private String avatar, login, url;

    public User() {
    }

    public User(String avatar, String login, String url) {
        this.avatar = avatar;
        this.login = login;
        this.url = url;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
