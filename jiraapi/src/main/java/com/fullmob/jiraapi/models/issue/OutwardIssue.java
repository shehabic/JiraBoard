
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutwardIssue implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("fields")
    @Expose
    private OutwardIssueFields fields;

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

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public OutwardIssueFields getFields() {
        return fields;
    }

    public void setFields(OutwardIssueFields fields) {
        this.fields = fields;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.self);
        dest.writeParcelable(this.fields, flags);
    }

    public OutwardIssue() {
    }

    protected OutwardIssue(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.self = in.readString();
        this.fields = in.readParcelable(OutwardIssueFields.class.getClassLoader());
    }

    public static final Parcelable.Creator<OutwardIssue> CREATOR = new Parcelable.Creator<OutwardIssue>() {
        @Override
        public OutwardIssue createFromParcel(Parcel source) {
            return new OutwardIssue(source);
        }

        @Override
        public OutwardIssue[] newArray(int size) {
            return new OutwardIssue[size];
        }
    };
}
