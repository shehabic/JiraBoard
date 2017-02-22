package com.fullmob.jiraapi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.net.URI;

public class Resolution implements Parcelable {

    @SerializedName("self")
    @Expose
    private URI self;
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

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
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
        dest.writeSerializable(this.self);
        dest.writeString(this.description);
        dest.writeString(this.iconUrl);
        dest.writeString(this.name);
        dest.writeString(this.id);
    }

    public Resolution() {
    }

    protected Resolution(Parcel in) {
        this.self = (URI) in.readSerializable();
        this.description = in.readString();
        this.iconUrl = in.readString();
        this.name = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<Resolution> CREATOR = new Parcelable.Creator<Resolution>() {
        @Override
        public Resolution createFromParcel(Parcel source) {
            return new Resolution(source);
        }

        @Override
        public Resolution[] newArray(int size) {
            return new Resolution[size];
        }
    };
}
