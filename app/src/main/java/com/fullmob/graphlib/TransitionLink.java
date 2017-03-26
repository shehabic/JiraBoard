package com.fullmob.graphlib;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransitionLink {
    @SerializedName("fromId")
    @Expose
    public String fromId;
    @SerializedName("fromName")
    @Expose
    public String fromName;
    @SerializedName("linkId")
    @Expose
    public String linkId;
    @SerializedName("linkName")
    @Expose
    public String linkName;
    @SerializedName("toName")
    @Expose
    public String toName;
    @SerializedName("toId")
    @Expose
    public String toId;

    public TransitionLink(String fromId, String fromName, String linkId, String linkName, String toName, String toId) {
        this.fromId = fromId;
        this.fromName = fromName;
        this.linkId = linkId;
        this.linkName = linkName;
        this.toName = toName;
        this.toId = toId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransitionLink that = (TransitionLink) o;

        return this.fromName.equals(that.fromName)
            && this.linkId.equals(that.linkId)
            && this.toName.equals(that.toName);
    }

    @Override
    public int hashCode() {
        return (fromName + "_" + toName + "_" + linkId).hashCode();
    }
}
