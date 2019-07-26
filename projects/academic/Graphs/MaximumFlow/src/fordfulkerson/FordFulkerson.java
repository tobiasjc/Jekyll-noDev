/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fordfulkerson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import maximumflow.AbstractFlowNetwork;
import maximumflow.FlowEdge;

/**
 *
 * @author jose
 */
public class FordFulkerson {

    private final AbstractFlowNetwork G;
    private final int s;
    private final int t;

    private double maximumFlow; // maximum network flow
    private final FlowEdge[] edgeTo; // saving augmenting path
    private final boolean[] seen; // divide S and T groups
    private final Queue<Integer> q; // queueing edges crossing cut

    // output settings
    private final String op;
    private final char sp;

    private final String verbose;

    public FordFulkerson(AbstractFlowNetwork G, int s, int t, String outputPath, String verbose) {
        // class variables start
        this.G = G;
        this.s = s;
        this.t = t;

        // initial max flow
        G.edges().forEach((FlowEdge e) -> {
            this.maximumFlow += e.getFlow();
        });

        // class structures initial settings
        this.seen = new boolean[G.V()];
        this.q = new LinkedList<>();
        edgeTo = new FlowEdge[G.V()];

        // path to write results
        this.op = outputPath;
        this.sp = '/';

        // verbosity level
        this.verbose = verbose;
    }

    public void run() {
        // verbose messages
        if ("true".equals(verbose)) {
            verboseOutput(0);
        } else {
            System.out.println("Executing Ford-Fulkerson method...");
        }

        // main loop
        while (checkAugmentingPath()) {
            double bottleneck = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                // get the minimum residual capacity of all edges on found path
                bottleneck = Math.min(bottleneck, edgeTo[v].getResidualCapacity(v));
            }
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                // add the minimum residual capacity to al edges on the last found path
                edgeTo[v].addFlowTo(bottleneck, v);
            }

            // increment maximum flow by the amount of min{residual capacity of all path, bottleneck}
            maximumFlow += bottleneck;

            // verbose messages
            if ("true".equals(verbose)) {
                verboseOutput(1);
            }
        }
        System.out.println("Maximum Network Flow = " + maximumFlow);

        // write outputs
        this.writeOutputs();
    }

    private boolean checkAugmentingPath() {
        // structures setting for iteration N
        Arrays.fill(this.seen, false);
        this.seen[s] = true;
        this.q.offer(s);

        // main loop
        while (!q.isEmpty()) {
            int u = q.remove();
            G.adj(u).forEach((FlowEdge e) -> {
                int v = e.other(u);
                // execute the check of edges with cut S and T
                // S are the marked ones, T are the unmarked ones
                if (e.getResidualCapacity(v) > 0 && !seen[v]) {
                    edgeTo[v] = e;
                    seen[v] = true;
                    q.offer(v);
                }
            });
        }
        return seen[t];
    }

    public void writeOutputs() {
        FileWriter fw = null;
        BufferedWriter bw = null;
        File file1 = new File(this.op);

        // file 1 write
        try {
            fw = new FileWriter(file1, false);
            bw = new BufferedWriter(fw);
            fw.write("origem,destino,fluxo\n");
            for (FlowEdge e : edgeTo) {
                if (e != null) {
                    int u = e.one();
                    int v = e.other(u);
                    double f = e.getFlow();
                    fw.write(u + "," + v + "," + f + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        // separate second string for flow file
        String[] separated = this.op.split("/");
        String newFileName = "maximumFlow".concat(separated[separated.length - 1]);
        separated[separated.length - 1] = newFileName;
        String flowPath = new String();

        // concatenating string back to a readable format
        for (String str : separated) {
            flowPath += str;
            if (!str.equals(newFileName)) {
                flowPath += this.sp;
            }
        }

        // file2 creation on desired path
        File file2 = new File(flowPath);
        try {
            fw = new FileWriter(file2, false);
            bw = new BufferedWriter(fw);
            fw.write("Maximum Flow\n" + Double.toString(maximumFlow) + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public FlowEdge[] getPath() {
        return this.edgeTo;
    }

    public double maximumFlow() {
        return this.maximumFlow;
    }

    // simplt verbose output function
    private void verboseOutput(int status) {

        if (status == 0) {
            System.out.println("Vertices number = " + G.V());
            System.out.println("Edges number = " + G.E());
            System.out.println("Source = " + this.s);
            System.out.println("Sink = " + this.t);
            System.out.println("Executing Ford-Fulkerson method on...");
            System.out.println("--- * --- * --- Initial Flow Network Status --- * --- * ---");
            G.edges().forEach((FlowEdge e) -> {
                System.out.println(e.toString());
            });
            System.out.println("--- * --- * --- Initial Residual Network Status --- * --- * ---");
            G.edges().forEach((FlowEdge e) -> {
                System.out.println(e.toStringResidual());
            });
        }
        System.out.println("===============================================================");
        System.out.println("--- * --- * --- * --- Flow Network Status --- * --- * --- * ---");
        G.edges().forEach((FlowEdge e) -> {
            System.out.println(e.toString());
        });
        System.out.println("--- * --- * --- * --- Residual Network Status --- * --- * --- * ---");
        G.edges().forEach((FlowEdge e) -> {
            System.out.println(e.toStringResidual());
        });
        System.out.println("--- * --- * --- * --- Augmenting Path --- * --- * --- * ---");
        for (FlowEdge e : edgeTo) {
            if (e != null) {
                System.out.println(e.one() + "->" + e.other(e.one()) + " " + e.getFlow());
            }
        }
        System.out.println("Maximum flow found for augmenting path = " + maximumFlow);
        System.out.println("===============================================================");
    }
}
