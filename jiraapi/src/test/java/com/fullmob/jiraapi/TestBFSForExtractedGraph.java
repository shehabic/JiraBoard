package com.fullmob.jiraapi;


import com.fullmob.graphlib.Graph;
import com.fullmob.graphlib.Node;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class TestBFSForExtractedGraph {
    String json = "{" +
        "\"Submitted\": {" +
        "\"541\": \"Ready to Submit\"," +
        "\"501\": \"Closed\"," +
        "\"271\": \"Released\"" +
        "}," +
        "\"Released\": {" +
        "\"431\": \"Ready to Submit\"," +
        "\"501\": \"Closed\"" +
        "}," +
        "\"In Development\": {" +
        "\"331\": \"Waiting for Dependencies\"," +
        "\"521\": \"Ready for Development\"," +
        "\"501\": \"Closed\"," +
        "\"71\": \"Review\"" +
        "}," +
        "\"Ready to Submit\": {" +
        "\"421\": \"Ready to Merge\"," +
        "\"531\": \"Submitted\"," +
        "\"501\": \"Closed\"" +
        "}," +
        "\"Review\": {" +
        "\"121\": \"In Development\"," +
        "\"501\": \"Closed\"," +
        "\"81\": \"In QA\"" +
        "}," +
        "\"Waiting for Dependencies\": {" +
        "\"321\": \"Review\"," +
        "\"501\": \"Closed\"," +
        "\"291\": \"In Development\"" +
        "}," +
        "\"In Refinement\": {" +
        "\"501\": \"Closed\"," +
        "\"391\": \"Backlog\"," +
        "\"51\": \"Ready for Development\"," +
        "\"461\": \"Icebox\"" +
        "}," +
        "\"Icebox\": {" +
        "\"241\": \"Backlog\"," +
        "\"501\": \"Closed\"" +
        "}," +
        "\"Ready for Development\": {" +
        "\"511\": \"In Development\"," +
        "\"501\": \"Closed\"," +
        "\"381\": \"In Refinement\"," +
        "\"471\": \"Icebox\"," +
        "\"351\": \"Waiting for Dependencies\"" +
        "}," +
        "\"Ready to Merge\": {" +
        "\"411\": \"PM Approval\"," +
        "\"501\": \"Closed\"," +
        "\"91\": \"Ready to Submit\"" +
        "}," +
        "\"PM Approval\": {" +
        "\"451\": \"In QA\"," +
        "\"101\": \"Ready to Merge\"," +
        "\"401\": \"In Development\"," +
        "\"501\": \"Closed\"" +
        "}," +
        "\"Backlog\": {" +
        "\"481\": \"Ready for Development\"," +
        "\"311\": \"Icebox\"," +
        "\"501\": \"Closed\"," +
        "\"151\": \"In Refinement\"" +
        "}," +
        "\"In QA\": {" +
        "\"231\": \"PM Approval\"," +
        "\"111\": \"In Development\"," +
        "\"441\": \"Review\"," +
        "\"501\": \"Closed\"" +
        "}" +
        "}";

    Map<String, Map<String, String>> graphMap
        = new Gson().fromJson(json, new TypeToken<Map<String, Map<String, String>>>() {
    }.getType());

    @Test
    public void testGraph() {
        Graph graph = buildGraphFromConnections(graphMap);

        System.out.println(graph.toString());
    }

    private Graph buildGraphFromConnections(Map<String, Map<String, String>> connections) {
        Graph g = new Graph();
        for (Map.Entry<String, Map<String, String>> entry : connections.entrySet()) {
            addNodeIfDoesNotExist(g, entry.getKey());
            Node sourceNode = g.nameToNode.get(entry.getKey());
            for (Map.Entry<String, String> targetMap : entry.getValue().entrySet()) {
                addNodeIfDoesNotExist(g, targetMap.getValue());
                Node targetNode = g.nameToNode.get(targetMap.getValue());
                targetNode.id = targetMap.getKey();
                sourceNode.addTarget(targetNode);
            }
        }

        return g;
    }

    private void addNodeIfDoesNotExist(Graph g, String name) {
        if (!g.nameToNode.containsKey(name)) {
            g.nameToNode.put(name, new Node(name));
        }
    }

    @Test
    public void testBFS() {
        String from = "Backlog";
        String to = "Review";
        Graph graph = buildGraphFromConnections(graphMap);
        List<String> steps = SearchDFS(graph, from, to);
        System.out.println(steps);

        List<String> steps2 = SearchBFS(graph, from, to);
        System.out.println(steps2);
    }

    private List<String> SearchDFS(Graph graph, String from, String to) {
        Stack<String> steps = new Stack<>();
        List<String> finalSteps = new ArrayList<>();
        Node start = graph.nameToNode.get(from);
        Node end = graph.nameToNode.get(to);
        boolean found = findRoute(start, end, steps, new HashMap<String, Boolean>());

        if (found) {
            while (!steps.isEmpty()) {
                finalSteps.add(steps.pop());
            }
        }

        return finalSteps;
    }

    private boolean findRoute(Node start, Node end, Stack<String> steps, HashMap<String, Boolean> visited) {
        if (visited.containsKey(start.toName)) {
            return false;
        }
        if (start == end) {
            return true;
        }
        visited.put(start.toName, true);
        for (Node child : start.vertices) {
            if (findRoute(child, end, steps, visited)) {
                steps.add(start.findLinkId(child));

                return true;
            }
        }

        return false;
    }


    private List<String> SearchBFS(Graph graph, String from, String to) {
        List<String> finalSteps = new ArrayList<>();
        Stack<String> steps = new Stack<>();
        Queue<Node> queue = new LinkedList<>();
        HashMap<String, Boolean> visited = new HashMap<>();
        Node n = graph.nameToNode.get(from);
        Node end = graph.nameToNode.get(to);
        queue.add(n);
        while (queue.peek() != null) {
            n = queue.poll();
            if (visited.containsKey(n.toName)) {
                continue;
            }
            if (n == end) {
                steps.add(n.toName);
                queue.clear();
            } else {
                steps.add(n.toName);
                visited.put(n.toName, true);
                for (Node v : n.vertices) {
                    queue.add(v);
                }
            }
        }
        while (!steps.isEmpty()) {
            finalSteps.add(steps.pop());
        }

        return finalSteps;
    }
}
