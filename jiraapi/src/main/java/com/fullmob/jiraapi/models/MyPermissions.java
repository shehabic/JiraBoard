package com.fullmob.jiraapi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class MyPermissions implements Parcelable {

    @SerializedName("permissions")
    private HashMap<String, Permission> permissions;

    public HashMap<String, Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(HashMap<String, Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.permissions);
    }

    public MyPermissions() {
    }

    protected MyPermissions(Parcel in) {
        this.permissions = (HashMap<String, Permission>) in.readSerializable();
    }

    public static final Parcelable.Creator<MyPermissions> CREATOR = new Parcelable.Creator<MyPermissions>() {
        @Override
        public MyPermissions createFromParcel(Parcel source) {
            return new MyPermissions(source);
        }

        @Override
        public MyPermissions[] newArray(int size) {
            return new MyPermissions[size];
        }
    };
}
