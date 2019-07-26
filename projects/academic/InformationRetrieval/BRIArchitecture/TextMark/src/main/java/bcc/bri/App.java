package bcc.bri;

import bcc.bri.ConnectionString;

import java.sql.*;
import java.util.concurrent.ForkJoinPool;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        String connectionString = ConnectionString.getConnectionString();

        try (Connection conn = DriverManager.getConnection(connectionString);
             Statement stmt = conn.createStatement();) {

            ResultSet rs = stmt.executeQuery("select min(old_id), max(old_id) from text;");
            rs.next();
            int max = rs.getInt("max(old_id)");
            int min = rs.getInt("min(old_id)");

            int interval = 49999;

            MarkThread markThread = new MarkThread(min, max, interval);
            ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke(markThread);
        } catch (SQLException e) {
            System.err.printf(e.getMessage());
            e.printStackTrace();
        }
    }
}