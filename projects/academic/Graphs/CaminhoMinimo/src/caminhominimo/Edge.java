/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caminhominimo;

/**
 *
 * @author jose
 */
public class Edge implements Comparable<Edge> {

    private final int u;
    private final int v;
    private final double weight;

    Edge(int u, int v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    public double weight() {
        return this.weight;
    }

    public int one() {
        return u;
    }

    public int other(int u) {
        return (u == this.u) ? this.v : this.u;
    }

    @Override
    public int compareTo(Edge that) {
        if (this.weight > that.weight) {
            return 1;
        } else if (this.weight < that.weight) {
            return -1;
        }
        return 0;
    }
}
