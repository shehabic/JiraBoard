
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Timetracking implements Parcelable {

    @SerializedName("remainingEstimate")
    @Expose
    private String remainingEstimate;
    @SerializedName("remainingEstimateSeconds")
    @Expose
    private Integer remainingEstimateSeconds;

    public String getRemainingEstimate() {
        return remainingEstimate;
    }

    public void setRemainingEstimate(String remainingEstimate) {
        this.remainingEstimate = remainingEstimate;
    }

    public Integer getRemainingEstimateSeconds() {
        return remainingEstimateSeconds;
    }

    public void setRemainingEstimateSeconds(Integer remainingEstimateSeconds) {
        this.remainingEstimateSeconds = remainingEstimateSeconds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.remainingEstimate);
        dest.writeValue(this.remainingEstimateSeconds);
    }

    public Timetracking() {
    }

    protected Timetracking(Parcel in) {
        this.remainingEstimate = in.readString();
        this.remainingEstimateSeconds = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Timetracking> CREATOR = new Parcelable.Creator<Timetracking>() {
        @Override
        public Timetracking createFromParcel(Parcel source) {
            return new Timetracking(source);
        }

        @Override
        public Timetracking[] newArray(int size) {
            return new Timetracking[size];
        }
    };
}
