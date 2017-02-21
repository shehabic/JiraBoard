package com.fullmob.jiraapi.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginInfo {

    @SerializedName("failedLoginCount")
    @Expose
    private Integer failedLoginCount;
    @SerializedName("loginCount")
    @Expose
    private Integer loginCount;
    @SerializedName("lastFailedLoginTime")
    @Expose
    private String lastFailedLoginTime;
    @SerializedName("previousLoginTime")
    @Expose
    private String previousLoginTime;

    public Integer getFailedLoginCount() {
        return failedLoginCount;
    }

    public void setFailedLoginCount(Integer failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public String getLastFailedLoginTime() {
        return lastFailedLoginTime;
    }

    public void setLastFailedLoginTime(String lastFailedLoginTime) {
        this.lastFailedLoginTime = lastFailedLoginTime;
    }

    public String getPreviousLoginTime() {
        return previousLoginTime;
    }

    public void setPreviousLoginTime(String previousLoginTime) {
        this.previousLoginTime = previousLoginTime;
    }

}
