
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutwardIssueFields implements Parcelable {

    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("priority")
    @Expose
    private Priority priority;
    @SerializedName("issuetype")
    @Expose
    private Issuetype issuetype;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Issuetype getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(Issuetype issuetype) {
        this.issuetype = issuetype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.summary);
        dest.writeParcelable(this.status, flags);
        dest.writeParcelable(this.priority, flags);
        dest.writeParcelable(this.issuetype, flags);
    }

    public OutwardIssueFields() {
    }

    protected OutwardIssueFields(Parcel in) {
        this.summary = in.readString();
        this.status = in.readParcelable(Status.class.getClassLoader());
        this.priority = in.readParcelable(Priority.class.getClassLoader());
        this.issuetype = in.readParcelable(Issuetype.class.getClassLoader());
    }

    public static final Parcelable.Creator<OutwardIssueFields> CREATOR = new Parcelable.Creator<OutwardIssueFields>() {
        @Override
        public OutwardIssueFields createFromParcel(Parcel source) {
            return new OutwardIssueFields(source);
        }

        @Override
        public OutwardIssueFields[] newArray(int size) {
            return new OutwardIssueFields[size];
        }
    };
}
