package org.example;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;


public class Main {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static class JsonGraphCompact {
        int vertices;
        List<List<Object>> edges;
    }

    static class ParsedGraph {
        final Graph graph;
        final String id;
        ParsedGraph(Graph g, String id) { this.graph = g; this.id = id; }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: Main input.json output.json summary.csv");
            return;
        }

        Path in = Paths.get(args[0]);
        Path out = Paths.get(args[1]);
        Path csv = Paths.get(args[2]);

        List<ParsedGraph> parsed = new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(in)) {
            JsonElement root = JsonParser.parseReader(reader);

            if (root.isJsonObject() && root.getAsJsonObject().has("graphs")) {
                JsonArray graphs = root.getAsJsonObject().getAsJsonArray("graphs");
                for (int gi = 0; gi < graphs.size(); gi++) {
                    JsonObject gobj = graphs.get(gi).getAsJsonObject();
                    String gid = gobj.has("id") ? gobj.get("id").getAsString() : String.valueOf(gi+1);

                    Map<String,Integer> index = new LinkedHashMap<>();
                    if (gobj.has("nodes") && gobj.get("nodes").isJsonArray()) {
                        JsonArray nodes = gobj.getAsJsonArray("nodes");
                        for (int i = 0; i < nodes.size(); i++) index.put(nodes.get(i).getAsString(), i);
                    }

                    List<JsonObject> edgeObjs = new ArrayList<>();
                    if (gobj.has("edges") && gobj.get("edges").isJsonArray()) {
                        JsonArray edges = gobj.getAsJsonArray("edges");
                        for (JsonElement ee : edges) {
                            JsonObject eo = ee.getAsJsonObject();
                            edgeObjs.add(eo);
                            String f = eo.get("from").getAsString();
                            String t = eo.get("to").getAsString();
                            if (!index.containsKey(f)) index.put(f, index.size());
                            if (!index.containsKey(t)) index.put(t, index.size());
                        }
                    }

                    Graph G = new Graph(index.size());
                    for (JsonObject eo : edgeObjs) {
                        String f = eo.get("from").getAsString();
                        String t = eo.get("to").getAsString();
                        double w = eo.get("weight").getAsDouble();
                        int u = index.get(f);
                        int v = index.get(t);
                        G.addEdge(new Edge(u, v, w));
                    }
                    parsed.add(new ParsedGraph(G, gid));
                }

            } else if (root.isJsonArray()) {
                Type listType = new TypeToken<List<JsonGraphCompact>>(){}.getType();
                List<JsonGraphCompact> compact = gson.fromJson(root, listType);
                for (int i = 0; i < compact.size(); i++) {
                    JsonGraphCompact cg = compact.get(i);
                    Graph G = new Graph(Math.max(0, cg.vertices));
                    if (cg.edges != null) {
                        for (List<Object> e : cg.edges) {
                            int u = ((Number)e.get(0)).intValue();
                            int v = ((Number)e.get(1)).intValue();
                            double w = ((Number)e.get(2)).doubleValue();
                            G.addEdge(new Edge(u, v, w));
                        }
                    }
                    parsed.add(new ParsedGraph(G, String.valueOf(i+1)));
                }

            } else if (root.isJsonObject()) {
                JsonGraphCompact single = gson.fromJson(root, JsonGraphCompact.class);
                Graph G = new Graph(Math.max(0, single.vertices));
                if (single.edges != null) {
                    for (List<Object> e : single.edges) {
                        int u = ((Number)e.get(0)).intValue();
                        int v = ((Number)e.get(1)).intValue();
                        double w = ((Number)e.get(2)).doubleValue();
                        G.addEdge(new Edge(u, v, w));
                    }
                }
                parsed.add(new ParsedGraph(G, "1"));
            } else {
                System.err.println("Unsupported JSON root element. Expect object or array.");
                return;
            }
        }

        if (parsed.isEmpty()) {
            System.err.println("No graphs found in input file.");
            return;
        }

        List<MSTResult> allResults = new ArrayList<>();
        OutputWriter writer = new OutputWriter();

        for (int gi = 0; gi < parsed.size(); gi++) {
            ParsedGraph pg = parsed.get(gi);
            Graph G = pg.graph;
            String gid = pg.id;

            KruskalMST km = new KruskalMST(G);
            long t0 = System.nanoTime();
            km.run();
            long t1 = System.nanoTime();
            MSTResult kr = new MSTResult();
            kr.graphId = gid;
            kr.algorithm = "Kruskal";
            kr.edges = edgesToStrings(km.edges());
            kr.totalCost = km.weight();
            kr.originalVertices = G.V();
            kr.originalEdges = G.E();
            kr.operations = km.operations;
            kr.timeMs = (t1 - t0)/1_000_000;
            allResults.add(kr);

            PrimMST pm = new PrimMST(G);
            t0 = System.nanoTime();
            pm.run();
            t1 = System.nanoTime();
            MSTResult pr = new MSTResult();
            pr.graphId = gid;
            pr.algorithm = "Prim";
            pr.edges = edgesToStrings(pm.edges());
            pr.totalCost = pm.weight();
            pr.originalVertices = G.V();
            pr.originalEdges = G.E();
            pr.operations = pm.operations;
            pr.timeMs = (t1 - t0)/1_000_000;
            allResults.add(pr);

            System.out.printf("Graph %s: V=%d E=%d%n", gid, G.V(), G.E());
            System.out.printf("  Kruskal cost=%.5f time=%dms ops=%d edges=%d%n", kr.totalCost, kr.timeMs, kr.operations, kr.edges.size());
            System.out.printf("  Prim    cost=%.5f time=%dms ops=%d edges=%d%n", pr.totalCost, pr.timeMs, pr.operations, pr.edges.size());
        }

        writer.writeJson(allResults, out);
        writer.writeCsv(allResults, csv);
        System.out.println("Wrote results to " + out + " and " + csv);
    }

    private static List<String> edgesToStrings(Iterable<Edge> edges) {
        List<String> res = new ArrayList<>();
        for (Edge e : edges) res.add(e.toString());
        return res;
    }
}