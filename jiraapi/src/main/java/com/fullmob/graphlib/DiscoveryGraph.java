package com.fullmob.graphlib;

import java.util.HashMap;
import java.util.HashSet;

public class DiscoveryGraph {
    public HashMap<String, Boolean> visited = new HashMap<>();
    public HashMap<String, Boolean> visiting = new HashMap<>();


    public HashSet<String> nodeIds = new HashSet<>();

    public int size() {
        return visited.size();
    }

    public boolean isVisited(Node node) {
        return visited.containsKey(createKey(node));
    }

    public boolean isVisiting(Node node) {
        return visiting.containsKey(createKey(node));
    }

    private String createKey(Node node) {
        return node.toName;
    }

    public DiscoveryGraph add(Node node) {
        visited.put(createKey(node), true);
        nodeIds.add(node.toId);

        return this;
    }

    public Node setVisited(Node node) {
        node.name = node.toName;
        visited.put(createKey(node), true);
        nodeIds.add(node.toId);

        return node;
    }
}