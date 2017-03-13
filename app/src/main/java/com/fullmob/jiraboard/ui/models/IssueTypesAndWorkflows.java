package com.fullmob.jiraboard.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashSet;
import java.util.List;

public class IssueTypesAndWorkflows implements Parcelable {
    private HashSet<String> workflows;
    private List<UIIssueType> issueTypes;

    public IssueTypesAndWorkflows(List<UIIssueType> uiIssueTypes, HashSet<String> uniqueWorkflows) {
        this.issueTypes = uiIssueTypes;
        this.workflows = uniqueWorkflows;
    }

    public HashSet<String> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(HashSet<String> workflows) {
        this.workflows = workflows;
    }

    public List<UIIssueType> getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(List<UIIssueType> issueTypes) {
        this.issueTypes = issueTypes;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.workflows);
        dest.writeTypedList(this.issueTypes);
    }

    protected IssueTypesAndWorkflows(Parcel in) {
        this.workflows = (HashSet<String>) in.readSerializable();
        this.issueTypes = in.createTypedArrayList(UIIssueType.CREATOR);
    }

    public static final Creator<IssueTypesAndWorkflows> CREATOR = new Creator<IssueTypesAndWorkflows>() {
        @Override
        public IssueTypesAndWorkflows createFromParcel(Parcel source) {
            return new IssueTypesAndWorkflows(source);
        }

        @Override
        public IssueTypesAndWorkflows[] newArray(int size) {
            return new IssueTypesAndWorkflows[size];
        }
    };
}
