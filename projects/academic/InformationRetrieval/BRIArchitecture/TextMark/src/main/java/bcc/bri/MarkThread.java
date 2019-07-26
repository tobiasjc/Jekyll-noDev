package bcc.bri;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.concurrent.RecursiveAction;

public class MarkThread extends RecursiveAction {
    private static final long serialVersionUID = 1L;
    private static int DONE = 0;
    /**
     *
     */
    private final int limit;
    private final int start;
    private final int end;

    MarkThread(int start, int end, int limit) {
        this.start = start;
        this.end = end;
        this.limit = limit;
    }

    private void calculate() {
        ResultSet rs;

        String pst = "update text set old_math=1 where old_id = ?";

        try (Connection conn1 = DriverManager.getConnection(ConnectionString.getConnectionString());
             Connection conn2 = DriverManager.getConnection(ConnectionString.getConnectionString());
             Statement stmt1 = conn2.createStatement();
             PreparedStatement ps = conn1.prepareStatement(pst)) {

            rs = stmt1.executeQuery("select count(*) from page;");
            rs.next();
            int N = rs.getInt(1);

            rs = stmt1.executeQuery("select old_id, old_text from text where old_id between " + this.start + " and " + this.end + ";");

            while (rs.next()) {
                String old_text = new String(rs.getBytes("old_text"), StandardCharsets.UTF_8);
                if (old_text.contains("<math>")) {
                    int old_id = rs.getInt(1);
                    ps.setInt(1, old_id);
                    ps.executeUpdate();
                }
            }
            DONE += 1;
            System.out.println("Done = " + DONE + " / " + N);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void compute() {
        if (this.end - this.start < this.limit) {
            calculate();
        } else {
            int newEnd = this.start + (this.end - this.start) / 2;
            int newStart = (this.start + (this.end - this.start) / 2) + 1;
            MarkThread markThread1 = new MarkThread(this.start, newEnd, limit);
            MarkThread markThread2 = new MarkThread(newStart, this.end, limit);

            invokeAll(markThread1, markThread2);
        }
    }
}