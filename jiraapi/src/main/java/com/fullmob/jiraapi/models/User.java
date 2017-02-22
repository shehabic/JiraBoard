package com.fullmob.jiraapi.models;

import com.fullmob.jiraapi.models.user.ApplicationRoles;
import com.fullmob.jiraapi.models.user.Groups;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.net.URI;


public class User {

    @SerializedName("self")
    @Expose
    private URI self;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("accountId")
    @Expose
    private String accountId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("avatarUrls")
    @Expose
    private AvatarUrls avatarUrls;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("timeZone")
    @Expose
    private String timeZone;
    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("groups")
    @Expose
    private Groups groups;
    @SerializedName("applicationRoles")
    @Expose
    private ApplicationRoles applicationRoles;
    @SerializedName("expand")
    @Expose
    private String expand;

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public AvatarUrls getAvatarUrls() {
        return avatarUrls;
    }

    public void setAvatarUrls(AvatarUrls avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Simple List Wrapper
     * <p>
     * 
     * 
     */
    public Groups getGroups() {
        return groups;
    }

    /**
     * Simple List Wrapper
     * <p>
     * 
     * 
     */
    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    /**
     * Simple List Wrapper
     * <p>
     * 
     * 
     */
    public ApplicationRoles getApplicationRoles() {
        return applicationRoles;
    }

    /**
     * Simple List Wrapper
     * <p>
     * 
     * 
     */
    public void setApplicationRoles(ApplicationRoles applicationRoles) {
        this.applicationRoles = applicationRoles;
    }

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

}
