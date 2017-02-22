
package com.fullmob.jiraapi.requests.issue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;


/**
 * Issue Update
 * <p>
 * 
 * 
 */
public class UpdateIssueRequest {

    /**
     * Transition
     * <p>
     * 
     * 
     */
    @SerializedName("transition")
    @Expose
    private Transition transition;
    @SerializedName("fields")
    @Expose
    private Map<String, String> fields;
    @SerializedName("update")
    @Expose
    private Map<String, List<Map<String, String>>> update;
    @SerializedName("historyMetadata")
    @Expose
    private HistoryMetadata historyMetadata;
    @SerializedName("properties")
    @Expose
    private List<Property> properties = null;

    /**
     * Transition
     * <p>
     * 
     * 
     */
    public Transition getTransition() {
        return transition;
    }

    /**
     * Transition
     * <p>
     * 
     * 
     */
    public void setTransition(Transition transition) {
        this.transition = transition;
    }


    /**
     * History Metadata
     * <p>
     * 
     * 
     */
    public HistoryMetadata getHistoryMetadata() {
        return historyMetadata;
    }

    /**
     * History Metadata
     * <p>
     * 
     * 
     */
    public void setHistoryMetadata(HistoryMetadata historyMetadata) {
        this.historyMetadata = historyMetadata;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

}
