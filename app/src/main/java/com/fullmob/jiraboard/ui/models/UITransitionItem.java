package com.fullmob.jiraboard.ui.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class UITransitionItem implements Parcelable {

    public static final int TRANSITION_HEADER_DIRECT = 1;
    public static final int TRANSITION_HEADER_FURTHER = 2;
    public static final int TRANSITION_ITEM = 3;
    private boolean isDirect = false;

    @IntDef({TRANSITION_HEADER_DIRECT, TRANSITION_HEADER_FURTHER, TRANSITION_ITEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TransitionItemType {}

    public String title;
    public UIIssueTransition transition;
    public @TransitionItemType int type;

    public UITransitionItem() {
    }

    public UITransitionItem(@TransitionItemType int type) {
        this(type, null, false);
    }

    public UITransitionItem(@TransitionItemType int type, UIIssueTransition uiIssueTransition) {
        this(type, uiIssueTransition, false);
    }

    public UITransitionItem(@TransitionItemType int type, UIIssueTransition uiIssueTransition, boolean isDirect) {
        this.type = type;
        this.transition = uiIssueTransition;
        this.isDirect = isDirect;
    }

    public boolean isDirect() {
        return isDirect;
    }

    public void setDirect(boolean direct) {
        isDirect = direct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isDirect ? (byte) 1 : (byte) 0);
        dest.writeString(this.title);
        dest.writeParcelable(this.transition, flags);
        dest.writeInt(this.type);
    }

    protected UITransitionItem(Parcel in) {
        this.isDirect = in.readByte() != 0;
        this.title = in.readString();
        this.transition = in.readParcelable(UIIssueTransition.class.getClassLoader());
        this.type = in.readInt();
    }

    public static final Creator<UITransitionItem> CREATOR = new Creator<UITransitionItem>() {
        @Override
        public UITransitionItem createFromParcel(Parcel source) {
            return new UITransitionItem(source);
        }

        @Override
        public UITransitionItem[] newArray(int size) {
            return new UITransitionItem[size];
        }
    };
}
