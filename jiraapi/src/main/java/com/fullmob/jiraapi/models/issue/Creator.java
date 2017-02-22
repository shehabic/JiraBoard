
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.fullmob.jiraapi.models.AvatarUrls;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Creator implements Parcelable {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("avatarUrls")
    @Expose
    private AvatarUrls avatarUrls;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("timeZone")
    @Expose
    private String timeZone;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeString(this.emailAddress);
        dest.writeParcelable(this.avatarUrls, flags);
        dest.writeString(this.displayName);
        dest.writeValue(this.active);
        dest.writeString(this.timeZone);
    }

    public Creator() {
    }

    protected Creator(Parcel in) {
        this.self = in.readString();
        this.name = in.readString();
        this.key = in.readString();
        this.emailAddress = in.readString();
        this.avatarUrls = in.readParcelable(AvatarUrls.class.getClassLoader());
        this.displayName = in.readString();
        this.active = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.timeZone = in.readString();
    }

    public static final Parcelable.Creator<com.fullmob.jiraapi.models.issue.Creator> CREATOR = new Parcelable.Creator<com.fullmob.jiraapi.models.issue.Creator>() {
        @Override
        public com.fullmob.jiraapi.models.issue.Creator createFromParcel(Parcel source) {
            return new com.fullmob.jiraapi.models.issue.Creator(source);
        }

        @Override
        public com.fullmob.jiraapi.models.issue.Creator[] newArray(int size) {
            return new com.fullmob.jiraapi.models.issue.Creator[size];
        }
    };
}
