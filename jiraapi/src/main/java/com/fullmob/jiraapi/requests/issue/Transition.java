
package com.fullmob.jiraapi.requests.issue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Transition
 * <p>
 * 
 * 
 */
public class Transition {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("to")
    @Expose
    private To to;

    @SerializedName("hasScreen")
    @Expose
    private Boolean hasScreen;

    public Transition() {
    }

    public Transition(String id) {
        this.id = id;
    }

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

    /**
     * Status
     * <p>
     * 
     * 
     */
    public To getTo() {
        return to;
    }

    /**
     * Status
     * <p>
     * 
     * 
     */
    public void setTo(To to) {
        this.to = to;
    }

    public Boolean getHasScreen() {
        return hasScreen;
    }

    public void setHasScreen(Boolean hasScreen) {
        this.hasScreen = hasScreen;
    }

}
