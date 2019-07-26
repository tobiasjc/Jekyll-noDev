/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcc.bri.app;

import bcc.bri.structures.QueryEntity;
import bcc.bri.algorithm.IndexBuild;
import bcc.bri.structures.WordIdDocQt;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

/**
 * @author flavio
 */
public class App {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     */
    public static void main(String[] args) throws SQLException, FileNotFoundException, UnsupportedEncodingException {

        String host = "127.0.0.1";
        String port = "3306";
        String database = "bri_2018";
        String user = "bri_2018";
        String password = "bri_2018";

        String connectionString = "jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + password + "&useSSL=false";

        Connection documentsQueryConn = DriverManager.getConnection(connectionString);

        System.out.println("Building index...");
        IndexBuild ib = IndexBuild.getInstance();

        System.out.println("Done!");

        Scanner sc = new Scanner(System.in);

        String ans = "y";

        // Stemmer
        Stemmer st = new Stemmer();

        // Statement
        while (ans.equals("y")) {
            String documentsQuery = "select id, title, url, abstract from content where id in (";
            Statement documentsQueryStatement = documentsQueryConn.createStatement();

            System.out.print("Query: ");
            String query = sc.nextLine();

            long startingMoment = System.currentTimeMillis();
            query = query.toLowerCase();
            query = query.replaceAll("\\s+", " ");
            String[] tmp = query.split(" ");

            Set<String> palavras = new HashSet<>();

            //adiciona todas as palavras da consulta
            //em um HashSet, desprezando-se portanto
            //palavras duplicadas
            for (String s : tmp) {
                st.add(s.toCharArray(), s.length());
                st.stem();
                s = st.toString();
                palavras.add(s);
            }

            //para cada palavra da consulta
            //deseja-se obter em quantos documentos a
            //mesma aparece
            ArrayList<WordIdDocQt> pvsDoc = new ArrayList<>();

            palavras.forEach((p) -> {
                //retorna id da palavra e em quantos documentos ela ocorre
                int id = ib.getWordId(p);
                if (id >= 0) {
                    int count = ib.getDocQt(p);

                    //adiciona na lista a relação idPalavra e qtdDocumentosPossuemPalavra
                    pvsDoc.add(new WordIdDocQt(id, count));
                }
            });

            //ordena a lista de palavras e documentos
            //comecando pela palavra que aparece em
            //menos documentos
            if (!pvsDoc.isEmpty()) {
                pvsDoc.sort(WordIdDocQt::compareTo);
            } else {
                System.out.println("Your search - '" + query + "' - did not match any documents.\n\n");
                continue;
            }

            //hora de fazer a intersecao desses conjuntos!!
            /*
            Uma hash map para cada palavra, contendo então sua Entry <documentId, documentWij>
            para cada documento da palavra. No final então, para N palavras, teremos
            N hash maps, cada uma com K entradas, onde K é o número de documentos
            que cada palavra está ṕresente. Assim sendo:
             */
            HashMap[] infoMap = new HashMap[pvsDoc.size()];

            String path = System.getProperty("user.dir");
            String folder = "/index_textual/";

            /*
             * adcionando todos os documentos para cada palavra em um array de:
             * Map.Entry<Integer, Double>
             */
            int count = 0;
            for (WordIdDocQt o : pvsDoc) {
                infoMap[count++] = new HashMap<Integer, Double>();
                try (BufferedReader br = new BufferedReader(new FileReader(path + folder + o.getWordId()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] info = line.split(" ");
                        int documentId = Integer.parseInt(info[0]);
                        double documentWij = Double.parseDouble(info[1]);
                        infoMap[count - 1].put(documentId, documentWij);
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }

            /*
            Neste hash map final ficarão apenas os documentos que contém todas as
            palavras da busca. Este hash map tem papel fundamental na ordenação,
            e outra estrutura não deve ser utilizada.
            A primeira palavra é o que possui menor conjunto de todas as outros,
            portanto, preciso fazer a comparação partindo do mesmo, afim de
            diminuir o número de comparações e maximizar a comparação. Nesse caso,
            todos os documentos da primeira palavra são automaticamente adcionados
            a hash map final, e retirados da mesma caso necessário.
             */
            HashMap<Integer, Double> finalMap = new HashMap<>();
            finalMap.putAll(infoMap[0]); // método do tipo "getNewInstanceOf()", ou seja, não há risco de exclusão via ponteiros

            /*
            Olhando para as keys da primeira hash map, ou seja da menor hash map,
            procuro se todos os documentos possuem esta key. Caso possuam, eu 
            mantenho a key em outra hash map final, bem como valor somados
            dos wij para uso posterior.
             */
            for (Object external : infoMap[0].entrySet()) {
                Map.Entry<Integer, Double> firstEntry = (Map.Entry<Integer, Double>) external;
                int keyFirst = firstEntry.getKey();
                double valueFirst = firstEntry.getValue();

                for (int index = 1; index < infoMap.length && !finalMap.isEmpty(); index++) {
                    Double value = (Double) infoMap[index].get(keyFirst);

                    if (value != null) {
                        finalMap.replace(keyFirst, value + valueFirst);
                    } else {
                        finalMap.remove(keyFirst);
                        break;
                    }
                }
            }

            int limit = 10; // sinta-se a vontade para mudar o número máximo de documentos mostrados

            if (finalMap.isEmpty()) {
                System.out.println("Your search - '" + query + "' - did not match any documents.\n\n");
                continue;
            }

            /*
            Afim de usar o método de ordenação dado pelo java, passo meu hash
            map para um array list que guardará os meus entries do hash map.
            Ordeno então os mesmos por valor, tendo ao final a minha lista ordenada
            contendo valores e chaves em seus respectivos lugares.
             */
            List<Map.Entry<Integer, Double>> list = new ArrayList<>(finalMap.entrySet());
            System.out.println("Showing top " + ((list.size() < limit) ? list.size() : limit) + " of " + list.size() + " documents found for your query.");

            list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue())); // ordena lista por valor de map
            if (list.size() > limit) {
                list.subList(limit, list.size()).clear();
            }

            /*
            Hora de montar a string para a query. Esta string possibilitará a
            query ser feita em uma única busca.
            Além disso, uma nova hash do tipo <Inteiro, QueryEntity> é montada. Essa
            hash map permite um mapeamento direto de chaves para instância de busca,
            fazendo com que tenhamos ordem garantida na busca utilizando as keys
            vindas da lista já previamente montada e ordenada por wij.
             */
            HashMap<Integer, QueryEntity> showHash = new HashMap<>();
            for (Map.Entry<Integer, Double> entry : list) {
                int key = entry.getKey();
                showHash.put(key, new QueryEntity());
                documentsQuery = documentsQuery.concat(key + ",");
            }
            int position = documentsQuery.lastIndexOf(",");
            documentsQuery = documentsQuery.substring(0, position).concat(");");
            ResultSet rs = documentsQueryStatement.executeQuery(documentsQuery);

            /*
            Populando os objetos de entidade com os dados a serem mostrados. Mapeamento
            direto <documentId, QueryEntity> feito.
             */
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = new String(rs.getBytes("title"), StandardCharsets.UTF_8);
                String url = new String(rs.getBytes("url"), StandardCharsets.UTF_8);
                String abstrac = new String(rs.getBytes("abstract"), StandardCharsets.UTF_8);

                showHash.get(id).title = title;
                showHash.get(id).url = url;
                showHash.get(id).abstrac = abstrac;
            }

            // fim da busca e das etapas de ordenação, fim da marcação do tempo
            long endingMoment = System.currentTimeMillis() - startingMoment;

            // demonstração do tempo ocorrido em s/1000
            System.out.println("time = " + endingMoment + " ms\n");

            /*
            Mostra os dados seguindo a ordem da lista que possui os valores
            ordenados por wij.
             */
            list.forEach((Map.Entry<Integer, Double> p) -> {
                int key = p.getKey();
                double value = p.getValue();

                QueryEntity showEntity = showHash.get(key);

                System.out.println("Title = " + showEntity.title);
                System.out.println("URL = " + showEntity.url);
                System.out.println("Abstract = " + showEntity.abstrac);
                System.out.println("Rating = " + value);
                System.out.println("----------------------------------------------------------------------");
            });
        }
    }
}
