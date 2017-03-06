package com.fullmob.graphlib;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransitionLink {
    @SerializedName("from")
    @Expose
    public String from;
    @SerializedName("linkId")
    @Expose
    public String linkId;
    @SerializedName("linkName")
    @Expose
    public String linkName;
    @SerializedName("to")
    @Expose
    public String to;

    public TransitionLink(String from, String linkId, String linkName, String to) {
        this.from = from;
        this.linkId = linkId;
        this.linkName = linkName;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransitionLink that = (TransitionLink) o;

        return this.from.equals(that.from)
            && this.linkId.equals(that.linkId)
            && this.to.equals(that.to);
    }

    @Override
    public int hashCode() {
        return (from + to + linkId).hashCode();
    }
}
