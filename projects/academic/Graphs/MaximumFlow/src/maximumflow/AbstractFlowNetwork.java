/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maximumflow;

import java.util.List;

/**
 *
 * @author jose
 */
public abstract class AbstractFlowNetwork {

    public abstract void insertEdge(int u, int v, double c);

    public abstract List<FlowEdge> edges();

    public abstract int E();

    public abstract int V();

    public abstract List<FlowEdge> adj(int u);
}
