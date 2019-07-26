package bcc.bri;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ForkJoinPool;

/**
 * Hello world!
 */
public class App {


    public static void main(String[] args) {
        String connectionString = ConnectionString.getConnectionString();

        try (Connection conn = DriverManager.getConnection(connectionString);
             Statement stmt = conn.createStatement();) {
            System.out.println("Doing...");

            ResultSet rs = stmt.executeQuery("select min(page_id), max(page_id) from page;");
            rs.next();
            int start = rs.getInt("min(page_id)");
            int end = rs.getInt("max(page_id)");

            int interval = 49999;

            InsertThread insertThread = new InsertThread(start, end, interval);
            ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke(insertThread);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Done.");
    }
}