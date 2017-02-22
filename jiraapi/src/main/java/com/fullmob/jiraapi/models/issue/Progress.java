
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Progress implements Parcelable {

    @SerializedName("progress")
    @Expose
    private Integer progress;
    @SerializedName("total")
    @Expose
    private Integer total;

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.progress);
        dest.writeValue(this.total);
    }

    public Progress() {
    }

    protected Progress(Parcel in) {
        this.progress = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Progress> CREATOR = new Parcelable.Creator<Progress>() {
        @Override
        public Progress createFromParcel(Parcel source) {
            return new Progress(source);
        }

        @Override
        public Progress[] newArray(int size) {
            return new Progress[size];
        }
    };
}
