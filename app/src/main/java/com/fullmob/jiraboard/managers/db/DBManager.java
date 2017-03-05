package com.fullmob.jiraboard.managers.db;

import android.content.Context;

import com.fullmob.jiraapi.models.Project;
import com.fullmob.jiraboard.db.data.JiraAvatarUrls;
import com.fullmob.jiraboard.db.data.JiraProject;
import com.fullmob.jiraboard.db.data.JiraSubDomain;
import com.fullmob.jiraboard.db.data.WorkflowDiscoveryTicket;
import com.fullmob.jiraboard.ui.models.SubDomain;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

public class DBManager implements DBManagerInterface {

    public static final long DB_VERSION = 1;
    private Mapper mapper;
    private Context appContext;

    public DBManager(Context appContext) {
        this.appContext = appContext;
        mapper = new Mapper();
    }

    private Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    @Override
    public List<UIProject> findAllProjects(String domain) {
        RealmResults<JiraProject> projects = getRealm().where(JiraProject.class).equalTo("subDomain", domain).findAll();

        return mapper.createUIProjectListFromDbList(projects);
    }

    public Observable<SubDomain> saveSubDomain(String subDomain) {
        return Observable.just(subDomain)
            .flatMap(new Func1<String, Observable<SubDomain>>() {
                @Override
                public Observable<SubDomain> call(String s) {
                    getRealm().beginTransaction();
                    JiraSubDomain jiraSubDomain = getRealm().createObject(JiraSubDomain.class);
                    jiraSubDomain.setCreatedAt(new Date());
                    jiraSubDomain.setSubDomain(s);
                    getRealm().commitTransaction();

                    return Observable.just(new SubDomain(s));
                }
            });
    }

    @Override
    public List<UIProject> addProjectsToSubDomain(String subDomain, final List<Project> projects) {
        List<UIProject> uiProjects = new ArrayList<>();
        for (Project project : projects) {
            getRealm().beginTransaction();
            JiraProject jiraProject = getRealm().createObject(JiraProject.class);
            mapper.createJiraProjectFromProject(jiraProject, project, subDomain);
            jiraProject.setAvatarUrls(
                mapper.fillJiraAvatarUrls(project.getAvatarUrls(), getRealm().createObject(JiraAvatarUrls.class)                )
            );
            getRealm().commitTransaction();
        }
        for (Project project : projects) {
            uiProjects.add(mapper.createUIProjectFromApiProject(project, subDomain));
        }

        return uiProjects;
    }

    @Override
    public void saveProject(UIProject project) {

    }

    @Override
    public void saveDiscoveryJob(WorkflowDiscoveryTicket ticket) {

    }

    @Override
    public List<UIProject> getProjects(String subDomain) {
        RealmResults<JiraProject> results
            = getRealm().where(JiraProject.class).equalTo("subdomain", subDomain).findAll();

        return mapper.createUIProjectListFromDbList(results);
    }

    @Override
    public List<UIProject> getProjectsAsyc(String subDomain) {
        RealmResults<JiraProject> jiraProjects = getRealm().where(JiraProject.class).equalTo("subdomain", subDomain).findAll();

        return mapper.createUIProjectListFromDbList(jiraProjects);
    }
}
