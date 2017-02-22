
package com.fullmob.jiraapi.models.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Simple List Wrapper
 * <p>
 * 
 * 
 */
public class Groups implements Parcelable {

    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("max-results")
    @Expose
    private Integer maxResults;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.size);
        dest.writeValue(this.maxResults);
        dest.writeTypedList(this.items);
    }

    public Groups() {
    }

    protected Groups(Parcel in) {
        this.size = (Integer) in.readValue(Integer.class.getClassLoader());
        this.maxResults = (Integer) in.readValue(Integer.class.getClassLoader());
        this.items = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Parcelable.Creator<Groups> CREATOR = new Parcelable.Creator<Groups>() {
        @Override
        public Groups createFromParcel(Parcel source) {
            return new Groups(source);
        }

        @Override
        public Groups[] newArray(int size) {
            return new Groups[size];
        }
    };
}
