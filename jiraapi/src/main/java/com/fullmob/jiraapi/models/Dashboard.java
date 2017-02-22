
package com.fullmob.jiraapi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dashboard implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("view")
    @Expose
    private String view;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.self);
        dest.writeString(this.view);
    }

    public Dashboard() {
    }

    protected Dashboard(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.self = in.readString();
        this.view = in.readString();
    }

    public static final Parcelable.Creator<Dashboard> CREATOR = new Parcelable.Creator<Dashboard>() {
        @Override
        public Dashboard createFromParcel(Parcel source) {
            return new Dashboard(source);
        }

        @Override
        public Dashboard[] newArray(int size) {
            return new Dashboard[size];
        }
    };
}
