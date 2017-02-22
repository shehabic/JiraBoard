
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment implements Parcelable {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("updateAuthor")
    @Expose
    private UpdateAuthor updateAuthor;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UpdateAuthor getUpdateAuthor() {
        return updateAuthor;
    }

    public void setUpdateAuthor(UpdateAuthor updateAuthor) {
        this.updateAuthor = updateAuthor;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.id);
        dest.writeParcelable(this.author, flags);
        dest.writeString(this.body);
        dest.writeParcelable(this.updateAuthor, flags);
        dest.writeString(this.created);
        dest.writeString(this.updated);
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.self = in.readString();
        this.id = in.readString();
        this.author = in.readParcelable(Author.class.getClassLoader());
        this.body = in.readString();
        this.updateAuthor = in.readParcelable(UpdateAuthor.class.getClassLoader());
        this.created = in.readString();
        this.updated = in.readString();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
