package com.fullmob.jiraboard.transitions;

import android.support.annotation.NonNull;

import com.fullmob.graphlib.dijkstra.DijkstraAlgorithm;
import com.fullmob.graphlib.dijkstra.Edge;
import com.fullmob.graphlib.dijkstra.Graph;
import com.fullmob.graphlib.dijkstra.Vertex;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.managers.queue.QueueManager;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;
import com.fullmob.jiraboard.ui.models.UIIssueTransition;
import com.fullmob.jiraboard.ui.models.UITransitionItem;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by shehabic on 27/03/2017.
 */
public class TransitionManager {

    private final DBManagerInterface db;
    private final IssuesManager issuesManager;
    private final QueueManager queue;

    public TransitionManager(DBManagerInterface db, IssuesManager issuesManager, QueueManager queue) {
        this.db = db;
        this.issuesManager = issuesManager;
        this.queue = queue;
    }

    public TransitionSteps findShortestPath(Issue issue, UITransitionItem transitionItem) {
        if (transitionItem.isDirect()) {
            return directTransition(issue, transitionItem);
        }

        Graph.Builder builder = createTransitionBuilder(issue);
        Vertex source = builder.getVertex(issue.getIssueFields().getStatus().getName());
        Vertex target = builder.getVertex(transitionItem.transition.toName);
        Graph graph = builder.build();

        return findShortestPath(source, target, graph, builder);

    }

    private TransitionSteps directTransition(Issue issue, UITransitionItem transitionItem) {
        TransitionSteps steps = new TransitionSteps();
        steps.add(new TransitionStep(
            transitionItem.transition.viaId,
            transitionItem.transition.viaName,
            transitionItem.transition.toId,
            transitionItem.transition.toName,
            issue.getIssueFields().getStatus().getStatusCategory().getColorName(),
            transitionItem.transition.toColor
        ));

        return steps;
    }

    @NonNull
    private TransitionSteps findShortestPath(Vertex source, Vertex target, Graph graph, Graph.Builder builder) {
        TransitionSteps steps = new TransitionSteps();
        DijkstraAlgorithm algorithm = new DijkstraAlgorithm(graph);
        algorithm.execute(source);
        LinkedList<Vertex> path = algorithm.getPath(target);
        Vertex lastVertex = path.getFirst();
        for (Vertex v : path) {
            Edge edge = builder.getEdge(lastVertex.getName(), v.getName());
            if (edge != null) {
                steps.add(
                    new TransitionStep(
                        edge.getId(),
                        edge.getEdgeName(),
                        v.getId(),
                        v.getName(),
                        lastVertex.getColor(),
                        v.getColor()
                    )
                );
            }
            lastVertex = v;
        }

        return steps;
    }

    private String findColor(Map<String, UIIssueStatus> statuses, String name) {
        return statuses.containsKey(name) ? statuses.get(name).getStatusCategory().getColorName() : "blue";
    }

    @NonNull
    private Graph.Builder createTransitionBuilder(Issue issue) {
        HashSet<UIIssueTransition> transitions = db.findDistinctTransitionsForIssue(issue);
        Graph.Builder builder = new Graph.Builder();
        Map<String, UIIssueStatus> statuses
            = issuesManager.getUniqueStatuses(issue.getIssueFields().getProject().getId());
        for (UIIssueTransition trans : transitions) {
            builder.addVertex(new Vertex(trans.fromId, trans.fromName, findColor(statuses, trans.fromName)));
            builder.addVertex(new Vertex(trans.toId, trans.toName, findColor(statuses, trans.toName)));
            builder.addEdge(
                new Edge(
                    trans.viaId,
                    trans.viaName,
                    builder.getVertex(trans.fromName),
                    builder.getVertex(trans.toName)
                )
            );
        }
        return builder;
    }

    public Observable<TransitionStatus> startTransition(final TransitionJob job) {
        return Observable.create(new ObservableOnSubscribe<TransitionStatus>() {
                @Override
                public void subscribe(ObservableEmitter<TransitionStatus> e) throws Exception {
                    TransitionStatus transitionStatus = new TransitionStatus(job);
                    Issue issue = issuesManager.getIssueSync(job.getIssueKey());
                    if (issue.getIssueFields().getStatus().getName().equals(job.getCurrentState())) {
                        int completedSteps = 0;
                        for (TransitionStep step : job.getTransitionSteps().getSteps()) {
                            Response response = issuesManager.moveIssue(job.getIssueKey(), step.viaId);
                            if (response.code() >= 200 && response.code() <= 210) {
                                job.setCurrentState(step.toName);
                                completedSteps++;
                                updateJobStatus(job, null, completedSteps);
                                transitionStatus.incrementCompletedSteps();
                                transitionStatus.updateState(step.toName, step.toColor);
                                e.onNext(transitionStatus);
                            } else {
                                transitionStatus.setIssueFailed(true);
                                e.onNext(transitionStatus);
                                throw new RuntimeException("Can't move ticket to " + step.toName);
                            }
                        }
                    } else {
                        // Handle another crap ship
                    }
                }
            })
            .doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    updateJobStatus(job, throwable, 0);
                }
            })
            .subscribeOn(Schedulers.io());
    }

    private void updateJobStatus(TransitionJob job, Throwable throwable, int completedSteps) {
        if (throwable == null) {
            TransitionJob jobToSave = job.clone();
            TransitionSteps newSteps = new TransitionSteps();
            if (job.getTransitionSteps().getSteps().size() > completedSteps) {
                newSteps.getSteps().addAll(
                    job.getTransitionSteps().getSteps().subList(
                        completedSteps,
                        job.getTransitionSteps().getSteps().size()
                    )
                );
            }
            jobToSave.getTransitionSteps().getSteps().clear();
            jobToSave.getTransitionSteps().getSteps().addAll(newSteps.getSteps());
            db.updateTransitionJob(job);
        } else {
            job.setAttempts(job.getAttempts() + 1);
            job.setStatus(TransitionJob.STATUS_FAILED);
            db.updateTransitionJob(job);
        }
    }

    public TransitionJob getTransitionJob(String queueJobKey) {
        return db.findTransitionJob(queueJobKey);
    }

    public void scheduleTransitionJob(Issue issue, TransitionSteps steps) {
        TransitionJob job = db.createTransitionQueueJob(issue, steps);
        queue.enqueueTransitionJob(job);
    }
}
