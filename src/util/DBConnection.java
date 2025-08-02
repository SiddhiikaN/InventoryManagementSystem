package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(".env"));
        } catch (IOException e) {
            System.out.println("Could not load .env file");
            e.printStackTrace();
        }

        URL = props.getProperty("DB_URL");
        USER = props.getProperty("DB_USER");
        PASSWORD = props.getProperty("DB_PASSWORD");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
