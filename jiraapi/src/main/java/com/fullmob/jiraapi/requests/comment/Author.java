
package com.fullmob.jiraapi.requests.comment;

import android.os.Parcel;
import android.os.Parcelable;

import com.fullmob.jiraapi.models.AvatarUrls;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * User
 * <p>
 * 
 * 
 */
public class Author implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("accountId")
    @Expose
    private String accountId;
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("avatarUrls")
    @Expose
    private AvatarUrls avatarUrls;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("timeZone")
    @Expose
    private String timeZone;

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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    /**
     * 
     * (Required)
     * 
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * 
     * (Required)
     * 
     */
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
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeString(this.accountId);
        dest.writeString(this.emailAddress);
        dest.writeParcelable(this.avatarUrls, flags);
        dest.writeString(this.displayName);
        dest.writeValue(this.active);
        dest.writeString(this.timeZone);
    }

    public Author() {
    }

    protected Author(Parcel in) {
        this.name = in.readString();
        this.key = in.readString();
        this.accountId = in.readString();
        this.emailAddress = in.readString();
        this.avatarUrls = in.readParcelable(AvatarUrls.class.getClassLoader());
        this.displayName = in.readString();
        this.active = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.timeZone = in.readString();
    }

    public static final Parcelable.Creator<Author> CREATOR = new Parcelable.Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel source) {
            return new Author(source);
        }

        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };
}
