package bcc.bri;


import wikipediafunctions.WikipediaFunctions;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.concurrent.RecursiveAction;


public class InsertThread extends RecursiveAction {

    private static final long serialVersionUID = 1L;
    private static int DONE = 0;
    private final int start, end, limit;

    InsertThread(int start, int end, int limit) {
        this.start = start;
        this.end = end;
        this.limit = limit;
    }

    private void calculate() {
        String insertString = "insert into content(title, url, text, abstract) values (?, ?, ?, ?);";
        ResultSet rs;

        try (Connection conn1 = DriverManager.getConnection(ConnectionString.getConnectionString());
             Connection conn2 = DriverManager.getConnection(ConnectionString.getConnectionString());
             PreparedStatement ps = conn2.prepareStatement(insertString);
             Statement stmt = conn1.createStatement();) {

            WikipediaFunctions c = new WikipediaFunctions();

            rs = stmt.executeQuery("select count(*) from page;");
            rs.next();
            int N = rs.getInt(1);

            rs = stmt.executeQuery("select page_title, old_text from page p, revision r, text t where p.page_latest = r.rev_id and r.rev_text_id = t.old_id and p.page_id between "
                    + this.start + " and " + this.end + ";");

            while (rs.next()) {
                String title_before = rs.getString("page_title");
                String title = title_before.replaceAll("_", " ");
                String url = "https://en.wikipedia.org/wiki/" + title_before;
                String text = null;

                try {
                    text = c.cleanText(new String(rs.getBytes("old_text"), StandardCharsets.UTF_8));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }

                String abst = null;
                if (text.length() > 128) {
                    abst = text.substring(0, 128);
                } else {
                    abst = text;
                }

                ps.setString(1, title);
                ps.setBytes(2, url.getBytes());
                ps.setBytes(3, text.getBytes());
                Blob blob = conn2.createBlob();
                blob.setBytes(4, abst.getBytes());
                ps.setBlob(4, blob);
                ps.executeUpdate();
                DONE += 1;
                System.out.println("Done ~= " + DONE + " / " + N);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void compute() {
        if ((this.end - this.start) < this.limit) {
            calculate();
        } else {
            int newEnd = this.start + (this.end - this.start) / 2;
            int newStart = (start + (this.end - this.start) / 2) + 1;
            InsertThread insertThread1 = new InsertThread(this.start, newEnd, this.limit);
            InsertThread insertThread2 = new InsertThread(newStart, this.end, this.limit);
            invokeAll(insertThread1, insertThread2);
        }
    }
}