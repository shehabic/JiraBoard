package com.fullmob.jiraapi.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.fullmob.jiraapi.models.Issue;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResults implements Parcelable {

    @SerializedName("startAt")
    private int startAt;

    @SerializedName("maxResults")
    private int maxResults;

    @SerializedName("total")
    private int total;

    @SerializedName("issues")
    private List<Issue> issues;

    public SearchResults() {
    }

    public int getStartAt() {
        return startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.startAt);
        dest.writeInt(this.maxResults);
        dest.writeInt(this.total);
        dest.writeTypedList(this.issues);
    }

    protected SearchResults(Parcel in) {
        this.startAt = in.readInt();
        this.maxResults = in.readInt();
        this.total = in.readInt();
        this.issues = in.createTypedArrayList(Issue.CREATOR);
    }

    public static final Creator<SearchResults> CREATOR = new Creator<SearchResults>() {
        @Override
        public SearchResults createFromParcel(Parcel source) {
            return new SearchResults(source);
        }

        @Override
        public SearchResults[] newArray(int size) {
            return new SearchResults[size];
        }
    };
}
