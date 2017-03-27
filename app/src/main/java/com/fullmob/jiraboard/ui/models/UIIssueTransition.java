package com.fullmob.jiraboard.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UIIssueTransition implements Parcelable {
    public String fromName;
    public String fromId;
    public String viaName;
    public String viaId;
    public String toName;
    public String toId;
    public String toColor = "blue";
    public String fromColor = "blue";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fromName);
        dest.writeString(this.fromId);
        dest.writeString(this.viaName);
        dest.writeString(this.viaId);
        dest.writeString(this.toName);
        dest.writeString(this.toId);
        dest.writeString(this.toColor);
        dest.writeString(this.fromColor);
    }

    public UIIssueTransition() {
    }

    protected UIIssueTransition(Parcel in) {
        this.fromName = in.readString();
        this.fromId = in.readString();
        this.viaName = in.readString();
        this.viaId = in.readString();
        this.toName = in.readString();
        this.toId = in.readString();
        this.toColor = in.readString();
        this.fromColor = in.readString();
    }

    public static final Parcelable.Creator<UIIssueTransition> CREATOR = new Parcelable.Creator<UIIssueTransition>() {
        @Override
        public UIIssueTransition createFromParcel(Parcel source) {
            return new UIIssueTransition(source);
        }

        @Override
        public UIIssueTransition[] newArray(int size) {
            return new UIIssueTransition[size];
        }
    };
}
