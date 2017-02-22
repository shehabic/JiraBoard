
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Worklog implements Parcelable {

    @SerializedName("startAt")
    @Expose
    private Integer startAt;
    @SerializedName("maxResults")
    @Expose
    private Integer maxResults;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("worklogs")
    @Expose
    private List<Object> worklogs = null;

    public Integer getStartAt() {
        return startAt;
    }

    public void setStartAt(Integer startAt) {
        this.startAt = startAt;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Object> getWorklogs() {
        return worklogs;
    }

    public void setWorklogs(List<Object> worklogs) {
        this.worklogs = worklogs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.startAt);
        dest.writeValue(this.maxResults);
        dest.writeValue(this.total);
        dest.writeList(this.worklogs);
    }

    public Worklog() {
    }

    protected Worklog(Parcel in) {
        this.startAt = (Integer) in.readValue(Integer.class.getClassLoader());
        this.maxResults = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total = (Integer) in.readValue(Integer.class.getClassLoader());
        this.worklogs = new ArrayList<Object>();
        in.readList(this.worklogs, Object.class.getClassLoader());
    }

    public static final Parcelable.Creator<Worklog> CREATOR = new Parcelable.Creator<Worklog>() {
        @Override
        public Worklog createFromParcel(Parcel source) {
            return new Worklog(source);
        }

        @Override
        public Worklog[] newArray(int size) {
            return new Worklog[size];
        }
    };
}
