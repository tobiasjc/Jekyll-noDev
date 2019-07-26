/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author José Tobias
 */
public class ReadFile {

    private final String filePath;
    private double d;
    private double delta;

    public ReadFile(String path) {
        this.filePath = path;
    }

    public double getD() {
        return d;
    }

    public double getDelta() {
        return delta;
    }

    public ArrayList<Integer>[] readLinks() {
        ArrayList<Integer>[] links = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            links = new ArrayList[Integer.parseInt(br.readLine())];
            for (int i = 0; i < links.length; i++) {
                links[i] = new ArrayList<>();
            }
            d = Double.parseDouble(br.readLine());
            delta = Double.parseDouble(br.readLine());
            while ((line = br.readLine()) != null) {
                String[] token = line.split("=>");
                int from = Integer.parseInt(token[0].trim());
                int to = Integer.parseInt(token[1].trim());
                if (!links[from].contains(to)) {
                    links[from].add(to);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return links;
    }

}
