package pt.isec.metapd.repository.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnectionHelper {
    // JDBC Driver Name & Database URL
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String JDBC_DB_URL_HEADER = "jdbc:mysql://";

    // Database User Credentials
    private static final String DATABASE_USER = "metapd_server";
    private static final String DATABASE_PW = "metapd-123*";
    private static final String DATABASE_NAME = "metapd";

    private static final int MYSQL_DEFAULT_PORT = 3306;

    private DbConnectionHelper() { }

    public static synchronized Connection getConnection(String address) throws SQLException {
        String jdbcDbUrl = JDBC_DB_URL_HEADER + address + ":" + MYSQL_DEFAULT_PORT + "/" + DATABASE_NAME;

        Properties properties = new Properties();
        properties.setProperty("user", DATABASE_USER);
        properties.setProperty("password", DATABASE_PW);
        properties.setProperty("MaxPooledStatements", "250");

        Connection connection = DriverManager.getConnection(jdbcDbUrl, properties);
        connection.setAutoCommit(false);

        return connection;
    }
}
