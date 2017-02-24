package com.fullmob.graphlib;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransitionLink {
    @SerializedName("from")
    @Expose
    public String from;
    @SerializedName("via")
    @Expose
    public String via;
    @SerializedName("to")
    @Expose
    public String to;

    public TransitionLink(String from, String via, String to) {
        this.from = from;
        this.via = via;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransitionLink that = (TransitionLink) o;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (via != null ? !via.equals(that.via) : that.via != null) return false;
        return to != null ? to.equals(that.to) : that.to == null;

    }
}
