/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.extras;

import me.gaestra.sysinforekamedis.DatabaseManager.DatabaseType;

/**
 *
 * @author Ganesha
 */
public class MySQLDBConfig implements DBConfig {
    
    public MySQLDBConfig() {}
    
    @Override
    public String getDatabaseType() {
        return DatabaseType.MYSQL;
    }

    @Override
    public String getHostName() {
        return "localhost";
    }

    @Override
    public String getPortNumber() {
        return "3306";
    }

    @Override
    public String getDatabaseName() {
        return "sysinforekamedis";
    }

    @Override
    public String getUsername() {
        return "root";
    }

    @Override
    public String getPassword() {
        return "";
    }
}
