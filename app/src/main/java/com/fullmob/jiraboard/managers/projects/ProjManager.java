package com.fullmob.jiraboard.managers.projects;

import com.fullmob.jiraapi.managers.ProjectsManager;
import com.fullmob.jiraapi.models.Project;
import com.fullmob.jiraboard.db.data.JiraProject;
import com.fullmob.jiraboard.exceptions.SubDomainNotStoredException;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.db.Mapper;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ProjManager {
    private final ProjectsManager api;
    private final DBManagerInterface dbManager;
    private final EncryptedStorage encStorage;
    private final Mapper mapper;

    public ProjManager(ProjectsManager api, DBManagerInterface dbManager, EncryptedStorage encStorage) {
        this.api = api;
        this.dbManager = dbManager;
        this.encStorage = encStorage;
        this.mapper = new Mapper();
    }

    public rx.Observable<List<UIProject>> findAllJiraProjects() {

        final String subDomain = encStorage.getSubDomain();
        if (subDomain == null || subDomain.isEmpty()) {
            return rx.Observable.error(new SubDomainNotStoredException());
        }
        return Observable.just(subDomain)
            .subscribeOn(Schedulers.io())
            .flatMap(new Func1<String, Observable<List<UIProject>>>() {
                @Override
                public Observable<List<UIProject>> call(String s) {
                    return Observable.just(dbManager.getProjects(s));
                }
            })
            .flatMap(new Func1<List<UIProject>, rx.Observable<List<UIProject>>>() {
                @Override
                public rx.Observable<List<UIProject>> call(List<UIProject> jiraProjects) {
                    List<UIProject> projects = jiraProjects;
                    if (jiraProjects.size() == 0) {
                        try {
                            List<Project> apiProjects = api.getProjectsAsync();
                            return saveProjects(subDomain, apiProjects);
                        } catch (java.io.IOException e) {
                        }
                    }

                    return rx.Observable.just(projects);
                }
            })
            .doOnError(new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
    }

    private rx.Observable<List<UIProject>> saveProjects(final String subDomain, List<Project> projects) {
        return Observable.just(dbManager.addProjectsToSubDomain(subDomain, projects))
            .subscribeOn(Schedulers.io())
            .flatMap(new Func1<List<UIProject>, Observable<List<UIProject>>>() {
                @Override
                public Observable<List<UIProject>> call(List<UIProject> uiProjects) {
                    return rx.Observable.just(dbManager.getProjectsAsyc(subDomain));
                }
            })
            .subscribeOn(Schedulers.io())
            .doOnError(new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
    }

    public void saveDefaultProject(JiraProject jiraProject) {

    }

    public Observable<List<UIProject>> getAllJiraProjects() {
        return Observable.just(dbManager.findAllProjects(encStorage.getSubDomain()));
    }
}
