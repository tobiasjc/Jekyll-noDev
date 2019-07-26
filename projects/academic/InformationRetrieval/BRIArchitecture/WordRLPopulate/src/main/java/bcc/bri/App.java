package bcc.bri;

import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        // JDBC info
        String host = "localhost";
        String database = "bri_2018";
        String user = "bri_2018";
        String password = "bri_2018";

        // program strings for query, updates and connection
        String connectionString = "jdbc:mariadb://" + host + "/" + database + "?user=" + user + "&password=" + password;
        String insertWordString = "insert into word (word, ni) values (?, 1);";
        String insertContentWordString = "insert into rlContentWord (contentId, wordId, fij) values (?, ?, 1);";
        String updateContentWordFIJString = "update rlContentWord set fij = fij + 1 where contentId = ? and wordId = ?;";
        String updateContentWordWIJString = "update rlContentWord set wij = ? where contentId = ? and wordId = ?;";
        String updateWordString = "update word set ni = ni + 1 where id = ?;";
        String searchWordIDString = "select id from word where word = ?;";
        String searchWordNIString = "select ni from word where id = ?";
        String lastWordString = "select max(id) from word";
        String selectWIJSum = "select sum(pow(wij, 2)) from rlContentWord where contentId = ?;";
        String updateDJ = "update content set dj = ? where id = ?;";

        try (Connection insertWordStatementConn = DriverManager.getConnection(connectionString);
             Connection updateStatementConn = DriverManager.getConnection(connectionString);
             Connection searchWordIDStatementConn = DriverManager.getConnection(connectionString);
             Connection searchWordNIStatementConn = DriverManager.getConnection(connectionString);
             Connection lastWordConn = DriverManager.getConnection(connectionString);
             Connection insertContentWordConn = DriverManager.getConnection(connectionString);
             Connection updateContentWordFIJConn = DriverManager.getConnection(connectionString);
             Connection updateContentWordWIJConn = DriverManager.getConnection(connectionString);
             Connection selectWIJSumStatementConn = DriverManager.getConnection(connectionString);
             Connection updateDJConn = DriverManager.getConnection(connectionString);
             Connection statementConn = DriverManager.getConnection(connectionString);
             PreparedStatement insertWordStatement = insertWordStatementConn.prepareStatement(insertWordString);
             PreparedStatement searchWordIDStatement = searchWordIDStatementConn.prepareStatement(searchWordIDString);
             PreparedStatement searchWordNIStatement = searchWordNIStatementConn.prepareStatement(searchWordNIString);
             PreparedStatement updateWordStatement = updateStatementConn.prepareStatement(updateWordString);
             PreparedStatement lastWordStatement = lastWordConn.prepareStatement(lastWordString);
             PreparedStatement insertContentWordStatement = insertContentWordConn.prepareStatement(insertContentWordString);
             PreparedStatement updateContentWordFIJStatement = updateContentWordFIJConn.prepareStatement(updateContentWordFIJString);
             PreparedStatement updateContentWordWIJStatement = updateContentWordWIJConn.prepareStatement(updateContentWordWIJString);
             PreparedStatement selectWIJSumStatement = selectWIJSumStatementConn.prepareStatement(selectWIJSum);
             PreparedStatement updateDJStatement = updateDJConn.prepareStatement(updateDJ);
             Statement stmt = statementConn.createStatement()) {

            // fully populate word table, and insert rlContentWord without wij
            Stemmer stemmer = new Stemmer();

            ResultSet rsN = stmt.executeQuery("select count(*) from content;");
            rsN.next();
            int N = rsN.getInt(1);
            int saveN = N;

            ResultSet rsContent1 = stmt.executeQuery("select * from content");

            System.out.println("First phase: Start...");
            while (rsContent1.next()) {
                int contentId = rsContent1.getInt(1);
                String text = new String(rsContent1.getBytes(4), StandardCharsets.UTF_8);
                text = text.replaceAll("\\s+", " ");
                String[] words = text.split(" ");
                HashSet<String> added = new HashSet<>();

                for (String w : words) {
                    if (!w.matches("[a-zA-Z]+"))
                        continue;
                    w = w.toLowerCase();
                    stemmer.add(w.toCharArray(), w.length());
                    stemmer.stem();
                    w = stemmer.toString();

                    Blob b1 = searchWordIDStatementConn.createBlob();
                    b1.setBytes(1, w.getBytes());
                    searchWordIDStatement.setBlob(1, b1);

                    ResultSet rsWord = searchWordIDStatement.executeQuery();
                    int gotId;

                    if (rsWord.next()) {
                        gotId = rsWord.getInt(1);
                        if (!added.contains(w)) {
                            updateWordStatement.setInt(1, gotId);
                            updateWordStatement.executeUpdate();
                            // System.out.println("Updated stem = " + w);
                        }
                    } else {
                        Blob b2 = insertWordStatementConn.createBlob();
                        b2.setBytes(1, w.getBytes());
                        insertWordStatement.setBlob(1, b2);
                        insertWordStatement.executeUpdate();

                        ResultSet maxRs = lastWordStatement.executeQuery();
                        maxRs.next();
                        gotId = maxRs.getInt(1);
                        // System.out.println("Inserted new stem = " + w);
                    }

                    try {
                        insertContentWordStatement.setInt(1, contentId);
                        insertContentWordStatement.setInt(2, gotId);
                        insertContentWordStatement.executeUpdate();
//                        System.out.println("Inserted new relation of (contentId, wordId) = (" + contentId + ", " + maxId + ") with ni = 1");
                    } catch (Exception e) {
                        updateContentWordFIJStatement.setInt(1, contentId);
                        updateContentWordFIJStatement.setInt(2, gotId);
                        updateContentWordFIJStatement.executeUpdate();
//                        System.out.println("Updated relation of (contentId, wordId) = (" + contentId + ", " + maxId + ") to ni = ni + 1");
                    }
                    added.add(w);
                }
                System.out.println("Done = " + contentId + " / " + N);
            }
            System.out.println("First phase: Done.");

            // insert wij into rlContentWord
            // TODO: pass this phase to multi-thread
            rsN = stmt.executeQuery("select count(*) from rlContentWord;");
            rsN.next();

            N = rsN.getInt(1);
            int count = 0;

            ResultSet rsrlContentWord = stmt.executeQuery("select * from rlContentWord;");

            System.out.println("Second phase: Start...");
            while (rsrlContentWord.next()) {
                int contentId = rsrlContentWord.getInt(1);
                int wordId = rsrlContentWord.getInt(2);
                int fij = rsrlContentWord.getInt(3);
                searchWordNIStatement.setInt(1, wordId);
                ResultSet rsNI = searchWordNIStatement.executeQuery();
                rsNI.next();
                int ni = rsNI.getInt(1);
                double wij = (1 + log2(fij)) * (log2((N / (double) ni)));

                updateContentWordWIJStatement.setDouble(1, wij);
                updateContentWordWIJStatement.setInt(2, contentId);
                updateContentWordWIJStatement.setInt(3, wordId);
                updateContentWordWIJStatement.executeUpdate();
//                System.out.println("wij(wordId, ni, fij) = wij(" + wordId + ", " + ni + ", " + fij + ") = " + wij);
                count++;
                System.out.println("Done = " + count + " / " + N);
            }
            System.out.println("Second phase: Done.");


            ResultSet rsContent2 = stmt.executeQuery("select id from content");
            System.out.println("Third phase: Start...");
            count = 0;
            while (rsContent2.next()) {
                int contentId = rsContent2.getInt(1);
                selectWIJSumStatement.setInt(1, contentId);
                ResultSet rsSum = selectWIJSumStatement.executeQuery();
                rsSum.next();

                double toUpdateDj = Math.sqrt(rsSum.getDouble(1));

                updateDJStatement.setDouble(1, toUpdateDj);
                updateDJStatement.setInt(2, contentId);
                updateDJStatement.executeUpdate();
                count++;
                System.out.println("Done = " + count + " / " + saveN);
            }
            System.out.println("Third phase: Done.");

            rsN.close();
            rsContent1.close();
            rsContent2.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static double log2(double value) {
        return Math.log(value) / Math.log(2.0);
    }
}