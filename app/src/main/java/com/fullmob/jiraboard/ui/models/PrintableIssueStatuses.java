package com.fullmob.jiraboard.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shehabic on 17.04.17.
 */
public class PrintableIssueStatuses implements Parcelable {
    public final List<UIIssueStatus> statuses;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.statuses);
    }

    public PrintableIssueStatuses() {
        statuses = new ArrayList<>();
    }

    protected PrintableIssueStatuses(Parcel in) {
        this.statuses = in.createTypedArrayList(UIIssueStatus.CREATOR);
    }

    public static final Parcelable.Creator<PrintableIssueStatuses> CREATOR = new Parcelable.Creator<PrintableIssueStatuses>() {
        @Override
        public PrintableIssueStatuses createFromParcel(Parcel source) {
            return new PrintableIssueStatuses(source);
        }

        @Override
        public PrintableIssueStatuses[] newArray(int size) {
            return new PrintableIssueStatuses[size];
        }
    };
}
