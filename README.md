# Design and Analysis of Algorithms - Assignment 3

This project is for my DAA class lab work.  
Task was to implement MST algorithms (Minimum Spanning Tree) using **Kruskal** and **Prim** algorithms.  
Also we need to compare their performace on diffrent graph sizes, hope it make sense lol.

## About project

So basically program read json file with some graphs (nodes and edges with weights).  
Then it runs both Kruskal and Prim on every graph and output results to json and csv files.  
In csv file i collect total cost, execution time and number of operations for each algo.  
Later I use it to make comparsion table, kinda cool huh?  
Didn’t use any fancy libs except gson for json parsing and junit for tests, keep it simple.

## Folder structure
```
src/
├─ main/
│   ├─ java/org/example/
│   │   ├─ Graph.java
│   │   ├─ Edge.java
│   │   ├─ KruskalMST.java
│   │   ├─ PrimMST.java
│   │   ├─ MSTResult.java
│   │   ├─ OutputWriter.java
│   │   └─ Main.java
│   └─ resources/
│       └─ input/
│           ├─ input_small.json
│           ├─ input_medium.json
│           ├─ input_large.json
│           └─ input_extra_large.json
└─ test/java/org/example/
└─ MSTAlgorithmsTest.java
```
## How to run

After build with maven, you can run like this: java -jar target/assignment3-1.0-SNAPSHOT.jar src/main/resources/input/input_small.json output.json summary.csv  
It will print something like: 

Graph 1: V=10 E=30  
Kruskal cost=1803.53 time=1ms ops=102 edges=11  
Prim    cost=1803.53 time=0ms ops=67 edges=11  
And after run, files `output.json` and `summary.csv` will be created in project folder.  
Not sure if perfect but hope it pass teacher pls be nice :D

## Results

I tested it on 5 small, 10 medium, 10 large and 3 extra large graphs.  
Mostly Prim works faster on big graphs, but sometimes Kruskal is faster on small ones.  
Not big diffrence, but depend on graph density, lol.
