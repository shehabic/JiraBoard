package com.fullmob.jiraapi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginInfo implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.failedLoginCount);
        dest.writeValue(this.loginCount);
        dest.writeString(this.lastFailedLoginTime);
        dest.writeString(this.previousLoginTime);
    }

    public LoginInfo() {
    }

    protected LoginInfo(Parcel in) {
        this.failedLoginCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.loginCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastFailedLoginTime = in.readString();
        this.previousLoginTime = in.readString();
    }

    public static final Parcelable.Creator<LoginInfo> CREATOR = new Parcelable.Creator<LoginInfo>() {
        @Override
        public LoginInfo createFromParcel(Parcel source) {
            return new LoginInfo(source);
        }

        @Override
        public LoginInfo[] newArray(int size) {
            return new LoginInfo[size];
        }
    };
}
