
package com.fullmob.jiraapi.models.project;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Roles implements Parcelable {

    @SerializedName("atlassian-addons-project-access")
    @Expose
    private String atlassianAddonsProjectAccess;
    @SerializedName("Developers")
    @Expose
    private String developers;
    @SerializedName("Administrators")
    @Expose
    private String administrators;
    @SerializedName("Users")
    @Expose
    private String users;
    @SerializedName("Release Managers")
    @Expose
    private String releaseManagers;

    public String getAtlassianAddonsProjectAccess() {
        return atlassianAddonsProjectAccess;
    }

    public void setAtlassianAddonsProjectAccess(String atlassianAddonsProjectAccess) {
        this.atlassianAddonsProjectAccess = atlassianAddonsProjectAccess;
    }

    public String getDevelopers() {
        return developers;
    }

    public void setDevelopers(String developers) {
        this.developers = developers;
    }

    public String getAdministrators() {
        return administrators;
    }

    public void setAdministrators(String administrators) {
        this.administrators = administrators;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getReleaseManagers() {
        return releaseManagers;
    }

    public void setReleaseManagers(String releaseManagers) {
        this.releaseManagers = releaseManagers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.atlassianAddonsProjectAccess);
        dest.writeString(this.developers);
        dest.writeString(this.administrators);
        dest.writeString(this.users);
        dest.writeString(this.releaseManagers);
    }

    public Roles() {
    }

    protected Roles(Parcel in) {
        this.atlassianAddonsProjectAccess = in.readString();
        this.developers = in.readString();
        this.administrators = in.readString();
        this.users = in.readString();
        this.releaseManagers = in.readString();
    }

    public static final Parcelable.Creator<Roles> CREATOR = new Parcelable.Creator<Roles>() {
        @Override
        public Roles createFromParcel(Parcel source) {
            return new Roles(source);
        }

        @Override
        public Roles[] newArray(int size) {
            return new Roles[size];
        }
    };
}
