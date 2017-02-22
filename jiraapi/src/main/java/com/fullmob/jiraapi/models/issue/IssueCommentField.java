
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IssueCommentField implements Parcelable {

    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;
    @SerializedName("maxResults")
    @Expose
    private Integer maxResults;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("startAt")
    @Expose
    private Integer startAt;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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

    public Integer getStartAt() {
        return startAt;
    }

    public void setStartAt(Integer startAt) {
        this.startAt = startAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.comments);
        dest.writeValue(this.maxResults);
        dest.writeValue(this.total);
        dest.writeValue(this.startAt);
    }

    public IssueCommentField() {
    }

    protected IssueCommentField(Parcel in) {
        this.comments = in.createTypedArrayList(Comment.CREATOR);
        this.maxResults = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total = (Integer) in.readValue(Integer.class.getClassLoader());
        this.startAt = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<IssueCommentField> CREATOR = new Parcelable.Creator<IssueCommentField>() {
        @Override
        public IssueCommentField createFromParcel(Parcel source) {
            return new IssueCommentField(source);
        }

        @Override
        public IssueCommentField[] newArray(int size) {
            return new IssueCommentField[size];
        }
    };
}
