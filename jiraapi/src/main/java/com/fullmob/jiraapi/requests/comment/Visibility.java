
package com.fullmob.jiraapi.requests.comment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;


/**
 * Visibility
 * <p>
 * 
 * 
 */
public class Visibility implements Parcelable {

    @SerializedName("type")
    @Expose
    private Type type;
    @SerializedName("value")
    @Expose
    private String value;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public enum Type {

        @SerializedName("group")
        GROUP("group"),
        @SerializedName("role")
        ROLE("role");
        private final String value;
        private final static Map<String, Type> CONSTANTS = new HashMap<String, Type>();

        static {
            for (Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Type fromValue(String value) {
            Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.value);
    }

    public Visibility() {
    }

    protected Visibility(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
        this.value = in.readString();
    }

    public static final Parcelable.Creator<Visibility> CREATOR = new Parcelable.Creator<Visibility>() {
        @Override
        public Visibility createFromParcel(Parcel source) {
            return new Visibility(source);
        }

        @Override
        public Visibility[] newArray(int size) {
            return new Visibility[size];
        }
    };
}
