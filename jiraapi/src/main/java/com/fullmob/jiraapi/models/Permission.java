package com.fullmob.jiraapi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Permission implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("havePermission")
    @Expose
    private Boolean havePermission;
    @SerializedName("deprecatedKey")
    @Expose
    private Boolean deprecatedKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHavePermission() {
        return havePermission;
    }

    public void setHavePermission(Boolean havePermission) {
        this.havePermission = havePermission;
    }

    public Boolean getDeprecatedKey() {
        return deprecatedKey;
    }

    public void setDeprecatedKey(Boolean deprecatedKey) {
        this.deprecatedKey = deprecatedKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.description);
        dest.writeValue(this.havePermission);
        dest.writeValue(this.deprecatedKey);
    }

    public Permission() {
    }

    protected Permission(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.type = in.readString();
        this.description = in.readString();
        this.havePermission = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.deprecatedKey = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Permission> CREATOR = new Parcelable.Creator<Permission>() {
        @Override
        public Permission createFromParcel(Parcel source) {
            return new Permission(source);
        }

        @Override
        public Permission[] newArray(int size) {
            return new Permission[size];
        }
    };
}
