package com.project.pja.databases;



import java.sql.Connection; 
import java.sql.DriverManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MyConnection {
    static String ip;
    static int port;
    static String databaseName;
    static String userName;
    static String password;
    public static void setDatabaseName(String databaseName) {
        MyConnection.databaseName = databaseName;
    }
    public static void setIp(String ip) {
        MyConnection.ip = ip;
    }
    public static void setPassword(String password) {
        MyConnection.password = password;
    }
    public static void setPort(int port) {
        MyConnection.port = port;
    }
    public static void setUserName(String userName) {
        MyConnection.userName = userName;
    }
    public static String getDatabaseName() {
        return databaseName;
    }
    public static String getIp() {
        return ip;
    }
    public static String getPassword() {
        return password;
    }
    public static int getPort() {
        return port;
    }
    public static String getUserName() {
        return userName;
    }

    public static Connection connect() throws Exception{
        return MyConnection.connect(MyConnection.getIp(), MyConnection.getPort(), MyConnection.getDatabaseName(), MyConnection.getUserName(), MyConnection.getPassword());    
    }
    public static Connection connect(String ip,int port,String databaseName,String userName,String password) throws Exception{
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection("jdbc:postgresql://"+ip+":"+port+"/"+databaseName+"", ""+userName+"", ""+password+"");
    }
}
