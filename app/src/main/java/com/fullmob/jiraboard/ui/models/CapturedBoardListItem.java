package com.fullmob.jiraboard.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fullmob.jiraapi.models.Issue;

/**
 * Created by shehabic on 02/04/2017.
 */
public class CapturedBoardListItem implements Parcelable {

    public enum RowType {
        ISSUE,
        STATUS
    }

    private RowType type;
    private Issue issue;
    private UIIssueStatus column;
    private boolean isInGoodStatus = false;
    private UIIssueStatus statusOnBoard;

    public CapturedBoardListItem() {
    }

    public RowType getType() {
        return type;
    }

    public void setType(RowType type) {
        this.type = type;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public UIIssueStatus getColumn() {
        return column;
    }

    public void setColumn(UIIssueStatus column) {
        this.column = column;
    }

    public boolean isInGoodStatus() {
        return isInGoodStatus;
    }

    public void setInGoodStatus(boolean inGoodStatus) {
        isInGoodStatus = inGoodStatus;
    }

    public UIIssueStatus getStatusOnBoard() {
        return statusOnBoard;
    }

    public void setStatusOnBoard(UIIssueStatus statusOnBoard) {
        this.statusOnBoard = statusOnBoard;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.issue, flags);
        dest.writeParcelable(this.column, flags);
        dest.writeByte(this.isInGoodStatus ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.statusOnBoard, flags);
    }


    protected CapturedBoardListItem(Parcel in) {
        this.issue = in.readParcelable(Issue.class.getClassLoader());
        this.column = in.readParcelable(UIIssueStatus.class.getClassLoader());
        this.isInGoodStatus = in.readByte() != 0;
        this.statusOnBoard = in.readParcelable(UIIssueStatus.class.getClassLoader());
    }

    public static final Parcelable.Creator<CapturedBoardListItem> CREATOR = new Parcelable.Creator<CapturedBoardListItem>() {
        @Override
        public CapturedBoardListItem createFromParcel(Parcel source) {
            return new CapturedBoardListItem(source);
        }

        @Override
        public CapturedBoardListItem[] newArray(int size) {
            return new CapturedBoardListItem[size];
        }
    };
}
