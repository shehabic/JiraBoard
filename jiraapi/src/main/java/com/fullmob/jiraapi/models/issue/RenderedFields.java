package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RenderedFields implements Parcelable {
    @SerializedName("resolutiondate")
    private String resolutionDate;

    @SerializedName("created")
    private String created;

    @SerializedName("updated")
    private String updated;

    @SerializedName("description")
    private String description;

    @SerializedName("attachment")
    private List<Attachment> attachments;

    @SerializedName("comments")
    private List<Comment> comments;

    public String getResolutionDate() {
        return resolutionDate;
    }

    public String getCreated() {
        return created;
    }

    public String getUpdated() {
        return updated;
    }

    public String getDescription() {
        return description;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.resolutionDate);
        dest.writeString(this.created);
        dest.writeString(this.updated);
        dest.writeString(this.description);
        dest.writeTypedList(this.attachments);
        dest.writeTypedList(this.comments);
    }

    public RenderedFields() {
    }

    protected RenderedFields(Parcel in) {
        this.resolutionDate = in.readString();
        this.created = in.readString();
        this.updated = in.readString();
        this.description = in.readString();
        this.attachments = in.createTypedArrayList(Attachment.CREATOR);
        this.comments = in.createTypedArrayList(Comment.CREATOR);
    }

    public static final Parcelable.Creator<RenderedFields> CREATOR = new Parcelable.Creator<RenderedFields>() {
        @Override
        public RenderedFields createFromParcel(Parcel source) {
            return new RenderedFields(source);
        }

        @Override
        public RenderedFields[] newArray(int size) {
            return new RenderedFields[size];
        }
    };
}