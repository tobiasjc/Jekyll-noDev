/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caminhominimo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author jose
 */
public final class CM {

    private Queue<Edge> cm = new LinkedList<>();
    private String[] pi;
    private double[] d;
    private AbstractGraph G;

    public CM(AbstractGraph G, HashMap<String, String> hash) {
        this.G = G;

        switch (hash.get("--algoritmo")) {
            case "1":
                bellmanFord(Integer.parseInt(hash.get("--inicial")));
                break;
            case "2":
                dijkstra(Integer.parseInt(hash.get("--inicial")));
                break;
            default:
                throw new Error("Algorithm specified not found");
        }
        save_cm();
        this.write(hash.get("--saida"), "/");
    }

    /**
     *
     * @param s source vertex.
     */
    private void bellmanFord(int s) {
        initialize_single_source(s);
        int u;
        int v;

        for (int i = 1; i <= G.V() - 1; i++) {
            for (Edge e : G.edges()) {
                u = e.one();
                v = e.other(u);
                relax(u, v, e.weight());
            }
        }
//        non_negative_cycles();
    }

    /**
     *
     * @param s source vertex.
     */
    private void dijkstra(int s) {
        initialize_single_source(s);
        Stack<Integer> S = new Stack<>();
        DistancePriorityQueue Q = new DistancePriorityQueue(d);
        int u;
        int v;

        while (!Q.empty()) {
            u = Q.remove();
            S.push(u);
            for (Edge e : G.listAdj(u)) {
                v = e.other(u);
                relax(u, v, e.weight());
            }
        }
    }

    /**
     *
     * @param u origin vertex.
     * @param v destiny vertex.
     * @param w the weight of the vertex (u,v).
     */
    private void relax(int u, int v, double w) {
        if (d[v] > (d[u] + w)) {
            d[v] = d[u] + w;
            pi[v] = String.valueOf(u);
        }
    }

    /**
     *
     * @param s source vertex
     */
    private void initialize_single_source(int s) {
        d = new double[G.V()];
        pi = new String[d.length];
        Arrays.fill(d, Double.POSITIVE_INFINITY);
        Arrays.fill(pi, "null");
        d[s] = 0;
    }

    /**
     * Function to save the minimum path edges.
     */
    public void save_cm() {
        for (int i = 0; i < pi.length; i++) {
            if (!"null".equals(pi[i])) {
                cm.add(new Edge(Integer.parseInt(pi[i]), i, 0));
            }
        }
    }

    public Queue<Edge> get_cm() {
        return this.cm;
    }

    public void write(String op, String sp) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        File file = new File(op);
        try {
            fw = new FileWriter(file, false);
            bw = new BufferedWriter(fw);
            fw.write("vértice,pai\n");
            for (int i = 0; i < pi.length; i++) {
                bw.write(i + "," + pi[i] + "\n");
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

    /**
     * Function to test it the graph contain negative cycles.
     *
     * @return false if it contains negative cycles, true otherwise.
     */
    public boolean non_negative_cycles() {
        for (Edge e : G.edges()) {
            int u = e.one();
            int v = e.other(u);
            if (d[v] > d[u] + e.weight()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Function to perform the shortest path operation on oriented acyclic
     * graphs.
     *
     * @param s source vertex.
     */
    private void GAO(int s) {
        initialize_single_source(s);
        Stack<Integer> T = Topologic.DFS(G);
        for (Integer u : T) {
            for (Edge e : G.listAdj(u)) {
                int v = e.other(u);
                relax(u, v, e.weight());
            }
        }
    }
}
