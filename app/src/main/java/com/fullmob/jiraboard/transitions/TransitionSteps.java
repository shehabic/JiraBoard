package com.fullmob.jiraboard.transitions;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shehabic on 30/03/2017.
 */
public class TransitionSteps implements Parcelable {

    private List<TransitionStep> steps = new ArrayList<>();

    public TransitionSteps() {
    }

    public void add(TransitionStep transitionStep) {
        steps.add(transitionStep);
    }

    public List<TransitionStep> getSteps() {
        return steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.steps);
    }

    protected TransitionSteps(Parcel in) {
        this.steps = in.createTypedArrayList(TransitionStep.CREATOR);
    }

    public static final Creator<TransitionSteps> CREATOR = new Creator<TransitionSteps>() {
        @Override
        public TransitionSteps createFromParcel(Parcel source) {
            return new TransitionSteps(source);
        }

        @Override
        public TransitionSteps[] newArray(int size) {
            return new TransitionSteps[size];
        }
    };
}
