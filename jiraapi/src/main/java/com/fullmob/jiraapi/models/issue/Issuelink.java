
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Issuelink implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("type")
    @Expose
    private Type type;
    @SerializedName("outwardIssue")
    @Expose
    private OutwardIssue outwardIssue;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public OutwardIssue getOutwardIssue() {
        return outwardIssue;
    }

    public void setOutwardIssue(OutwardIssue outwardIssue) {
        this.outwardIssue = outwardIssue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.self);
        dest.writeParcelable(this.type, flags);
        dest.writeParcelable(this.outwardIssue, flags);
    }

    public Issuelink() {
    }

    protected Issuelink(Parcel in) {
        this.id = in.readString();
        this.self = in.readString();
        this.type = in.readParcelable(Type.class.getClassLoader());
        this.outwardIssue = in.readParcelable(OutwardIssue.class.getClassLoader());
    }

    public static final Parcelable.Creator<Issuelink> CREATOR = new Parcelable.Creator<Issuelink>() {
        @Override
        public Issuelink createFromParcel(Parcel source) {
            return new Issuelink(source);
        }

        @Override
        public Issuelink[] newArray(int size) {
            return new Issuelink[size];
        }
    };
}
