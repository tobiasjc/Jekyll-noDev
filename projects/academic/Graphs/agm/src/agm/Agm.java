/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agm;

import implementation.AbstractGraph;
import implementation.AdjacencyList;
import implementation.AdjacencyMatrix;
import implementation.GraphGUI;
import implementation.MST;
import implementation.Read_CSV;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 *
 * @author jose
 */
public class Agm {

    /**
     * @param args
     */
    //java -jar agm.jar -input grafo.csv -output arvoreGeradora.csv -representacao 1 -algorithm prim -draw true
    public static void main(String[] args) {
        HashMap<String, String> hash;
        hash = read_parms(args);

        double[][] coord = Read_CSV.read_input(hash);
        AbstractGraph G = read_graph(coord, hash);

        MST mst = new MST(G, hash);

        if ("true".equals(hash.get("-draw"))) {
            GraphGUI gf = new GraphGUI(coord, G.edges(), mst.mst(), hash.get("-output"));
            gf.setVisible(true);
        }
    }

    public static AbstractGraph read_graph(double[][] coord, HashMap<String, String> hash) {
        AbstractGraph G = null;
        switch (hash.get("-representation")) {
            case "1":
                G = new AdjacencyMatrix(coord.length);
                break;
            case "2":
                G = new AdjacencyList(coord.length);
                break;
            default:
                throw new Error("Algorithm specified not found.");
        }

        double dist;
        for (int i = 0; i < coord.length; i++) {
            for (int k = i + 1; k < coord.length; k++) {
                dist = Math.sqrt(Math.pow((coord[i][0] - coord[k][0]), 2) + Math.pow((coord[i][1] - coord[k][1]), 2));
                G.addEdge(i, k, dist);
                G.addEdge(k, i, dist);
            }
        }

        return G;
    }

    public static HashMap<String, String> read_parms(String[] parms) {
        HashMap<String, String> hash = new HashMap<>();
        switch (parms.length) {
            case 0:
                throw new Error("Parameters not found");
            case 1:
                switch (parms[0]) {
                    case "-help":
                        String line;
                        try (BufferedReader br = new BufferedReader(new FileReader("../documentation/help.hlp"))) {
                            while ((line = br.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            default:
                if (parms.length % 2 == 0) {
                    for (int i = 0; i < parms.length; i += 2) {
                        hash.put(parms[i], parms[i + 1]);
                    }
                } else {
                    throw new Error("Wrong number of parameters have been passed");
                }
        }
        return hash;
    }
}
