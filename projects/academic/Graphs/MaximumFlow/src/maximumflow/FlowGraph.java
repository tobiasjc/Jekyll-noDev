/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maximumflow;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author jose
 */
public class FlowGraph extends AbstractFlowNetwork {

    private List<FlowEdge>[] adj;
    private final int V;
    private int E;

    public FlowGraph(int size) {
        this.V = size;
        this.E = 0;
        initialize(size);
    }

    private void initialize(int s) {
        this.adj = new LinkedList[s];
        for (int i = 0; i < adj.length; i++) {
            this.adj[i] = new LinkedList<>();
        }
    }

    /**
     *
     * @param u origin vertex
     * @param v destination vertex
     * @param c capacity value of the edge
     */
    @Override
    public void insertEdge(int u, int v, double c) {
        adj[u].add(new FlowEdge(u, v, c));
        E += 1;
    }

    /**
     *
     * @return Number of edges
     */
    @Override
    public int E() {
        return E;
    }

    /**
     *
     * @return Number of vertices
     */
    @Override
    public int V() {
        return this.V;
    }

    /**
     *
     * @param u number of the vertex to get all adjacent vertices edges
     * @return all edges that connect the u vertex to his adjacent vertices
     */
    @Override
    public List<FlowEdge> adj(int u) {
        List<FlowEdge> adjS = new LinkedList<>();
        adj[u].forEach((FlowEdge e) -> adjS.add(e));
        return adjS;
    }

    /**
     *
     * @return All edges on the flow network
     */
    @Override
    public List<FlowEdge> edges() {
        List<FlowEdge> all = new LinkedList<>();

        for (List<FlowEdge> l : adj) {
            l.forEach((FlowEdge e) -> all.add(e));
        }
        return all;
    }
}
