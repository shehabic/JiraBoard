
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomField implements Parcelable {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("id")
    @Expose
    private String id;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        dest.writeString(this.value);
        dest.writeString(this.id);
    }

    public CustomField() {
    }

    protected CustomField(Parcel in) {
        this.self = in.readString();
        this.value = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<CustomField> CREATOR = new Parcelable.Creator<CustomField>() {
        @Override
        public CustomField createFromParcel(Parcel source) {
            return new CustomField(source);
        }

        @Override
        public CustomField[] newArray(int size) {
            return new CustomField[size];
        }
    };
}
