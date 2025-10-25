package org.example;


public class Edge implements Comparable<Edge> {
    private final int u;
    private final int v;
    private final double weight;

    public Edge(int u, int v, double weight) {
        if (u < 0 || v < 0) throw new IllegalArgumentException("Vertex must be >= 0");
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    public int either() { return u; }
    public int other(int x) {
        if (x == u) return v;
        if (x == v) return u;
        throw new IllegalArgumentException("Vertex not incident to edge");
    }
    public double weight() { return weight; }

    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.weight, that.weight);
    }

    @Override
    public String toString() {
        return String.format("%d-%d %.5f", u, v, weight);
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof Edge)) return false;
        Edge e = (Edge)o;
        return ((e.u==u && e.v==v) || (e.u==v && e.v==u)) && Double.compare(e.weight, weight)==0;
    }

    @Override
    public int hashCode(){
        int a = Math.min(u,v), b = Math.max(u,v);
        long bits = Double.doubleToLongBits(weight);
        return 31*(31*a + b) + (int)(bits ^ (bits>>>32));
    }
}