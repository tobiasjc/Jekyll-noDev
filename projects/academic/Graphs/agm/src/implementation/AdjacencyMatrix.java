package implementation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class AdjacencyMatrix extends AbstractGraph {

    private double[][] adj;

    public AdjacencyMatrix(int n) {
        super.representacao = Representation.MATRIZ_ADJACENCIA;
        create(n);
    }

    @Override
    public void create(int n) {
        adj = new double[n][n];
        for (double[] o : adj) {
            Arrays.fill(o, Double.POSITIVE_INFINITY);
        }
    }

    @Override
    public int V() {
        return adj.length;
    }

    @Override
    public void addEdge(int u, int v, double w) {
        adj[u][v] = w;
    }

    @Override
    public List<Edge> listAdj(int u) {
        List<Edge> ret = new LinkedList<>();
        for (int v = 0; v < adj.length; v++) {
            if (adj[u][v] != Double.POSITIVE_INFINITY) {
                ret.add(new Edge(v, u, adj[u][v]));
            }
        }
        return ret;
    }

    @Override
    public List<Edge> edges() {
        List<Edge> ret = new LinkedList<>();
        for (int u = 0; u < adj.length; u++) {
            for (int v = adj.length - 1; v > u; v--) {
                if (adj[u][v] != Double.POSITIVE_INFINITY) {
                    ret.add(new Edge(u, v, adj[u][v]));
                }
            }
        }
        return ret;
    }

    @Override
    public Representation rep() {
        return this.representacao;
    }

}
