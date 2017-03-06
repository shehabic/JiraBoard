package com.fullmob.jiraboard.db.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class JiraIssueStatus extends RealmObject {

    @Required
    private String name;

    @PrimaryKey
    @Required
    private String id;

    @Required
    private String self;

    private String description;

    private String iconUrl;

    private JiraStatusCategory statusCategory;

    public JiraIssueStatus() {
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

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
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

    public JiraStatusCategory getStatusCategory() {
        return statusCategory;
    }

    public void setStatusCategory(JiraStatusCategory statusCategory) {
        this.statusCategory = statusCategory;
    }
}
