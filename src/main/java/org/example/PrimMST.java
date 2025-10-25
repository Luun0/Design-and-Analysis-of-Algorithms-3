package org.example;

import java.util.*;


public class PrimMST {
    private final Graph G;
    public long operations = 0;
    private final boolean[] marked;
    private final Edge[] edgeTo;
    private final double[] distTo;
    private final List<Edge> mst = new ArrayList<>();

    public PrimMST(Graph G) {
        this.G = G;
        int V = G.V();
        marked = new boolean[V];
        edgeTo = new Edge[V];
        distTo = new double[V];
        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
    }

    public void run() {
        for (int s = 0; s < G.V(); s++) {
            if (!marked[s]) prim(s);
        }
        for (Edge e : edgeTo) if (e != null) mst.add(e);
    }

    private void prim(int s) {
        distTo[s] = 0.0;
        PriorityQueue<VertexDist> pq = new PriorityQueue<>();
        pq.add(new VertexDist(s, 0.0));
        while (!pq.isEmpty()) {
            VertexDist vd = pq.poll();
            int v = vd.v;
            if (marked[v]) continue;
            marked[v] = true;
            operations++;
            for (Edge e : G.adj(v)) {
                int w = e.other(v);
                if (marked[w]) continue;
                operations++;
                if (e.weight() < distTo[w]) {
                    distTo[w] = e.weight();
                    edgeTo[w] = e;
                    pq.add(new VertexDist(w, distTo[w]));
                    operations++;
                }
            }
        }
    }

    public List<Edge> edges() { return Collections.unmodifiableList(mst); }
    public double weight() {
        double sum = 0.0;
        for (Edge e : edges()) sum += e.weight();
        return sum;
    }

    private static class VertexDist implements Comparable<VertexDist> {
        int v;
        double d;
        VertexDist(int v, double d) { this.v = v; this.d = d; }
        public int compareTo(VertexDist o) { return Double.compare(this.d, o.d); }
    }
}