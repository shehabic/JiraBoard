package com.fullmob.jiraapi.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.fullmob.jiraapi.requests.issue.Transition;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class IssueTransitionsResponse implements Parcelable {
    @SerializedName("transitions")
    @Expose
    private List<Transition> transitions;

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.transitions);
    }

    public IssueTransitionsResponse() {
    }

    protected IssueTransitionsResponse(Parcel in) {
        this.transitions = new ArrayList<>();
        in.readList(this.transitions, Transition.class.getClassLoader());
    }

    public static final Parcelable.Creator<IssueTransitionsResponse> CREATOR = new Parcelable.Creator<IssueTransitionsResponse>() {
        @Override
        public IssueTransitionsResponse createFromParcel(Parcel source) {
            return new IssueTransitionsResponse(source);
        }

        @Override
        public IssueTransitionsResponse[] newArray(int size) {
            return new IssueTransitionsResponse[size];
        }
    };
}
