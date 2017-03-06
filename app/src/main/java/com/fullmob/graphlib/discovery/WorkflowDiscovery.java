package com.fullmob.graphlib.discovery;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fullmob.graphlib.DiscoveryGraph;
import com.fullmob.graphlib.Node;
import com.fullmob.graphlib.TransitionLink;
import com.fullmob.jiraapi.managers.IssuesManager;
import com.fullmob.jiraapi.managers.ProjectsManager;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.models.ProjectIssueTypeStatus;
import com.fullmob.jiraapi.models.issue.Status;
import com.fullmob.jiraapi.requests.issue.Transition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class WorkflowDiscovery {
    private final IssuesManager manager;
    private final ProjectsManager projectsManager;
    private boolean isDebug;

    public WorkflowDiscovery(IssuesManager manager, ProjectsManager projectsManager) {
        this(manager, projectsManager, false);
    }

    public WorkflowDiscovery(IssuesManager manager, ProjectsManager projectsManager, boolean debug) {
        this.manager = manager;
        this.projectsManager = projectsManager;
        isDebug = debug;
    }

    public Observable<DiscoveryStatus> discoverWorkFlow(final String key) throws IOException {
        return Observable.create(new ObservableOnSubscribe<DiscoveryStatus>() {
            @Override
            public void subscribe(final ObservableEmitter<DiscoveryStatus> em) throws Exception {
                final DiscoveryStatus status = createDiscoveryStatus(key);
                HashSet<String> badEnds = new HashSet<>();
                Map<String, List<Node>> childNodes = new HashMap<>();

                for (int i = 0; i < getMaxAttempts(badEnds); i++) {
                    status.increasePasses();
                    Node startingNode = findStartingNode(key);
                    try {
                        exploreOptimized(startingNode, new DiscoveryGraph(), key, childNodes, badEnds, status, new OnNodeVisited() {
                            @Override
                            public void onVisit(DiscoveryStatus discoveryStatus) {
                                em.onNext(status);
                            }
                        });
                    } catch (BadEndException expected) {
                        badEnds.add(manager.getIssueSync(key).body().getIssueFields().getStatus().getName());
                    }
                    if (i < getMaxAttempts(badEnds)) {
                        em.onNext(status);
                    }
                }
                status.markCompleted();
                em.onNext(status);
                em.onComplete();
            }
        });
    }

    @NonNull
    public DiscoveryStatus createDiscoveryStatus(String key) throws IOException {
        Issue issue = manager.getIssueSync(key).body();
        DiscoveryStatus status = new DiscoveryStatus(
            issue.getIssueFields().getProject().getName(),
            issue.getIssueFields().getProject().getKey(),
            key
        );
        status.setPossibleNodes(getListOfPossibleStatusesForTicket(issue, issue.getIssueFields().getProject().getId()));

        return status;
    }

    private HashSet<String> getListOfPossibleStatusesForTicket(Issue issue, String projectId) throws IOException {
        HashSet<String> statuses = new HashSet<>();
        List<ProjectIssueTypeStatus> issueTypeStatuses = projectsManager.getProjectIssueStatus(projectId);
        for (ProjectIssueTypeStatus type : issueTypeStatuses) {
            if (issue.getIssueFields().getIssuetype().getName().equals(type.getName())) {
                for (Status status : type.getStatuses()) {
                    statuses.add(status.getName());
                }
            }
        }

        return statuses;
    }

    @NonNull
    private Node findStartingNode(String ticketKey) throws IOException {
        Issue issue = manager.getIssueSync(ticketKey).body();
        Status status = issue.getIssueFields().getStatus();

        return new Node("none", "0", status.getName(), status.getId());
    }

    private int getMaxAttempts(HashSet<String> deadEnds) {
        return Math.max(1, Math.min(deadEnds.size() + 1, 10));
    }

    private void addConnection(Node src, Node target, DiscoveryStatus status) {
        src.addTarget(target);
        status.addTransition(new TransitionLink(src.toName, target.id, target.name, target.toName));
    }

    private void exploreOptimized(
        Node current,
        DiscoveryGraph discoveryGraph,
        String key,
        Map<String, List<Node>> childNodes,
        HashSet<String> badEnds,
        DiscoveryStatus status,
        OnNodeVisited listener
    ) throws IOException, BadEndException {

        Stack<Node> stack = new Stack<>(), refStack = new Stack<>();
        Node referrer;
        stack.push(current);
        discoveryGraph.setVisited(current);

        while (!stack.isEmpty() && !areAllNodesCompleted(status)) {
            current = stack.peek();
            referrer = !refStack.isEmpty() ? refStack.peek() : null;
            moveToStatus(current, key);
            Node child = getUnvisitedChild(current, key, discoveryGraph, badEnds, referrer, status, childNodes);
            listener.onVisit(status);
            if (child != null) {
                discoveryGraph.setVisited(child);
                stack.push(child);
                refStack.push(current);
            } else {
                status.addCompletedNode(current);
                listener.onVisit(status);
                if (!stack.isEmpty()) {
                    stack.pop();
                }
                if (!refStack.isEmpty()) {
                    refStack.pop();
                    if (referrer != null) {
                        if (!canGoBack(referrer, current, childNodes)) {
                            throw new BadEndException("Can't go back");
                        } else {
                            status.addCompletedBranch(referrer, current);
                        }
                        stack.peek().id = referrer.id;
                    }
                }
                if (isDebug) System.out.println("<-Going back");
            }
        }
    }

    private boolean areAllNodesCompleted(DiscoveryStatus status) {
        return status.getCompletedNodesCount() >= status.getPossibleNodesCount();
    }

    private void moveToStatus(Node current, String key) throws IOException, BadEndException {
        if (!current.id.equals("0")) {
            if (!jumpTo(current, key, "")) {
                throw new BadEndException();
            }
        }
    }


    private boolean canGoBack(Node referrer, Node current, Map<String, List<Node>> childNodes) throws IOException {
        if (childNodes != null) {
            List<Node> allChildNodes = childNodes.get(current.toName);
            for (Node child : allChildNodes) {
                if (isBackLink(referrer, child)) {
                    referrer.id = child.id;
                    return true;
                }
            }
        }
        return false;
    }


    @Nullable
    private Node getUnvisitedChild(
        Node current,
        String key,
        DiscoveryGraph discoveryGraph,
        HashSet<String> badEnds,
        Node referrer,
        DiscoveryStatus status,
        Map<String, List<Node>> childNodes
    ) throws IOException {
        if (!childNodes.containsKey(current.toName)) {
            childNodes.put(current.toName, findAllVertices(key));
        }
        List<Node> allChildNodes = childNodes.get(current.toName);
        for (Node child : allChildNodes) {
            if (isDebug) {
                System.out.println("\t-Child: " + child.id + ":" + child.toName);
            }
            addConnection(current, child, status);
        }

        for (Node child : allChildNodes) {
            if (isBadEnd(child, badEnds, current)) {
                if (!discoveryGraph.isVisited(child)) {
                    discoveryGraph.add(child);
                }
                continue;
            }
            if (isBackLink(referrer, child) || status.isCompletedVertex(current, child)) {
                continue;
            }
            if (!discoveryGraph.isVisited(child)) {
                return child.clone();
            }
        }

        return null;
    }

    private boolean isBackLink(Node referrer, Node child) {
        return referrer != null && referrer.toName.toLowerCase().equals(child.toName.toLowerCase());
    }

    private boolean isBadEnd(Node child, HashSet<String> badEnds, Node current) {
        return current.toName.equals(child.toName)
            || badEnds.contains(child.toName)
            || child.toName.toLowerCase().contains("close");
    }

    private boolean jumpTo(Node target, String key, String prefix) throws IOException {
        boolean done = manager.moveIssueSync(key, target.id).isSuccessful();
        if (isDebug) {
            System.out.println("->" + prefix + target.toName + (done ? "  [OK]" : "  [Err]"));
        }

        return done;
    }

    private Node createFromTransition(Transition trans) {
        return new Node(trans.getName(), trans.getId(), trans.getTo().getName(), trans.getTo().getId());
    }

    private List<Node> findAllVertices(String key) throws IOException {
        List<Transition> transitions = manager.getPossibleIssueTransitionsSync(key).body().getTransitions();
        List<Node> nodes = new ArrayList<>();
        for (Transition t : transitions) {
            nodes.add(createFromTransition(t));
        }

        return nodes;
    }

    public static class BadEndException extends Throwable {
        BadEndException() {
            super("Invalid state exception");
        }

        BadEndException(String s) {
            super(s);
        }
    }

    private interface OnNodeVisited {
        void onVisit(DiscoveryStatus discoveryStatus);
    }
}
