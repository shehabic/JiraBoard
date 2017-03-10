package com.fullmob.jiraboard.managers.db;

import com.fullmob.graphlib.TransitionLink;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.models.Project;
import com.fullmob.jiraapi.models.ProjectIssueTypeStatus;
import com.fullmob.jiraapi.models.issue.Status;
import com.fullmob.jiraapi.models.issue.StatusCategory;
import com.fullmob.jiraboard.db.data.JiraAvatarUrls;
import com.fullmob.jiraboard.db.data.JiraIssueStatus;
import com.fullmob.jiraboard.db.data.JiraIssueType;
import com.fullmob.jiraboard.db.data.JiraProject;
import com.fullmob.jiraboard.db.data.JiraStatusCategory;
import com.fullmob.jiraboard.db.data.JiraSubDomain;
import com.fullmob.jiraboard.db.data.WorkflowDiscoveryQueueJob;
import com.fullmob.jiraboard.db.data.workflow.Vertices;
import com.fullmob.jiraboard.ui.models.SubDomain;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.fullmob.jiraboard.ui.models.UIProject;
import com.fullmob.jiraboard.ui.models.UIWorkflowQueueJob;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

public class DBManager implements DBManagerInterface {

    public static final long DB_VERSION = 1;
    public static final String COL_SUB_DOMAIN = "subDomain";
    public static final String COL_PROJECT_ID = "projectId";
    public static final String COL_ID = "id";
    public static final String COL_WORKFLOW_KEY = "workflowKey";
    public static final String COL_JIRA_ID = "jiraId";
    private static final String COL_WORKFLOW_QUEUE_JOB_KEY = "jobKey";
    private Mapper mapper;

    public DBManager() {
        mapper = new Mapper();
    }

    private Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    @Override
    public List<UIProject> findAllProjects(String domain) {
        RealmResults<JiraProject> projects = getRealm().where(JiraProject.class).equalTo(COL_SUB_DOMAIN, domain).findAll();

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
        saveAllProjects(subDomain, projects);
        for (Project project : projects) {
            uiProjects.add(mapper.createUIProjectFromApiProject(project, subDomain));
        }

        return uiProjects;
    }

    public void saveAllProjects(String subDomain, List<Project> projects) {
        Map<String, RealmList<JiraIssueType>> projectsIssueTypes = createAllIssueStatusesAndCategories(projects);
        for (Project project : projects) {
            getRealm().beginTransaction();
            setProjectData(subDomain, project, projectsIssueTypes);
            getRealm().commitTransaction();
        }
    }

    /**
     * Proj
     * |
     * |---IssueType1
     * |        |______Status1-->StatusCategory1
     * |        |______Status2-->StatusCategory1
     * |
     * |---IssueType2
     * |        |______Status3-->StatusCategory1
     * |        |______Status4-->StatusCategory2
     * |        |______Status5-->StatusCategory2
     * |
     * |---IssueType2
     * |______Status6-->StatusCategory2
     * |______Status7-->StatusCategory3
     */
    private Map<String, RealmList<JiraIssueType>> createAllIssueStatusesAndCategories(List<Project> projects) {
        Map<String, RealmList<JiraIssueType>> projectIssueTypes = new HashMap<>();
        Map<Integer, JiraStatusCategory> jiraStatusCategories;
        Map<String, JiraIssueStatus> jiraIssueStatuses;
        Map<Integer, StatusCategory> apiStatusCategories;
        Map<String, Status> apiIssueStatuses;
        Map<String, ProjectIssueTypeStatus> apiIssuesType;

        for (Project project : projects) {
            for (ProjectIssueTypeStatus issueType : project.getIssueTypeStatuses()) {
                issueType.setProjectId(project.getId());
            }
        }

        for (Project project : projects) {
            apiStatusCategories = new HashMap<>();
            apiIssueStatuses = new HashMap<>();
            apiIssuesType = new HashMap<>();
            jiraIssueStatuses = new HashMap<>();
            jiraStatusCategories = new HashMap<>();

            // Loop through issue-types in this project
            for (ProjectIssueTypeStatus issueType : project.getIssueTypeStatuses()) {
                ProjectIssueTypeStatus temp = issueType.clone();
                temp.getStatuses().clear();
                apiIssuesType.put(issueType.getId(), temp);
                for (Status status : issueType.getStatuses()) {
                    apiStatusCategories.put(status.getStatusCategory().getId(), status.getStatusCategory().clone());
                    if (!apiIssueStatuses.containsKey(status.getId())) {
                        apiIssueStatuses.put(status.getId(), status.clone());
                    }
                    apiIssuesType.get(issueType.getId()).getStatuses().add(apiIssueStatuses.get(status.getId()));
                }
                // Create all status categories found so far
                createJiraStatusCategories(jiraStatusCategories, apiStatusCategories);
                // create all statuses found so far
                createJiraIssueStatuses(apiIssueStatuses, jiraIssueStatuses, jiraStatusCategories);
                // create all issue-types found so far
                projectIssueTypes.put(project.getId(), createJiraIssueTypes(apiIssuesType, jiraIssueStatuses));
            }
        }

        return projectIssueTypes;
    }

