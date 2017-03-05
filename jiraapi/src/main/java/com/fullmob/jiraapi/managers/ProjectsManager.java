package com.fullmob.jiraapi.managers;

import com.fullmob.jiraapi.api.ProjectsApi;
import com.fullmob.jiraapi.models.Project;
import com.fullmob.jiraapi.models.ProjectIssueTypeStatus;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ProjectsManager extends AbstractApiManager<ProjectsApi> {

    public ProjectsManager(ProjectsApi api) {
        super(api);
    }

    public Observable<Response<List<Project>>> getProjects() {
        return api.getProjects().subscribeOn(Schedulers.io());
    }

    public Response<List<ProjectIssueTypeStatus>> getProjectIssueStatus(String projectId) throws IOException {
        return api.getProjectIssueStatusesSync(projectId).execute();
    }

    public List<Project> getProjectsAsync() throws IOException {
        return api.getProjectsSync().execute().body();
    }
}
