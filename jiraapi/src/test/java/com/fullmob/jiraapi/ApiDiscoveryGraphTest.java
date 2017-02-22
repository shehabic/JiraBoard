package com.fullmob.jiraapi;

import android.support.annotation.NonNull;

import com.fullmob.graphlib.DiscoveryGraph;
import com.fullmob.graphlib.Node;
import com.fullmob.jiraapi.managers.IssuesManager;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.models.issue.Status;
import com.fullmob.jiraapi.requests.issue.Transition;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ApiDiscoveryGraphTest extends BaseUnitTest {

    private IssuesManager manager;

    @Before
    public void setup() {
        manager = new IssuesManager(getIssuesApi());
    }

    private int getMaxAttempts(HashSet<String> deadEnds) {
        return Math.max(1, Math.min(deadEnds.size() + 1, 10));
    }

    @Test
    public void TestLearningTheStatusesGraph() throws Exception {
        String ticketKey = TEST_TICKET_ID;
        HashSet<String> badEnds = new HashSet<>();
        HashMap<String, Map<String, String>> connections = new HashMap<>();
        for (int i = 0; i < getMaxAttempts(badEnds); i++) {
            Node startingNode = findStartingNode(ticketKey);
            System.out.println("->" + startingNode.toName);
            try {
                explore(startingNode, new DiscoveryGraph(), ticketKey, startingNode, badEnds, connections);
            } catch (BadEndException e) {
                badEnds.add(manager.getIssueSync(ticketKey).body().getIssueFields().getStatus().getName());
            }
        }
        debugOutput(badEnds, connections);
    }

    @NonNull
    protected Node findStartingNode(String ticketKey) throws IOException {
        Issue issue = manager.getIssueSync(ticketKey).body();
        Status status = issue.getIssueFields().getStatus();

        return new Node("none", "0", status.getName(), status.getId());
    }

    private void addConnection(Node src, Node target, Map<String, Map<String, String>> connections) {
        src.addTarget(target);
        if (!connections.containsKey(src.toName)) {
            connections.put(src.toName, new HashMap<String, String>());
        }
        connections.get(src.toName).put(target.id, target.toName);
    }

    private Node explore(
        Node current,
        DiscoveryGraph discoveryGraph,
        String key,
        Node referrer,
        HashSet<String> badEnds,
        Map<String, Map<String, String>> connections
    ) throws IOException, BadEndException {

        Node cloned = null;
        if (!discoveryGraph.isVisited(current)) {
            cloned = discoveryGraph.addNode(current.clone());
            Node toPreviousNode = null;

            if (!current.id.equals("0")) {
                if (!jumpTo(current, key, "")) {
                    throw new BadEndException();
                }
            }

            for (Node child : findAllVertices(key, current)) {
                addConnection(cloned, child, connections);
                System.out.println("\t-Child: " + child.toName);
                if (isBadEnd(child, badEnds, current)) {
                    if (!discoveryGraph.isVisited(child)) {
                        discoveryGraph.add(child);
                    }
                    continue;
                }
                if (isBackLink(referrer, child)) {
                    toPreviousNode = child;
                    continue;
                }

                if (!discoveryGraph.isVisited(child)) {
                    explore(child, discoveryGraph, key, current, badEnds, connections);
                }
            }

            if (toPreviousNode != null) {
                if (!jumpTo(toPreviousNode, key, "[Back To] ")) {
                    throw new BadEndException();
                }
            }
        }

        return cloned;
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
        System.out.println("->" + prefix + target.toName + (done ? "  [OK]" : "  [Err]"));

        return done;
    }

    private Node createFromTransition(Transition trans) {
        return new Node(trans.getName(), trans.getId(), trans.getTo().getName(), trans.getTo().getId());
    }

    private List<Node> findAllVertices(String key, Node visiting) throws IOException {
        List<Transition> transitions = manager.getPossibleIssueTransitionsSync(key).body().getTransitions();
        List<Node> nodes = new ArrayList<>();
        for (Transition t : transitions) {
            nodes.add(createFromTransition(t));
        }

        return nodes;
    }

    private void debugOutput(HashSet<String> badEnds, Map<String, Map<String, String>> connections) {
        System.out.println(
            "Did " + getMaxAttempts(badEnds) + " attempt(s) and found " + connections.size() + " useful nodes\n\n"
        );
        System.out.println(new Gson().toJson(connections));
    }

    private class BadEndException extends Throwable {
    }
}