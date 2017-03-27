package com.fullmob.graphlib.dijkstra;

/**
 * Created by shehabic on 27/03/2017.
 */
public class Edge {
    private final String id;
    private final String edgeName;
    private final Vertex source;
    private final Vertex destination;

    public Edge(String id,String edgeName, Vertex source, Vertex destination) {
        this.id = id;
        this.edgeName = edgeName;
        this.source = source;
        this.destination = destination;
    }

    public String getId() {
        return id;
    }

    public Vertex getDestination() {
        return destination;
    }

    public Vertex getSource() {
        return source;
    }

    public int getWeight() {
        return 1;
    }

    public String getEdgeName() {
        return edgeName;
    }

    @Override
    public String toString() {
        return source + " " + destination;
    }
}