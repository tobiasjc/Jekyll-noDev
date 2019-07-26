package implementation;

import java.util.List;

public abstract class AbstractGraph {

    Representation representacao = null;

    public abstract void create(int numeroDevertices);

    public abstract void addEdge(int u, int v, double w);

    public abstract List<Edge> listAdj(int u);

    public abstract List<Edge> edges();

    public abstract int V();

    public abstract Representation rep();
}
