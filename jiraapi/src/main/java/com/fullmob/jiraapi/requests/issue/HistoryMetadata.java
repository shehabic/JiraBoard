
package com.fullmob.jiraapi.requests.issue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * History Metadata
 * <p>
 * 
 * 
 */
public class HistoryMetadata {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("descriptionKey")
    @Expose
    private String descriptionKey;
    @SerializedName("activityDescription")
    @Expose
    private String activityDescription;
    @SerializedName("activityDescriptionKey")
    @Expose
    private String activityDescriptionKey;
    @SerializedName("emailDescription")
    @Expose
    private String emailDescription;
    @SerializedName("emailDescriptionKey")
    @Expose
    private String emailDescriptionKey;
    /**
     * History Metadata Participant
     * <p>
     * 
     * 
     */
    @SerializedName("actor")
    @Expose
    private Actor actor;
    /**
     * History Metadata Participant
     * <p>
     * 
     * 
     */
    @SerializedName("generator")
    @Expose
    private Generator generator;
    /**
     * History Metadata Participant
     * <p>
     * 
     * 
     */
    @SerializedName("cause")
    @Expose
    private Cause cause;
    @SerializedName("extraData")
    @Expose
    private ExtraData extraData;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getActivityDescriptionKey() {
        return activityDescriptionKey;
    }

    public void setActivityDescriptionKey(String activityDescriptionKey) {
        this.activityDescriptionKey = activityDescriptionKey;
    }

    public String getEmailDescription() {
        return emailDescription;
    }

    public void setEmailDescription(String emailDescription) {
        this.emailDescription = emailDescription;
    }

    public String getEmailDescriptionKey() {
        return emailDescriptionKey;
    }

    public void setEmailDescriptionKey(String emailDescriptionKey) {
        this.emailDescriptionKey = emailDescriptionKey;
    }

    /**
     * History Metadata Participant
     * <p>
     * 
     * 
     */
    public Actor getActor() {
        return actor;
    }

    /**
     * History Metadata Participant
     * <p>
     * 
     * 
     */
    public void setActor(Actor actor) {
        this.actor = actor;
    }

    /**
     * History Metadata Participant
     * <p>
     * 
     * 
     */
    public Generator getGenerator() {
        return generator;
    }

    /**
     * History Metadata Participant
     * <p>
     * 
     * 
     */
    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    /**
     * History Metadata Participant
     * <p>
     * 
     * 
     */
    public Cause getCause() {
        return cause;
    }

    /**
     * History Metadata Participant
     * <p>
     * 
     * 
     */
    public void setCause(Cause cause) {
        this.cause = cause;
    }

    public ExtraData getExtraData() {
        return extraData;
    }

    public void setExtraData(ExtraData extraData) {
        this.extraData = extraData;
    }

}
