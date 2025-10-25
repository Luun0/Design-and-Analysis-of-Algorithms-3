package org.example;

import java.util.*;


public class Graph {
    private final int V;
    private int E;
    private final List<List<Edge>> adj;

    public Graph(int V) {
        this.V = V;
        this.E = 0;
        this.adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(Edge e) {
        int v = e.either(), w = e.other(v);
        adj.get(v).add(e);
        adj.get(w).add(e);
        E++;
    }

    public Iterable<Edge> edges() {
        List<Edge> list = new ArrayList<>();
        boolean[][] seen = new boolean[V][V];
        for (int v = 0; v < V; v++) {
            for (Edge e : adj.get(v)) {
                int w = e.other(v);
                if (!seen[Math.min(v,w)][Math.max(v,w)]) {
                    list.add(e);
                    seen[Math.min(v,w)][Math.max(v,w)] = true;
                }
            }
        }
        return list;
    }

    public Iterable<Edge> adj(int v) { return Collections.unmodifiableList(adj.get(v)); }
    public int V() { return V; }
    public int E() { return E; }
}