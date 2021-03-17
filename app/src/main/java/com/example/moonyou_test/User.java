package com.example.moonyou_test;

public class User {
    private String profile;
    private String id;
    private int runtime;
    private String period;

    public User(){}

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setPw(int pw) {
        this.runtime = pw;
    }

    public String getPeriod() {
        return period;
    }

    public void setUserName(String userName) {
        this.period = userName;
    }
}
