package com.fullmob.jiraboard.managers.projects;

import com.fullmob.jiraapi.managers.IssuesApiClient;
import com.fullmob.jiraapi.managers.ProjectsApiClient;
import com.fullmob.jiraapi.models.Project;
import com.fullmob.jiraapi.models.ProjectIssueTypeStatus;
import com.fullmob.jiraapi.models.issue.Issuetype;
import com.fullmob.jiraboard.exceptions.SubDomainNotStoredException;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ProjectsManager {
    private final ProjectsApiClient projectsApi;
    private final DBManagerInterface dbManager;
    private final EncryptedStorage encStorage;
    private final IssuesApiClient issuesApi;

    public ProjectsManager(
        ProjectsApiClient api,
        IssuesApiClient issuesApi,
        DBManagerInterface dbManager,
        EncryptedStorage encStorage
    ) {
        this.projectsApi = api;
        this.issuesApi = issuesApi;
        this.dbManager = dbManager;
        this.encStorage = encStorage;
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
                            List<Project> apiProjects = projectsApi.getProjectsAsync();
                            completeAllStatusesForProjects(apiProjects);

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

    private void completeAllStatusesForProjects(List<Project> apiProjects) {
        Map<String, Issuetype> cachedIssueTypes = new HashMap<>();
        for (Project project : apiProjects) {
            try {
                project.setIssueTypeStatuses(projectsApi.getProjectIssueStatus(project.getId()));
                fetchUniqueIssueTypes(project, cachedIssueTypes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchUniqueIssueTypes(Project project, Map<String, Issuetype> cachedIssueTypes) {
        for (ProjectIssueTypeStatus projectIssueTypeStatus : project.getIssueTypeStatuses()) {
            String id = projectIssueTypeStatus.getId();
            if (!cachedIssueTypes.containsKey(id)) {
                try {
                    Issuetype issuetype = issuesApi.getIssueTypeAsync(id).body();
                    cachedIssueTypes.put(issuetype.getId(), issuetype);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (cachedIssueTypes.containsKey(id)) {
                projectIssueTypeStatus.setAvatarId(String.valueOf(cachedIssueTypes.get(id).getAvatarId()));
                projectIssueTypeStatus.setIconUrl(String.valueOf(cachedIssueTypes.get(id).getIconUrl()));
            }
        }
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

    public void saveDefaultProject(UIProject jiraProject) {
        encStorage.saveDefaultProject(jiraProject);
    }

    public Observable<List<UIProject>> getAllJiraProjects() {
        return Observable.just(dbManager.findAllProjects(encStorage.getSubDomain()));
    }

    public Observable<List<UIIssueType>> findAllIssueTypesInCurrentProject() {
        return Observable.just(dbManager.findProjectIssueTypes(encStorage.getDefaultProject()));
    }

    public Observable<HashSet<String>> findUniqueWorkflowsInCurrentProject() {
        return Observable.just(dbManager.findProjectWorkflows(encStorage.getDefaultProject()));
    }

    public Observable<UIProject> getProject(String projectId) {
        return Observable.just(dbManager.getProject(projectId));
    }
}
