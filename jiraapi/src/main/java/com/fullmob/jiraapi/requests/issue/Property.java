
package com.fullmob.jiraapi.requests.issue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Entity Property
 * <p>
 * 
 * 
 */
public class Property {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("value")
    @Expose
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
