package com.fullmob.graphlib.discovery;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fullmob.graphlib.DiscoveryGraph;
import com.fullmob.graphlib.Graph;
import com.fullmob.graphlib.Node;
import com.fullmob.graphlib.TransitionLink;
import com.fullmob.jiraapi.managers.IssuesManager;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.models.issue.Status;
import com.fullmob.jiraapi.requests.issue.Transition;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class WorkflowDiscovery {
    private final IssuesManager manager;
    private boolean isDebug;
    public WorkflowDiscovery(IssuesManager manager) {
        this(manager, false);
    }

    public WorkflowDiscovery(IssuesManager manager, boolean debug) {
        this.manager = manager;
        isDebug = debug;
    }

    private void addNodeIfDoesNotExist(Graph g, String name) {
        if (!g.nameToNode.containsKey(name)) {
            g.nameToNode.put(name, new Node(name));
        }
    }

    private Graph buildGraphFromConnections(Map<String, Map<String, String>> connections) {
        Graph g = new Graph();
        for (Map.Entry<String, Map<String, String>> entry : connections.entrySet()) {
            addNodeIfDoesNotExist(g, entry.getKey());
            Node sourceNode = g.nameToNode.get(entry.getKey());
            for (Map.Entry<String, String> targetMap : entry.getValue().entrySet()) {
                addNodeIfDoesNotExist(g, targetMap.getValue());
                Node targetNode = g.nameToNode.get(targetMap.getValue()).clone();
                targetNode.id = targetMap.getKey();
                sourceNode.addTarget(targetNode);
            }
        }

        return g;
    }

    public Observable<HashSet<TransitionLink>> discoverWorkFlow(String ticketKey) throws IOException {
        return Observable.just(ticketKey)
            .flatMap(new Function<String, ObservableSource<HashSet<TransitionLink>>>() {
                @Override
                public ObservableSource<HashSet<TransitionLink>> apply(String ticketKey) throws Exception {
                    HashSet<String> badEnds = new HashSet<>();
                    HashSet<TransitionLink> connections = new HashSet<>();
                    Map<String, List<Node>> childNodes = new HashMap<>();
                    for (int i = 0; i < getMaxAttempts(badEnds); i++) {
                        Node startingNode = findStartingNode(ticketKey);
                        if (isDebug){
                            System.out.println("=======> Attempt " + (i + 1));
                            System.out.println("->" + startingNode.toName);
                        }
                        try {
                            exploreOptimized(startingNode, new DiscoveryGraph(), ticketKey, childNodes, badEnds, connections);
                        } catch (BadEndException e) {
                            badEnds.add(manager.getIssueSync(ticketKey).body().getIssueFields().getStatus().getName());
                        }
                    }
                    System.out.println(new Gson().toJson(connections));

                    return Observable.just(connections);
                }
            });
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

    private void addConnection(Node src, Node target, HashSet<TransitionLink> connections) {
        src.addTarget(target);
        TransitionLink link = new TransitionLink(src.toName, target.id, target.toName);
        if (!connections.contains(link)) {
            connections.add(link);
        }
    }

    private void exploreOptimized(
        Node current,
        DiscoveryGraph discoveryGraph,
        String key,
        Map<String, List<Node>> childNodes,
        HashSet<String> badEnds,
        HashSet<TransitionLink> connections
    ) throws IOException, BadEndException {
        Stack<Node> stack = new Stack<>(), refStack = new Stack<>();
        Node referrer;
        stack.push(current);
        discoveryGraph.setVisited(current);
        while (!stack.isEmpty()) {
            current = stack.peek();
            referrer = !refStack.isEmpty() ? refStack.peek() : null;
            moveToStatus(current, key);
            Node child = getUnvisitedChild(current, key, discoveryGraph, badEnds, referrer, connections, childNodes);
            if (child != null) {
                discoveryGraph.setVisited(child);
                stack.push(child);
                refStack.push(current);
            } else {
                if (!stack.isEmpty()) {
                    stack.pop();
                }
                if (!refStack.isEmpty()) {
                    refStack.pop();
                    if (referrer != null) {
                        if (!canGoBack(referrer, current, childNodes)) {
                            throw new BadEndException("Can't go back");
                        }
                        stack.peek().id = referrer.id;
                    }
                }
                if (isDebug) System.out.println("<-Going back");
            }
        }
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
        HashSet<TransitionLink> connections,
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
            addConnection(current, child, connections);
            if (isBackLink(referrer, child)) {
                continue;
            }
        }

        for (Node child : allChildNodes) {
            if (isBadEnd(child, badEnds, current)) {
                if (!discoveryGraph.isVisited(child)) {
                    discoveryGraph.add(child);
                }
                continue;
            }
            if (isBackLink(referrer, child)) {
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

    private class BadEndException extends Throwable {
        public BadEndException() {
            super("Invalid state exception");
        }

        public BadEndException(String s) {
            super(s);
        }
    }
}
