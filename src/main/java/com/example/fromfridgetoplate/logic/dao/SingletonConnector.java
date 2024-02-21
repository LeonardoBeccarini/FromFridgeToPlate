package com.example.fromfridgetoplate.logic.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class SingletonConnector {

    private final Connection connection;

    private SingletonConnector() {
        try (InputStream is = new FileInputStream("src/main/resources/com/example/Properties/db_config.properties")) {
            Properties prop = new Properties();
            prop.load(is);

            String userConfig = prop.getProperty("USER");
            String passConfig = prop.getProperty("PASS");
            String dbUrlConfig = prop.getProperty("DB_URL");

            connection = DriverManager.getConnection(dbUrlConfig, userConfig, passConfig);
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error initializing database connection", e);
        }
    }

    private static class SingletonHelper {
        private static final SingletonConnector INSTANCE = new SingletonConnector();
    }

    public static SingletonConnector getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }
}
