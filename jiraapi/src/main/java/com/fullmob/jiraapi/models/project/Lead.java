
package com.fullmob.jiraapi.models.project;

import android.os.Parcel;
import android.os.Parcelable;

import com.fullmob.jiraapi.models.AvatarUrls;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lead implements Parcelable {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("avatarUrls")
    @Expose
    private AvatarUrls avatarUrls;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("active")
    @Expose
    private Boolean active;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AvatarUrls getAvatarUrls() {
        return avatarUrls;
    }

    public void setAvatarUrls(AvatarUrls avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeParcelable(this.avatarUrls, flags);
        dest.writeString(this.displayName);
        dest.writeValue(this.active);
    }

    public Lead() {
    }

    protected Lead(Parcel in) {
        this.self = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.avatarUrls = in.readParcelable(AvatarUrls.class.getClassLoader());
        this.displayName = in.readString();
        this.active = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Lead> CREATOR = new Parcelable.Creator<Lead>() {
        @Override
        public Lead createFromParcel(Parcel source) {
            return new Lead(source);
        }

        @Override
        public Lead[] newArray(int size) {
            return new Lead[size];
        }
    };
}
