/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caminhominimo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Read_CSV {

    private static int u = 0;
    private static String line;
    private static int counter;
    private static double x1;
    private static double y1;

    public static double[][] read_input(HashMap<String, String> hash) {
        counter = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(hash.get("--entrada")))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    counter++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[][] coord = new double[counter][2];

        try (BufferedReader br1 = new BufferedReader(new FileReader(hash.get("--entrada")))) {
            br1.readLine();
            while ((line = br1.readLine()) != null) {
                if (line.length() > 0) {
                    x1 = Double.parseDouble(line.split(",")[0]);
                    y1 = Double.parseDouble(line.split(",")[1]);
                    coord[u][0] = x1;
                    coord[u][1] = y1;
                    u++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coord;
    }

}
