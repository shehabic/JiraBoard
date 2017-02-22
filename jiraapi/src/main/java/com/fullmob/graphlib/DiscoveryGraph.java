package com.fullmob.graphlib;

import java.util.HashMap;
import java.util.HashSet;

public class DiscoveryGraph {
    public HashMap<String, Node> actionsToNodes = new HashMap<>();
    public HashSet<String> nodeIds = new HashSet<>();

    public int size() {
        return actionsToNodes.size();
    }

    public Node findById(String id) {
        return actionsToNodes.get(id);
    }

    public Node getNode(Node node) {
        return actionsToNodes.get(createKey(node));
    }

    public boolean isVisited(Node node) {
        return actionsToNodes.containsKey(createKey(node));
    }

    private String createKey(Node node) {
        return node.toName;
    }

    public DiscoveryGraph add(Node node) {
        Node nodeToAdd = node.clone();
        nodeToAdd.id = node.id != "0" ? node.toId : "0";
        nodeToAdd.name = node.toName;
        actionsToNodes.put(createKey(nodeToAdd), nodeToAdd);
        nodeIds.add(node.toId);

        return this;
    }

    public Node addNode(Node node) {
        Node nodeToAdd = node.clone();
        nodeToAdd.id = node.id != "0" ? node.toId : "0";
        nodeToAdd.name = node.toName;
        actionsToNodes.put(createKey(nodeToAdd), nodeToAdd);
        nodeIds.add(node.toId);

        return nodeToAdd;
    }
}