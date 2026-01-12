package com.project.controller;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.configuration.DatabaseConfigProperties;
import com.project.databases.MyConnection;

@Controller
public class RooterController {
    
    @Autowired
    private DatabaseConfigProperties dbConfig;
    
    @GetMapping("/") 
    public String home() {
        System.out.println("Database Config: " + dbConfig.toString());
        
        System.out.println("MyConnection values:");
        System.out.println("Host: " + MyConnection.getIp());
        System.out.println("Port: " + MyConnection.getPort());
        System.out.println("Database: " + MyConnection.getDatabaseName());
        System.out.println("Username: " + MyConnection.getUserName());

        try {
            Connection connection = MyConnection.connect();
            if (connection== null) {
                System.out.println("arakzany");
            }else{
                System.out.println("mety"); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        return "home";
    }

    @GetMapping("/form")
    public String form() {
        return "pages/form"; 
    }

    @GetMapping("/table")
    public String table() {
        return "pages/table";
    }

    @GetMapping("/log-out")
    public String logout() {
        return "pages/login";
    }
}  