    private RealmList<JiraIssueType> createJiraIssueTypes(
        Map<String, ProjectIssueTypeStatus> apiIssuesType,
        Map<String, JiraIssueStatus> jiraIssueStatuses
    ) {
        RealmList<JiraIssueType> jiraIssueTypes = new RealmList<>();
        for (Map.Entry<String, ProjectIssueTypeStatus> entry : apiIssuesType.entrySet()) {
            jiraIssueTypes.add(getOrCreateIssueType(entry.getValue(), jiraIssueStatuses));
        }

        return jiraIssueTypes;
    }

    private JiraIssueType getOrCreateIssueType(ProjectIssueTypeStatus apiTypeStatus, Map<String, JiraIssueStatus> jiraIssueStatuses) {
        JiraIssueType jiraIssueType;
        jiraIssueType = getRealm().where(JiraIssueType.class)
            .equalTo(COL_ID, apiTypeStatus.getId()).findAll()
            .where()
            .equalTo(COL_PROJECT_ID, apiTypeStatus.getProjectId())
            .findFirst();
        if (jiraIssueType == null) {
            getRealm().beginTransaction();
            jiraIssueType = getRealm().createObject(JiraIssueType.class);
            mapper.fillJiraIssueType(jiraIssueType, apiTypeStatus, jiraIssueStatuses);
            getRealm().commitTransaction();
        }

        return jiraIssueType;
    }

    private void createJiraIssueStatuses(
        Map<String, Status> apiIssueStatuses,
        Map<String, JiraIssueStatus> jiraIssueStatuses,
        Map<Integer, JiraStatusCategory> jiraStatusCategories
    ) {
        for (Map.Entry<String, Status> entry : apiIssueStatuses.entrySet()) {
            jiraIssueStatuses.put(entry.getKey(), getOrCreateIssueStatus(entry.getValue(), jiraStatusCategories));
        }
    }

    private void createJiraStatusCategories(
        Map<Integer, JiraStatusCategory> jiraStatusCategories,
        Map<Integer, StatusCategory> apiStatusCategories
    ) {
        for (Map.Entry<Integer, StatusCategory> entry : apiStatusCategories.entrySet()) {
            jiraStatusCategories.put(entry.getKey(), getOrCreateStatusCategory(entry.getValue()));
        }
    }

    private JiraIssueStatus getOrCreateIssueStatus(
        Status status,
        Map<Integer, JiraStatusCategory> jiraStatusCategories
    ) {
        JiraIssueStatus jiraIssueStatus;
        jiraIssueStatus = getRealm().where(JiraIssueStatus.class).equalTo(COL_ID, status.getId()).findFirst();
        if (jiraIssueStatus == null) {
            getRealm().beginTransaction();
            jiraIssueStatus = getRealm().createObject(JiraIssueStatus.class, status.getId());
            mapper.fillJiraIssueStatus(jiraIssueStatus, status, jiraStatusCategories);
            getRealm().commitTransaction();
        }

        return jiraIssueStatus;
    }

    private JiraStatusCategory getOrCreateStatusCategory(StatusCategory statusCategory) {
        JiraStatusCategory jiraStatusCategory;
        jiraStatusCategory = getRealm().where(JiraStatusCategory.class).equalTo(COL_ID, statusCategory.getId()).findFirst();
        if (jiraStatusCategory == null) {
            getRealm().beginTransaction();
            jiraStatusCategory = getRealm().createObject(JiraStatusCategory.class, statusCategory.getId());
            mapper.fillJiraStatusCategory(jiraStatusCategory, statusCategory);
            getRealm().commitTransaction();
        }

        return jiraStatusCategory;
    }

    private void setProjectData(String subDomain, Project project, Map<String, RealmList<JiraIssueType>> projectsIssueTypes) {
        JiraProject jiraProject = getOrCreateProject(project, subDomain);
        jiraProject = mapper.createJiraProjectFromProject(jiraProject, project, subDomain);
        if (projectsIssueTypes.containsKey(jiraProject.getJiraId())) {
            jiraProject.setIssueTypes(projectsIssueTypes.get(jiraProject.getJiraId()));
        }
        jiraProject.setAvatarUrls(
            mapper.fillJiraAvatarUrls(project.getAvatarUrls(), getRealm().createObject(JiraAvatarUrls.class))
        );
    }

    private JiraProject getOrCreateProject(Project project, String subDomain) {
        JiraProject jiraProject = getRealm()
            .where(JiraProject.class).equalTo(COL_ID, project.getId())
            .findAll()
            .where().equalTo(COL_SUB_DOMAIN, subDomain)
            .findFirst();

        if (jiraProject == null) {
            jiraProject = getRealm().createObject(JiraProject.class, project.getId());
        }

        return jiraProject;
    }

