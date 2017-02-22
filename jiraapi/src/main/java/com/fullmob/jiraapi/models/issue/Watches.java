
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Watches implements Parcelable {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("watchCount")
    @Expose
    private Integer watchCount;
    @SerializedName("isWatching")
    @Expose
    private Boolean isWatching;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public Integer getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(Integer watchCount) {
        this.watchCount = watchCount;
    }

    public Boolean getIsWatching() {
        return isWatching;
    }

    public void setIsWatching(Boolean isWatching) {
        this.isWatching = isWatching;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeValue(this.watchCount);
        dest.writeValue(this.isWatching);
    }

    public Watches() {
    }

    protected Watches(Parcel in) {
        this.self = in.readString();
        this.watchCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isWatching = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Watches> CREATOR = new Parcelable.Creator<Watches>() {
        @Override
        public Watches createFromParcel(Parcel source) {
            return new Watches(source);
        }

        @Override
        public Watches[] newArray(int size) {
            return new Watches[size];
        }
    };
}
