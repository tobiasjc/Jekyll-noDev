/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maximumflow;

import fordfulkerson.FordFulkerson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import maximumflow.graphics.MaximumFlowGUI;

/**
 *
 * @author jose
 */
public class MaximumFlow {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        HashMap<String, String> hash = readHash(args);
        AbstractFlowNetwork G = readGraph(hash.get("-entrada"));

        FordFulkerson ff = new FordFulkerson(
                G,
                Integer.parseInt(hash.get("-origem")),
                Integer.parseInt(hash.get("-destino")),
                hash.get("-saida"),
                hash.get("-verbose")
        );

        // run Ford-Fulkerson method
        ff.run();

        if ("true".equals(hash.get("-draw"))) {
            MaximumFlowGUI gui = new MaximumFlowGUI(ff.getPath(),
                    G, hash.get("-origem"),
                    hash.get("-destino"),
                    ff.maximumFlow()
            );
            gui.setVisible(true);
        }
    }

    /**
     *
     * @param inputPath the input path of the graph in the given format
     * @return a abstract flow network
     */
    private static AbstractFlowNetwork readGraph(String inputPath) {
        AbstractFlowNetwork G;
        int quantity = 0;
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                int u = Integer.parseInt(line.split(",")[0]);
                int v = Integer.parseInt(line.split(",")[1]);
                if (line.length() > 0) {
                    if (u > v) {
                        if (u > quantity) {
                            quantity = u;
                        }
                    } else {
                        if (v > quantity) {
                            quantity = v;
                        }
                    }
                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        G = new FlowGraph(quantity + 1);

        try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                int u = Integer.parseInt(line.split(",")[0]);
                int v = Integer.parseInt(line.split(",")[1]);
                double c = Double.parseDouble(line.split(",")[2]);
                G.insertEdge(u, v, c);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return G;
    }

    /**
     *
     * @param s a string array which is equal the arguments received by main
     * method
     * @return a HashMap containing keys and values passed by command line
     */
    private static HashMap<String, String> readHash(String[] s) {
        HashMap<String, String> temp = new HashMap<>();

        if (s.length % 2 == 0) {
            for (int i = 0; i < s.length; i += 2) {
                temp.put(s[i], s[i + 1]);
            }
            return temp;
        }
        throw new Error("Wrong number of parameters.");
    }

}
