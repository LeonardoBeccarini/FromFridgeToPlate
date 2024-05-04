package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DatabaseConnectionInitializationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SingletonConnector {

    private final Connection connection;
    private static SingletonConnector instance;
    protected SingletonConnector() {
        try (InputStream is = new FileInputStream("src/main/resources/com/example/Properties/db_config.properties")) {
            Properties prop = new Properties();
            prop.load(is);

            String userConfig = prop.getProperty("USER");
            String passConfig = prop.getProperty("PASS");
            String dbUrlConfig = prop.getProperty("DB_URL");

            connection = DriverManager.getConnection(dbUrlConfig, userConfig, passConfig);
        } catch (IOException | SQLException e) {
            throw new DatabaseConnectionInitializationException("Error initializing database connection", e);
        }
    }

    public static synchronized SingletonConnector getInstance() {

       if(instance == null){
           instance = new SingletonConnector();
           return instance;
       }
       return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
