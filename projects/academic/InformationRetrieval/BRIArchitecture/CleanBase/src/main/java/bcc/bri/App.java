package bcc.bri;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    public static void main(String[] args) {
        String connectionString = ConnectionString.getConnectionString();
        try (Connection conn = DriverManager.getConnection(connectionString);) {
            Statement stmt = conn.createStatement();

            System.out.println("Deleting...");
            stmt.executeUpdate("delete p.*, r.*, t.* from page p, revision r, text t where p.page_latest=r.rev_id and r.rev_text_id=t.old_id and p.page_namespace <> 0;");
            stmt.executeUpdate("delete p.*, r.*, t.* from page p, revision r, text t where p.page_latest=r.rev_id and r.rev_text_id=t.old_id and t.old_math = 0;");
            System.out.println("Done.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}