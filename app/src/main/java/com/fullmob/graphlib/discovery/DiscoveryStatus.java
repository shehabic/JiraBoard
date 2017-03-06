package com.fullmob.graphlib.discovery;

import com.fullmob.graphlib.Node;
import com.fullmob.graphlib.TransitionLink;

import java.util.HashSet;

public class DiscoveryStatus {
    private String projectName;
    private String ticketKey;
    private String projectKey;
    private HashSet<TransitionLink> vertices;
    private HashSet<String> uniqueNodes;
    private HashSet<String> possibleNodes;
    private HashSet<String> completedNodes;
    private HashSet<String> completedVertices;

    private int passes = 0;
    private boolean completed = false;

    public DiscoveryStatus(String projectName, String projectKey, String ticketKey) {
        vertices = new HashSet<>();
        uniqueNodes = new HashSet<>();
        possibleNodes = new HashSet<>();
        completedNodes = new HashSet<>();
        completedVertices = new HashSet<>();
        this.projectName = projectName;
        this.ticketKey = ticketKey;
        this.projectKey = projectKey;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getTicketKey() {
        return ticketKey;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public HashSet<TransitionLink> getVertices() {
        return vertices;
    }

    public void addTransition(TransitionLink vertex) {
        vertices.add(vertex);
        uniqueNodes.add(vertex.from);
        uniqueNodes.add(vertex.to);
    }

    public void increasePasses() {
        passes++;
    }

    public int getUniqueNodes() {
        return uniqueNodes.size();
    }

    public int getPasses() {
        return passes;
    }

    public void addCompletedNode(Node node) {
        completedNodes.add(node.toName);
    }

    public void setPossibleNodes(HashSet<String> possibleNodes) {
        this.possibleNodes = possibleNodes;
    }

    public int getCompletedNodesCount() {
        return completedNodes.size();
    }

    public int getPossibleNodesCount() {
        return possibleNodes.size();
    }

    public void markCompleted() {
        this.completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void addCompletedBranch(Node referrer, Node current) {
        completedVertices.add(referrer.toName + "_" + current.toName);
    }

    public boolean isCompletedVertex(Node current, Node child) {
        return completedVertices.contains(current.toName + "_" + child.toName);
    }
}
