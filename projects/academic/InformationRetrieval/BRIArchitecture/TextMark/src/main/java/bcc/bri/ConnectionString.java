package bcc.bri;

class ConnectionString {
    private static final String host = "127.0.0.1";
    private static final String database = "bri_2018";
    private static final String user = "bri_2018";
    private static final String password = "bri_2018";

    static String getConnectionString() {
        return "jdbc:mariadb://" + host + ":3306/" + database + "?user=" + user + "&password=" + password;
    }
}
