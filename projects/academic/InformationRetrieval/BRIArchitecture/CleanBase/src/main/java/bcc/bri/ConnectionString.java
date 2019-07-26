package bcc.bri;

public class ConnectionString {
    private static final String host = "localhost";
    private static final String database = "bri_2018";
    private static final String user = "bri_2018";
    private static final String password = "bri_2018";

    public static String getConnectionString() {
        return "jdbc:mariadb://" + host + "/" + database + "?user=" + user + "&password=" + password;
    }
}
