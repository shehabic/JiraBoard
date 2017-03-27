package com.fullmob.graphlib.dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shehabic on 27/03/2017.
 */
public class Graph {
    private final List<Vertex> vertices;
    private final List<Edge> edges;

    public Graph(List<Vertex> vertices, List<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public static class Builder {
        private final Map<String, Vertex> verticesMap = new HashMap<>();
        private final List<Vertex> vertices = new ArrayList<>();
        private final List<Edge> edges = new ArrayList<>();
        private final Map<String, Edge> addedEdges = new HashMap<>();

        public Builder addVertex(Vertex vertex) {
            if (!verticesMap.containsKey(vertex.getName())) {
                verticesMap.put(vertex.getName(), vertex);
                vertices.add(vertex);
            }

            return this;
        }

        public Builder addEdge(Edge edge) {
            String key = edge.getSource().getName() + "_" + edge.getDestination().getName();
            if (!addedEdges.containsKey(key)) {
                addedEdges.put(key, edge);
                edges.add(edge);
            }

            return this;
        }

        public Graph build() {
            return new Graph(vertices, edges);
        }

        public Vertex getVertex(String fromName) {
            return verticesMap.get(fromName);
        }

        public Edge getEdge(String srcName, String targetName) {
            String key = srcName + "_" + targetName;

            return addedEdges.get(key);
        }
    }
}