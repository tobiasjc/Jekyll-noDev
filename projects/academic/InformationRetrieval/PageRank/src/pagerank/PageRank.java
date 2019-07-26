/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author José Tobias
 */
public class PageRank {

    /**
     * @param args the command line arguments
     *
     * Passar agumento "--filePath"
     */
    public static void main(String[] args) {
        // TODO code application logic here
        HashMap<String, String> argsHash = getArgsHash(args);
        ReadFile rf = new ReadFile(argsHash.get("--filePath")); // Passar agumento "--filePath"
        ArrayList<Integer>[] links = rf.readLinks();
        double delta = rf.getDelta();
        double d = rf.getD();
        int[] count = getCount(links);
        ArrayList<Integer>[] appointments = getAppointments(links);
        double[] ranks = rank(appointments, count, d, delta);
        getReport(appointments, ranks);
    }

    private static double[] rank(ArrayList<Integer>[] appointments, int[] count, double d, double delta) {
        double[] ranks = new double[appointments.length];
        double[] temp = new double[appointments.length];
        Arrays.fill(ranks, 1);
        boolean done;
        do {
            done = true;
            for (int i = 0; i < appointments.length; i++) {
                double sum = 0;
                if (appointments[i].isEmpty()) {
                    sum = 1;
                } else {
                    for (int l : appointments[i]) {
                        if (count[l] != 0) {
                            sum += ranks[l] / count[l];
                        }else{
                            sum += ranks[l] / (count[l] + 1);
                        }
                    }
                }
                temp[i] = (1 - d) + d * sum;
            }

            for (int j = 0; j < temp.length; j++) {
                if (Math.abs(ranks[j] - temp[j]) >= delta) {
                    done = false;
                }
                ranks[j] = temp[j];
            }
        } while (!done);
        return ranks;
    }

    public static ArrayList<Integer>[] getAppointments(ArrayList<Integer>[] links) {
        ArrayList<Integer>[] appointments = new ArrayList[links.length];
        for (int l = 0; l < appointments.length; l++) {
            appointments[l] = new ArrayList<>();
        }
        for (int i = 0; i < links.length; i++) {
            for (int j = 0; j < links.length; j++) {
                if (links[j].contains(i)) {
                    appointments[i].add(j);
                }
            }
        }
        return appointments;
    }

    private static int[] getCount(ArrayList<Integer>[] links) {
        int[] count = new int[links.length];
        for (int i = 0; i < links.length; i++) {
            for (ArrayList<Integer> link : links) {
                if (link.contains(i)) {
                    count[i]++;
                }
            }
        }
        System.out.println(Arrays.toString(count));
        return count;
    }

    public static HashMap<String, String> getArgsHash(String[] args) {
        if (args.length % 2 != 0) {
            System.err.println("Errou os argumetos aí amigão, o tamanho deles não é par.");
            System.exit(1);
        }
        HashMap<String, String> hash = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            hash.put(args[i], args[i + 1]);
        }
        return hash;
    }

    private static List<Entry<Integer, Double>> getRankOrder(double[] ranks) {
        HashMap<Integer, Double> rankMap = new HashMap<>();
        for (int i = 0; i < ranks.length; i++) {
            rankMap.put(i, ranks[i]);
        }
        List<Entry<Integer, Double>> rankOrder = new LinkedList<>(rankMap.entrySet());
        Collections.sort(rankOrder, (Entry<Integer, Double> o1, Entry<Integer, Double> o2) -> {
            return o2.getValue().compareTo(o1.getValue());
        });
        return rankOrder;
    }

    private static void getReport(ArrayList<Integer>[] appointments, double[] ranks) {
        List<Entry<Integer, Double>> order = getRankOrder(ranks);
        System.out.println("Solution report for " + appointments.length + " links ->");
        for (int i = 0; i < appointments.length; i++) {
            System.out.println(i + " <= " + appointments[i]);
        }
        System.out.println("\nRANK->");
        double sum = 0;
        for (Entry<Integer, Double> entry : order) {
            int key = entry.getKey();
            double value = entry.getValue();
            System.out.println(key + " C[" + appointments[key].size() + "] => " + value);
            sum += value;
        }
        System.out.println("sum = " + sum);
    }
}
