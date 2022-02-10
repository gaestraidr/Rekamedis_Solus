/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.gaestra.sysinforekamedis.extras.DBConfig;
import me.gaestra.sysinforekamedis.helper.Logger;

/**
 *
 * @author Ganesha
 */
public class DatabaseManager {
    
    private static DatabaseManager connector;
    private Connection connection;
    private DBConfig config;
    
    private String jdbc_driver;
    private String type;

    public Connection open() {
        try {
            this.setDatabaseType();
            Class.forName(jdbc_driver);
            String url = type + config.getHostName() + ":" + config.getPortNumber() + "/" + config.getDatabaseName();
            connection = DriverManager.getConnection(url, config.getUsername(), config.getPassword());

        } catch (ClassNotFoundException | SQLException e) {
            Logger.error("Failed to open connection: " + e.getMessage());
        }

        return connection;
    }

    public void execute(String sql) {
        open();
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            Logger.error("Failed to execute statement: " + e.getMessage());
        }
        close();
    }

    public ResultSet executeQuery(String sql) {
        open();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            return resultSet;
        } catch (SQLException e) {
            Logger.error("Failed to execute query: " + e.getMessage());
        }
        close();

        return null;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            Logger.error("Failed to close connection: " + e.getMessage());
        }
    }

    public Object getResult(ResultSet rs, String type, String column) throws SQLException {
        switch (type) {
            case "int":
            case "Integer":
                return rs.getInt(column);
            case "double":
            case "Double":
                return rs.getDouble(column);
            case "boolean":
            case "Boolean":
                return rs.getBoolean(column);
            case "char":
            case "Character":
                return rs.getString(column).charAt(0);
            case "Array":
                return rs.getArray(column);
            case "String":
                return rs.getString(column);
        }
        return null;
    }
    
    public void setDBConfig(DBConfig config) {
        this.config = config;
        setDatabaseType();
    }
    
    public DBConfig getDBConfig() {
        return config;
    }
    
    private void setDatabaseType() {
        switch (config.getDatabaseType()) {
            case DatabaseType.MYSQL:
                jdbc_driver = "com.mysql.jdbc.Driver";
                type = "jdbc:mysql://";
                break;
            case DatabaseType.POSTGRES:
                jdbc_driver = "org.postgresql.Driver";
                type = "jdbc:postgresql://";
                break;
            default:
                System.err.println("An error have occurred");
        }
    }
    
    public final static DatabaseManager getInstance() {
        if (connector == null) {
            connector = new DatabaseManager();
        }
        
        return connector;
    }
    
    public interface DatabaseType {
        public final String MYSQL = "MYSQL";
        public final String POSTGRES = "POSTGRES";
    }
}
