/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcc.bri.algorithm;

import bcc.bri.structures.WordIdDocQt;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author flavio
 */
public class IndexBuild {

    //dada uma palavra, essa lista possui as paginas
    //onde aquela palavra ocorre, e o valor
    //de wij que aquela palavra contribuiu para
    //o valor final de tf-idf da pagina
	/*Map<Integer, Double> mapPagesWij;
	List<PageIdWij> listPagesWij;*/
    public static IndexBuild instance;
    //dada a palavra, retorna qual o seu id, e em
    //quantas paginas a mesma aparece
    public final Map<String, WordIdDocQt> wordWordIdDocQt;

    public IndexBuild() {
        wordWordIdDocQt = new HashMap();
    }

    public static IndexBuild getInstance() {
        if (instance == null) {
            instance = new IndexBuild();
            System.out.println("Loading Word Ids");
            instance.loadWordIds();
            System.out.println("Loading Word Pages");
            instance.loadPageIds();
        }
        return instance;
    }

    /**
     * Dada uma palavra, guardara o seu id,
     * e em quantos documentos a mesma ocorre.
     */
    public void loadWordIds() {
        Connection conn;
        PreparedStatement pstmt;
        ResultSet rs;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bri_2018?user=bri_2018&password=bri_2018&useSSL=false");
            int min = 1;
            int gap = 1000;
            int max;

            pstmt = conn.prepareStatement("SELECT max(id) FROM word;");
            rs = pstmt.executeQuery();
            rs.next();
            max = rs.getInt(1);
            pstmt.close();

            while (min <= max) {
                String q = "SELECT w.id, w.word, count(*) FROM word w, rlContentWord cw "
                        + "WHERE w.id = cw.wordId "
                        + "AND (w.id BETWEEN " + min + " AND " + (min + gap) + ") "
                        + "GROUP BY w.id";
                pstmt = conn.prepareStatement(q);
                rs = pstmt.executeQuery();
                //System.out.println("Fetching between "+min+" and "+(min + gap)+".");
                while (rs.next()) {
                    int id = rs.getInt("w.id");
                    String word = rs.getString("w.word");
                    int count = rs.getInt("count(*)");

                    wordWordIdDocQt.put(word, new WordIdDocQt(id, count));
                }
                pstmt.close();
                min = min + gap + 1;
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(IndexBuild.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Dado o id de uma palavra, gerara um arquivo
     * texto contendo em cada linha, o id do
     * documento que contem a palavra, e o
     * valor wij que a palavra contribuiu para o
     * valor final de TF-IDF do documento.
     */
    public void loadPageIds() {
        Connection conn;
        PreparedStatement pstmt;
        ResultSet rs;

        //obtem o path onde a aplicacao se encontra
        String path = System.getProperty("user.dir");

        try {
            if (Files.exists(Paths.get(path + "/index_textual/"))) {
                return;
            } else {
                Files.createDirectory(Paths.get(path + "/index_textual/"));
            }

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bri_2018?user=bri_2018&password=bri_2018&useSSL=false");
            int wordId = 1;
            int max;

            pstmt = conn.prepareStatement("SELECT max(id) FROM word;");
            rs = pstmt.executeQuery();
            rs.next();
            max = rs.getInt(1);
            pstmt.close();

            while (wordId <= max) {
                String q = "SELECT contentId, wij FROM rlContentWord WHERE wordId = " + wordId + " ORDER BY wij DESC;";

                pstmt = conn.prepareStatement(q);
                rs = pstmt.executeQuery();

                try (PrintWriter writer = new PrintWriter(path + "/index_textual/" + wordId, "UTF-8")) {
                    while (rs.next()) {
                        int contentId = rs.getInt("contentId");
                        double wij = rs.getDouble("wij");
                        writer.println(contentId + " " + wij);
                    }
                }
                pstmt.close();

                wordId++;
            }
            conn.close();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(IndexBuild.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getWordId(String word) {
        WordIdDocQt w = wordWordIdDocQt.get(word);
        if (w != null) {
            return (w.getWordId());
        } else {
            return -1;
        }
    }

    public int getDocQt(String word) {
        return (wordWordIdDocQt.get(word).getDocQt());
    }
}
