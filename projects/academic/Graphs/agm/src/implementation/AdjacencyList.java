package implementation;

import java.util.LinkedList;
import java.util.List;

public final class AdjacencyList extends AbstractGraph {

    private static List<Edge>[] adj;

    public AdjacencyList(int n) {
        super.representacao = Representation.LISTA_ADJACENCIA;
        create(n);
    }

    @Override
    public void create(int n) {
        adj = new LinkedList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    @Override
    public int V() {
        return adj.length;
    }

    @Override
    public void addEdge(int u, int v, double w) {
        adj[u].add(new Edge(u, v, w));
    }

    @Override
    public List<Edge> listAdj(int u) {
        return adj[u];
    }

    @Override
    public List<Edge> edges() {
        List<Edge> ret = new LinkedList<>();
        for (int n = 0; n < adj.length; n++) {
            for (Edge e : adj[n]) {
                ret.add(e);
            }
        }
        return ret;
    }

    @Override
    public Representation rep() {
        return this.representacao;
    }
}
