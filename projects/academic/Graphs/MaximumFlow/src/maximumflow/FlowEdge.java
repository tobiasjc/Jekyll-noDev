/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maximumflow;

/**
 *
 * @author jose
 */
public class FlowEdge {

    private final int u; // origin
    private final int v; // destiny
    private final double c; // capacity
    private double f; // flow

    public FlowEdge(int u, int v, double c) {
        this.u = u;
        this.v = v;
        this.c = c;
        this.f = 0; // initial network flow (|f|) is 0
    }

    // if the flow is towards the vertex direction, it is equals flow
    // if the flow is not toward the vertex direction, it is (capacity - (flow towards direction))
    public double getResidualCapacity(int k) {
        return (k == this.u) ? this.f : (this.c - this.f);
    }

    public double getCapacity() {
        return this.c;
    }

    // if the quantity is towards the vertex direction, it is added
    // if it is against the direction, it is subtracted (residual network)
    public void addFlowTo(double delta, int k) {
        this.f += (k == this.u) ? -delta : delta;
    }

    // gets the other vertex of the one passed as an argument
    public int other(int u) {
        return (u == this.u) ? this.v : this.u;
    }

    // gets origin vertex
    public int one() {
        return u;
    }

    public double getFlow() {
        return f;
    }

    // used to get the flow network to string
    @Override
    public String toString() {
        return this.u + "->" + this.v + " " + this.f + "/" + this.c;
    }

    // used on residual network to string
    public String toStringResidual() {
        return "{" + this.u + "," + this.v + "} " + (this.c - this.f) + "/" + this.f;
    }
}
