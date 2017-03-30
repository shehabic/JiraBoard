package com.fullmob.jiraboard.transitions;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shehabic on 27/03/2017.
 */
public class TransitionStep implements Parcelable {
    public final String viaId;
    public final String viaName;
    public final String toId;
    public final String toName;
    public final String fromColor;
    public final String toColor;

    public TransitionStep(String viaId, String viaName, String toId, String toName, String fromColor, String toColor) {
        this.viaId = viaId;
        this.viaName = viaName;
        this.toName = toName;
        this.toId = toId;
        this.fromColor = fromColor;
        this.toColor = toColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.viaId);
        dest.writeString(this.viaName);
        dest.writeString(this.toId);
        dest.writeString(this.toName);
        dest.writeString(this.fromColor);
        dest.writeString(this.toColor);
    }

    protected TransitionStep(Parcel in) {
        this.viaId = in.readString();
        this.viaName = in.readString();
        this.toId = in.readString();
        this.toName = in.readString();
        this.fromColor = in.readString();
        this.toColor = in.readString();
    }

    public static final Parcelable.Creator<TransitionStep> CREATOR = new Parcelable.Creator<TransitionStep>() {
        @Override
        public TransitionStep createFromParcel(Parcel source) {
            return new TransitionStep(source);
        }

        @Override
        public TransitionStep[] newArray(int size) {
            return new TransitionStep[size];
        }
    };
}
