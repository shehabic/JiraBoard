
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusCategory implements Parcelable {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("colorName")
    @Expose
    private String colorName;
    @SerializedName("name")
    @Expose
    private String name;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeValue(this.id);
        dest.writeString(this.key);
        dest.writeString(this.colorName);
        dest.writeString(this.name);
    }

    public StatusCategory() {
    }

    protected StatusCategory(Parcel in) {
        this.self = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.key = in.readString();
        this.colorName = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<StatusCategory> CREATOR = new Parcelable.Creator<StatusCategory>() {
        @Override
        public StatusCategory createFromParcel(Parcel source) {
            return new StatusCategory(source);
        }

        @Override
        public StatusCategory[] newArray(int size) {
            return new StatusCategory[size];
        }
    };
}
