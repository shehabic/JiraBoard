package com.fullmob.jiraboard.db.data;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class JiraProject extends RealmObject {

    @Required
    private String id;

    @Required
    private String subdomain;

    private String expand;

    private String self;

    @Required
    private String key;

    @Required
    private String name;

    private String description;
    private RealmList<JiraIssuetype> issueTypes = null;
    private String assigneeType;
    private JiraAvatarUrls avatarUrls;
    private String projectTypeKey;

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RealmList<JiraIssuetype> getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(RealmList<JiraIssuetype> issueTypes) {
        this.issueTypes = issueTypes;
    }

    public String getAssigneeType() {
        return assigneeType;
    }

    public void setAssigneeType(String assigneeType) {
        this.assigneeType = assigneeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JiraAvatarUrls getAvatarUrls() {
        return avatarUrls;
    }

    public void setAvatarUrls(JiraAvatarUrls avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    public String getProjectTypeKey() {
        return projectTypeKey;
    }

    public void setProjectTypeKey(String projectTypeKey) {
        this.projectTypeKey = projectTypeKey;
    }

    public String getSubDomain() {
        return subdomain;
    }

    public void setSubDomain(String subDomain) {
        this.subdomain = subDomain;
    }
}
