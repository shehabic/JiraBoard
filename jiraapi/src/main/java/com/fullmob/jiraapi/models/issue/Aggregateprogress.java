
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Aggregateprogress implements Parcelable {

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

    public Aggregateprogress() {
    }

    protected Aggregateprogress(Parcel in) {
        this.progress = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Aggregateprogress> CREATOR = new Parcelable.Creator<Aggregateprogress>() {
        @Override
        public Aggregateprogress createFromParcel(Parcel source) {
            return new Aggregateprogress(source);
        }

        @Override
        public Aggregateprogress[] newArray(int size) {
            return new Aggregateprogress[size];
        }
    };
}
