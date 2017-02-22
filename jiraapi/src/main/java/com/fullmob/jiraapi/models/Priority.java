package com.fullmob.jiraapi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Priority implements Parcelable {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("statusColor")
    @Expose
    private String statusColor;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private String id;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.statusColor);
        dest.writeString(this.description);
        dest.writeString(this.iconUrl);
        dest.writeString(this.name);
        dest.writeString(this.id);
    }

    public Priority() {
    }

    protected Priority(Parcel in) {
        this.self = in.readString();
        this.statusColor = in.readString();
        this.description = in.readString();
        this.iconUrl = in.readString();
        this.name = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<Priority> CREATOR = new Parcelable.Creator<Priority>() {
        @Override
        public Priority createFromParcel(Parcel source) {
            return new Priority(source);
        }

        @Override
        public Priority[] newArray(int size) {
            return new Priority[size];
        }
    };
}