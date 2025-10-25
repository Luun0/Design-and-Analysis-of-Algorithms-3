package org.example;

import java.util.List;

public class MSTResult {
    public String graphId;
    public String algorithm;
    public List<String> edges;
    public double totalCost;
    public int originalVertices;
    public int originalEdges;
    public long operations;
    public long timeMs;

    public MSTResult() {}
}