package com.fullmob.jiraapi.requests;

import com.fullmob.jiraapi.requests.comment.Author;
import com.fullmob.jiraapi.requests.comment.UpdateAuthor;
import com.fullmob.jiraapi.requests.comment.Visibility;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentRequest {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("renderedBody")
    @Expose
    private String renderedBody;
    @SerializedName("updateAuthor")
    @Expose
    private UpdateAuthor updateAuthor;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("visibility")
    @Expose
    private Visibility visibility;

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

    public String getRenderedBody() {
        return renderedBody;
    }

    public void setRenderedBody(String renderedBody) {
        this.renderedBody = renderedBody;
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

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

}
