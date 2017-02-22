
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Issuetype implements Parcelable {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("subtask")
    @Expose
    private Boolean subtask;
    @SerializedName("avatarId")
    @Expose
    private Integer avatarId;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Boolean getSubtask() {
        return subtask;
    }

    public void setSubtask(Boolean subtask) {
        this.subtask = subtask;
    }

    public Integer getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Integer avatarId) {
        this.avatarId = avatarId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeString(this.iconUrl);
        dest.writeString(this.name);
        dest.writeValue(this.subtask);
        dest.writeValue(this.avatarId);
    }

    public Issuetype() {
    }

    protected Issuetype(Parcel in) {
        this.self = in.readString();
        this.id = in.readString();
        this.description = in.readString();
        this.iconUrl = in.readString();
        this.name = in.readString();
        this.subtask = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.avatarId = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Issuetype> CREATOR = new Parcelable.Creator<Issuetype>() {
        @Override
        public Issuetype createFromParcel(Parcel source) {
            return new Issuetype(source);
        }

        @Override
        public Issuetype[] newArray(int size) {
            return new Issuetype[size];
        }
    };
}
