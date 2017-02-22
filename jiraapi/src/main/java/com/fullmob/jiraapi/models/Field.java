
package com.fullmob.jiraapi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fullmob.jiraapi.models.field.Schema;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Field implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("custom")
    @Expose
    private Boolean custom;
    @SerializedName("orderable")
    @Expose
    private Boolean orderable;
    @SerializedName("navigable")
    @Expose
    private Boolean navigable;
    @SerializedName("searchable")
    @Expose
    private Boolean searchable;
    @SerializedName("clauseNames")
    @Expose
    private List<String> clauseNames = new ArrayList<>();
    @SerializedName("schema")
    @Expose
    private Schema schema;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public Boolean getOrderable() {
        return orderable;
    }

    public void setOrderable(Boolean orderable) {
        this.orderable = orderable;
    }

    public Boolean getNavigable() {
        return navigable;
    }

    public void setNavigable(Boolean navigable) {
        this.navigable = navigable;
    }

    public Boolean getSearchable() {
        return searchable;
    }

    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    public List<String> getClauseNames() {
        return clauseNames;
    }

    public void setClauseNames(List<String> clauseNames) {
        this.clauseNames = clauseNames;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeValue(this.custom);
        dest.writeValue(this.orderable);
        dest.writeValue(this.navigable);
        dest.writeValue(this.searchable);
        dest.writeStringList(this.clauseNames);
        dest.writeParcelable(this.schema, flags);
    }

    public Field() {
    }

    protected Field(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.custom = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.orderable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.navigable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.searchable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.clauseNames = in.createStringArrayList();
        this.schema = in.readParcelable(Schema.class.getClassLoader());
    }

    public static final Parcelable.Creator<Field> CREATOR = new Parcelable.Creator<Field>() {
        @Override
        public Field createFromParcel(Parcel source) {
            return new Field(source);
        }

        @Override
        public Field[] newArray(int size) {
            return new Field[size];
        }
    };
}
