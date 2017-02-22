package com.fullmob.graphlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
    public String name;
    public String id;
    public String toName;
    public String toId;
    public List<Node> vertices = new ArrayList<>();
    public Map<String, Map<String, Node>> targets = new HashMap<>();
    public HashMap<String, String> statusesToId = new HashMap<>();

    public Node(String name) {
        this.toName = name;
        this.name = name;
    }

    public Node(String name, String id, String toName, String toId) {
        this.name = name;
        this.id = id;
        this.toId = toId;
        this.toName = toName;
    }

    public Node clone() {
        return new Node(name, id, toName, toId);
    }

    public void addTarget(Node n) {
        HashMap<String, Node> target = new HashMap<>();
        target.put(n.id, n);
        targets.put(n.toName, target);
        vertices.add(n);
        statusesToId.put(n.id, n.toName);
    }

    public String findLinkId(Node v) {
        for (Map.Entry<String, Node> link : targets.get(v.toName).entrySet()) {
            return link.getKey();
        }

        return null;
    }
}
