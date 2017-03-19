package com.fullmob.jiraboard.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UIIssueStatus implements Parcelable {

    private String name;

    private String id;

    private String self;

    private String description;

    private String iconUrl;

    private UIStatusCategory statusCategory;

    public UIIssueStatus() {
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

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
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

    public UIStatusCategory getStatusCategory() {
        return statusCategory;
    }

    public void setStatusCategory(UIStatusCategory statusCategory) {
        this.statusCategory = statusCategory;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UIIssueStatus) {
            return ((UIIssueStatus) obj).getName().equals(name);
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.self);
        dest.writeString(this.description);
        dest.writeString(this.iconUrl);
        dest.writeParcelable(this.statusCategory, flags);
    }

    protected UIIssueStatus(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.self = in.readString();
        this.description = in.readString();
        this.iconUrl = in.readString();
        this.statusCategory = in.readParcelable(UIStatusCategory.class.getClassLoader());
    }

    public static final Parcelable.Creator<UIIssueStatus> CREATOR = new Parcelable.Creator<UIIssueStatus>() {
        @Override
        public UIIssueStatus createFromParcel(Parcel source) {
            return new UIIssueStatus(source);
        }

        @Override
        public UIIssueStatus[] newArray(int size) {
            return new UIIssueStatus[size];
        }
    };
}
