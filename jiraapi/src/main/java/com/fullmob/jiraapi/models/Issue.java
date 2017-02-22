
package com.fullmob.jiraapi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fullmob.jiraapi.models.issue.IssueFields;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Issue implements Parcelable {

    @SerializedName("expand")
    @Expose
    private String expand;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("fields")
    @Expose
    private IssueFields issueFields;

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public IssueFields getIssueFields() {
        return issueFields;
    }

    public void setIssueFields(IssueFields issueFields) {
        this.issueFields = issueFields;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.expand);
        dest.writeString(this.id);
        dest.writeString(this.self);
        dest.writeString(this.key);
        dest.writeParcelable(this.issueFields, flags);
    }

    public Issue() {
    }

    protected Issue(Parcel in) {
        this.expand = in.readString();
        this.id = in.readString();
        this.self = in.readString();
        this.key = in.readString();
        this.issueFields = in.readParcelable(IssueFields.class.getClassLoader());
    }

    public static final Parcelable.Creator<Issue> CREATOR = new Parcelable.Creator<Issue>() {
        @Override
        public Issue createFromParcel(Parcel source) {
            return new Issue(source);
        }

        @Override
        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };
}
