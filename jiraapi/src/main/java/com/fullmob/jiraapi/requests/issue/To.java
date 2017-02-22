
package com.fullmob.jiraapi.requests.issue;

import com.fullmob.jiraapi.models.issue.StatusCategory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Status
 * <p>
 * 
 * 
 */
public class To {

    @SerializedName("statusColor")
    @Expose
    private String statusColor;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private String id;
    /**
     * Status Category
     * <p>
     * 
     * 
     */
    @SerializedName("statusCategory")
    @Expose
    private StatusCategory statusCategory;

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Status Category
     * <p>
     * 
     * 
     */
    public StatusCategory getStatusCategory() {
        return statusCategory;
    }

    /**
     * Status Category
     * <p>
     * 
     * 
     */
    public void setStatusCategory(StatusCategory statusCategory) {
        this.statusCategory = statusCategory;
    }

}
