package org.example;

import java.util.*;


public class KruskalMST {
    private final Graph G;
    public long operations = 0;
    private final List<Edge> mst = new ArrayList<>();
    private double total = 0.0;

    public KruskalMST(Graph G) {
        this.G = G;
    }

    public void run() {
        List<Edge> edges = new ArrayList<>();
        for (Edge e : G.edges()) edges.add(e);
        edges.sort(Comparator.naturalOrder());
        operations += edges.size() * (long)Math.log(Math.max(2, edges.size()));
        UF uf = new UF(G.V());
        for (Edge e : edges) {
            int v = e.either(), w = e.other(v);
            operations++;
            if (uf.find(v) != uf.find(w)) {
                uf.union(v, w);
                operations++;
                mst.add(e);
                total += e.weight();
                if (mst.size() == G.V() - 1) break;
            }
        }
    }

    public List<Edge> edges() { return Collections.unmodifiableList(mst); }
    public double weight() { return total; }

    static class UF {
        private final int[] parent, rank;
        public UF(int n) {
            parent = new int[n]; rank = new int[n];
            for (int i=0;i<n;i++) parent[i]=i;
        }
        public int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }
        public void union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return;
            if (rank[ra] < rank[rb]) parent[ra] = rb;
            else if (rank[ra] > rank[rb]) parent[rb] = ra;
            else { parent[rb] = ra; rank[ra]++; }
        }
    }
}