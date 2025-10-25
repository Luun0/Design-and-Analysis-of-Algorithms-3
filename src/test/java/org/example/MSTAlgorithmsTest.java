package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MSTAlgorithmsTest {

    private Graph sampleGraph() {
        Graph G = new Graph(6);
        G.addEdge(new Edge(0,1,4));
        G.addEdge(new Edge(0,2,3));
        G.addEdge(new Edge(1,2,1));
        G.addEdge(new Edge(1,3,2));
        G.addEdge(new Edge(2,3,4));
        G.addEdge(new Edge(3,4,2));
        G.addEdge(new Edge(4,5,6));
        return G;
    }

    @Test
    public void mstCostsEqual() {
        Graph G = sampleGraph();
        KruskalMST k = new KruskalMST(G);
        k.run();
        PrimMST p = new PrimMST(G);
        p.run();
        assertEquals(k.weight(), p.weight(), 1e-9);
        assertEquals(G.V()-1, k.edges().size());
        assertEquals(G.V()-1, p.edges().size());
    }

    @Test
    public void mstIsAcyclicAndSpans() {
        Graph G = sampleGraph();
        KruskalMST k = new KruskalMST(G);
        k.run();
        assertEquals(G.V()-1, k.edges().size());
        KruskalMST.UF uf = new KruskalMST.UF(G.V());
        for (Edge e : k.edges()) {
            int a = e.either(), b = e.other(a);
            assertNotEquals(uf.find(a), uf.find(b));
            uf.union(a, b);
        }
        int root = uf.find(0);
        for (int i = 0; i < G.V(); i++) assertEquals(root, uf.find(i));
    }
}