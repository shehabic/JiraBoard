package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shehabic on 25/03/2017.
 */

class Attachment implements Parcelable {
    @SerializedName("com.fullmob.jiraapi.models.issue.Attachment.self")
    private String self;

    @SerializedName("id")
    private String id;

    @SerializedName("filename")
    private String filename;

    @SerializedName("author")
    private Author author;

    @SerializedName("created")
    private String created;

    @SerializedName("size")
    private String size;

    @SerializedName("mimeType")
    private String mimeType;

    @SerializedName("content")
    private String content;

    @SerializedName("thumbnail")
    private String thumbnail;

    public String getSelf() {
        return self;
    }

    public String getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public Author getAuthor() {
        return author;
    }

    public String getCreated() {
        return created;
    }

    public String getSize() {
        return size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getContent() {
        return content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.id);
        dest.writeString(this.filename);
        dest.writeParcelable(this.author, flags);
        dest.writeString(this.created);
        dest.writeString(this.size);
        dest.writeString(this.mimeType);
        dest.writeString(this.content);
        dest.writeString(this.thumbnail);
    }

    public Attachment() {
    }

    protected Attachment(Parcel in) {
        this.self = in.readString();
        this.id = in.readString();
        this.filename = in.readString();
        this.author = in.readParcelable(Author.class.getClassLoader());
        this.created = in.readString();
        this.size = in.readString();
        this.mimeType = in.readString();
        this.content = in.readString();
        this.thumbnail = in.readString();
    }

    public static final Parcelable.Creator<Attachment> CREATOR = new Parcelable.Creator<Attachment>() {
        @Override
        public Attachment createFromParcel(Parcel source) {
            return new Attachment(source);
        }

        @Override
        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };
}
