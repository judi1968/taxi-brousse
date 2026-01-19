package com.project.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.project.pja.databases.MyConnection;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    
    @Autowired
    private DatabaseConfigProperties dbConfig;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialiser MyConnection avec les valeurs de DatabaseConfigProperties
        MyConnection.setIp(dbConfig.getHost());
        MyConnection.setPort(dbConfig.getPort());
        MyConnection.setDatabaseName(dbConfig.getDatabase());
        MyConnection.setUserName(dbConfig.getUsername());
        MyConnection.setPassword(dbConfig.getPassword());
        
        System.out.println("MyConnection initialized from application properties");
        System.out.println("Database: " + dbConfig.getDatabase());
        System.out.println("Host: " + dbConfig.getHost());
    }
}