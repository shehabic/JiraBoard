
package com.fullmob.jiraapi.requests;

import com.fullmob.jiraapi.requests.issue.HistoryMetadata;
import com.fullmob.jiraapi.requests.issue.Property;
import com.fullmob.jiraapi.requests.issue.Transition;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransitionRequest {

    @SerializedName("transition")
    @Expose
    private Transition transition;

    @SerializedName("historyMetadata")
    @Expose
    private HistoryMetadata historyMetadata;
    @SerializedName("properties")
    @Expose
    private List<Property> properties = null;

    public TransitionRequest(Transition transition) {
        this.transition = transition;
    }

    public TransitionRequest() {
    }

    public Transition getTransition() {
        return transition;
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }

    public HistoryMetadata getHistoryMetadata() {
        return historyMetadata;
    }

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
