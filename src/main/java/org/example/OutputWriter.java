package org.example;

import com.google.gson.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class OutputWriter {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void writeJson(List<MSTResult> results, Path out) throws IOException {
        try (Writer w = Files.newBufferedWriter(out)) {
            gson.toJson(results, w);
        }
    }


    public void writeCsv(List<MSTResult> results, Path out) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(out)) {
            bw.write("graphId,algorithm,vertices,execTimeMs,operationCount,totalCost\n");
            for (MSTResult r : results) {
                String gid = r.graphId == null ? "" : r.graphId;
                bw.write(String.format(Locale.ROOT, "%s,%s,%d,%d,%d,%.5f\n",
                        gid,
                        r.algorithm,
                        r.originalVertices,
                        r.timeMs,
                        r.operations,
                        r.totalCost));
            }
        }
    }
}