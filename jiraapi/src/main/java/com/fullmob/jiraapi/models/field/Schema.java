
package com.fullmob.jiraapi.models.field;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Json Type
 * <p>
 * 
 * 
 */
public class Schema implements Parcelable {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("items")
    @Expose
    private String items;
    @SerializedName("system")
    @Expose
    private String system;
    @SerializedName("custom")
    @Expose
    private String custom;
    @SerializedName("customId")
    @Expose
    private Integer customId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public Integer getCustomId() {
        return customId;
    }

    public void setCustomId(Integer customId) {
        this.customId = customId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.items);
        dest.writeString(this.system);
        dest.writeString(this.custom);
        dest.writeValue(this.customId);
    }

    public Schema() {
    }

    protected Schema(Parcel in) {
        this.type = in.readString();
        this.items = in.readString();
        this.system = in.readString();
        this.custom = in.readString();
        this.customId = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Schema> CREATOR = new Parcelable.Creator<Schema>() {
        @Override
        public Schema createFromParcel(Parcel source) {
            return new Schema(source);
        }

        @Override
        public Schema[] newArray(int size) {
            return new Schema[size];
        }
    };
}
