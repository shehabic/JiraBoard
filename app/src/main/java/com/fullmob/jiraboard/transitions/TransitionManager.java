package com.fullmob.jiraboard.transitions;

import android.support.annotation.NonNull;

import com.fullmob.graphlib.dijkstra.DijkstraAlgorithm;
import com.fullmob.graphlib.dijkstra.Edge;
import com.fullmob.graphlib.dijkstra.Graph;
import com.fullmob.graphlib.dijkstra.Vertex;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;
import com.fullmob.jiraboard.ui.models.UIIssueTransition;
import com.fullmob.jiraboard.ui.models.UITransitionItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by shehabic on 27/03/2017.
 */
public class TransitionManager {

    private final DBManagerInterface db;
    private final IssuesManager issuesManager;

    public TransitionManager(DBManagerInterface db, IssuesManager issuesManager) {
        this.db = db;
        this.issuesManager = issuesManager;
    }

    public List<TransitionStep> findShortestPath(Issue issue, UITransitionItem transitionItem) {
        if (transitionItem.isDirect()) {
            return directTransition(issue, transitionItem);
        }

        Graph.Builder builder = createTransitionBuilder(issue);
        Vertex source = builder.getVertex(issue.getIssueFields().getStatus().getName());
        Vertex target = builder.getVertex(transitionItem.transition.toName);
        Graph graph = builder.build();

        return findShortestPath(source, target, graph, builder);

    }

    private List<TransitionStep> directTransition(Issue issue, UITransitionItem transitionItem) {
        List<TransitionStep> transitionSteps = new ArrayList<>();
        transitionSteps.add(new TransitionStep(
            transitionItem.transition.viaId,
            transitionItem.transition.viaName,
            transitionItem.transition.toId,
            transitionItem.transition.toName,
            issue.getIssueFields().getStatus().getStatusCategory().getColorName(),
            transitionItem.transition.toColor
        ));

        return transitionSteps;
    }

    @NonNull
    private List<TransitionStep> findShortestPath(Vertex source, Vertex target, Graph graph, Graph.Builder builder) {
        List<TransitionStep> transitionSteps = new ArrayList<>();
        DijkstraAlgorithm algorithm = new DijkstraAlgorithm(graph);
        algorithm.execute(source);
        LinkedList<Vertex> path = algorithm.getPath(target);
        Vertex lastVertex = path.getFirst();
        for (Vertex v : path) {
            Edge edge = builder.getEdge(lastVertex.getName(), v.getName());
            if (edge != null) {
                TransitionStep step = new TransitionStep(
                    edge.getId(),
                    edge.getEdgeName(),
                    v.getId(),
                    v.getName(),
                    lastVertex.getColor(),
                    v.getColor()
                );
                transitionSteps.add(step);
            }
            lastVertex = v;
        }

        return transitionSteps;
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
}
