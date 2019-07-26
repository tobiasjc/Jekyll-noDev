/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author jose
 */
public final class MST {

    private Queue<Edge> MST;
    private LowPriorityQueue<Edge> PQ;
    private static int u;
    private static int v;

    public MST(AbstractGraph G, HashMap<String, String> hash) {
        switch (hash.get("-algorithm")) {
            case "kruskal":
                kruskal(G);
                break;
            case "prim":
                prim(G);
                break;
            default:
                throw new Error("Algorithm specified not found");
        }
        this.write(hash.get("-output"), "/");
    }

    public void kruskal(AbstractGraph G) {
        MST = new LinkedList<>();
        PQ = new LowPriorityQueue<>(G.edges());
        UnionFind uf = new UnionFind(G.V());

        while (!PQ.empty() && MST.size() <= G.V() - 1) {
            Edge e = PQ.remove();
            u = e.one();
            v = e.other(u);
            if (!uf.connected(u, v)) {
                uf.union(u, v);
                MST.add(e);
            }
        }
        weightToString();
    }

    public void prim(AbstractGraph G) {
        MST = new LinkedList<>();
        PQ = new LowPriorityQueue<>(G.edges().size());
        boolean[] marked = new boolean[G.V()];

        visit(G, 0, marked, PQ);
        while (!PQ.empty()) {
            Edge e = PQ.remove();
            u = e.one();
            v = e.other(u);
            if (!(marked[u] && marked[v])) {
                MST.add(e);
                if (!marked[u]) {
                    visit(G, u, marked, PQ);
                } else {
                    visit(G, v, marked, PQ);
                }
            }
        }
        weightToString();
    }

    private void visit(AbstractGraph G, int u, boolean[] marked, LowPriorityQueue<Edge> PQ) {
        marked[u] = true;
        for (Edge e : G.listAdj(u)) {
            if (!marked[e.other(u)]) {
                PQ.insert(e);
            }
        }
    }

    public void weightToString() {
        double sum = 0;
        for (Edge e : MST) {
            sum += e.weight();
        }
        System.out.println(sum);
    }

    public void write(String op, String sp) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        File file = new File(op);
        int u;
        int v;
        try {
            fw = new FileWriter(file, false);
            bw = new BufferedWriter(fw);
            fw.write("vertice,adjacente\n");
            for (Edge e : MST) {
                u = e.one();
                v = e.other(u);
                bw.write(u + "," + v + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Queue<Edge> mst() {
        return MST;
    }
}