    @Override
    public void saveProject(UIProject project) {
    }

    @Override
    public void saveDiscoveryJob(WorkflowDiscoveryQueueJob ticket) {
    }

    @Override
    public List<UIProject> getProjects(String subDomain) {
        RealmResults<JiraProject> results
            = getRealm().where(JiraProject.class).equalTo(COL_SUB_DOMAIN, subDomain).findAll();

        return mapper.createUIProjectListFromDbList(results);
    }

    @Override
    public List<UIProject> getProjectsAsyc(String subDomain) {
        RealmResults<JiraProject> jiraProjects
            = getRealm().where(JiraProject.class).equalTo(COL_SUB_DOMAIN, subDomain).findAll();

        return mapper.createUIProjectListFromDbList(jiraProjects);
    }

    private RealmResults<JiraIssueType> fetchProjectIssueTypes(String projectId) {
        return getRealm().where(JiraIssueType.class)
            .equalTo(COL_PROJECT_ID, projectId)
            .findAll();
    }

    @Override
    public List<UIIssueType> findProjectIssueTypes(String projectId) {
        RealmResults<JiraIssueType> issueTypes = fetchProjectIssueTypes(projectId);

        return mapper.convertJiraIssueTypesToUIIssueTypes(issueTypes);
    }

    @Override
    public HashSet<String> findProjectWorkflows(String projectId) {
        HashSet<String> workflows = new HashSet<>();
        List<UIIssueType> issueTypes = findProjectIssueTypes(projectId);
        if (issueTypes.size() > 0) {
            for (UIIssueType type : issueTypes) {
                workflows.add(type.getWorkflowKey());
            }
        }

        return workflows;
    }

    @Override
    public UIProject getProject(String projectId) {
        JiraProject project = getRealm().where(JiraProject.class).equalTo(COL_JIRA_ID, projectId).findFirst();

        return project != null ? mapper.createUIProjectFromDB(project) : null;
    }

    @Override
    public UIWorkflowQueueJob queueWorkflowDiscoveryTicket(Issue issue, UIProject uiProject, UIIssueType issueType) {
        String jobKey
            = issue.getKey() + "_" + uiProject.getId() + "_" + uiProject.getSubDomain() + System.currentTimeMillis();
        getRealm().beginTransaction();
        WorkflowDiscoveryQueueJob job = getRealm().createObject(WorkflowDiscoveryQueueJob.class);
        job.setJobKey(jobKey);
        job.setSubDomain(uiProject.getSubDomain());
        job.setWorkflowKey(issueType.getWorkflowKey());
        job.setKey(issue.getKey());
        job.setProject(uiProject.getId());
        job.setDiscoveryStatus(WorkflowDiscoveryQueueJob.STATUS_PENDING);
        job.setTitle(issue.getIssueFields().getSummary());
        job.setStatusId(issue.getIssueFields().getStatus().getId());
        job.setTypeId(issueType.getId());
        getRealm().commitTransaction();

        return mapper.createUIWorkflowQueueJob(job);
    }

    @Override
    public UIWorkflowQueueJob getUIWorkflowDiscoveryJob(String key) {
        WorkflowDiscoveryQueueJob job = findWorkflowQueueJob(key);

        return job != null ? mapper.createUIWorkflowQueueJob(job) : null;
    }

    @Override
    public WorkflowDiscoveryQueueJob findWorkflowQueueJob(String key) {
        return getRealm().where(WorkflowDiscoveryQueueJob.class).equalTo(COL_WORKFLOW_QUEUE_JOB_KEY, key).findFirst();
    }

    @Override
    public void updateWorkflowQueueJob(UIWorkflowQueueJob uiJob) {
        getRealm().beginTransaction();
        WorkflowDiscoveryQueueJob job = findWorkflowQueueJob(uiJob.getJobKey());
        mapper.updateWorkflowDiscoveryQueueJob(uiJob, job);
        getRealm().commitTransaction();
    }

    @Override
    public void addVertex(TransitionLink link, String jobKey) {
        getRealm().beginTransaction();
        WorkflowDiscoveryQueueJob job = findWorkflowQueueJob(jobKey);
        Vertices vertex = getRealm().createObject(Vertices.class);
        vertex.setSubDomain(job.getSubDomain());
        vertex.setProjectId(job.getProject());
        vertex.setIssueType(job.getTypeId());
        vertex.setLinkId(link.linkId);
        vertex.setLinkName(link.linkName);
        vertex.setSourceStatus(link.from);
        vertex.setTargetStatus(link.to);
        vertex.setWorkflowDiscoveryTicket(job);
        getRealm().commitTransaction();
    }
}
