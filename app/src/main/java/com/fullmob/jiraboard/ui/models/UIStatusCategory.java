package com.fullmob.jiraboard.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UIStatusCategory implements Parcelable {
    private int id;

    private String name;

    private String key;

    private String self;

    private String colorName;

    public UIStatusCategory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeString(this.self);
        dest.writeString(this.colorName);
    }

    protected UIStatusCategory(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.key = in.readString();
        this.self = in.readString();
        this.colorName = in.readString();
    }

    public static final Parcelable.Creator<UIStatusCategory> CREATOR = new Parcelable.Creator<UIStatusCategory>() {
        @Override
        public UIStatusCategory createFromParcel(Parcel source) {
            return new UIStatusCategory(source);
        }

        @Override
        public UIStatusCategory[] newArray(int size) {
            return new UIStatusCategory[size];
        }
    };
}
