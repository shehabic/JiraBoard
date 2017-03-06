package com.fullmob.jiraboard.db.data;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

public class JiraSubDomain extends RealmObject {

    @Required
    public String subDomain;

    public RealmList<JiraProject> projects;

    public Date createdAt;

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public RealmList<JiraProject> getProjects() {
        return projects;
    }

    public void setProjects(RealmList<JiraProject> projects) {
        this.projects = projects;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
