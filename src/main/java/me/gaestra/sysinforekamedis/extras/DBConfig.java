/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.extras;

/**
 *
 * @author Ganesha
 */
public interface DBConfig {
    
    public String getDatabaseType();

    public String getHostName();

    public String getPortNumber();

    public String getDatabaseName();

    public String getUsername();

    public String getPassword();
}
