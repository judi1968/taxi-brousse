package com.project.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.configuration.DatabaseConfigProperties;
import com.project.databases.MyConnection;
import com.project.databases.generalisation.DB;
import com.project.model.table.PlaceVoiture;
import com.project.model.table.Voiture;

@Controller
public class RooterController {
    
    @Autowired
    private DatabaseConfigProperties dbConfig;
    
    @GetMapping("/") 
    public String home() throws Exception {
        System.out.println("Database Config: " + dbConfig.toString());
        
        System.out.println("MyConnection values:");
        System.out.println("Host: " + MyConnection.getIp());
        System.out.println("Port: " + MyConnection.getPort());
        System.out.println("Database: " + MyConnection.getDatabaseName());
        System.out.println("Username: " + MyConnection.getUserName());
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            Voiture fiara = new Voiture();
            fiara.setId(2);
            fiara.setNom("BMWx");
            fiara.setNumero("TDI8842");
            // DB.save(fiara, connection);

            PlaceVoiture placeVoiture = (PlaceVoiture) DB.getById(new PlaceVoiture(), 1, connection);
            placeVoiture.setVoiture(fiara);
            DB.save(placeVoiture, connection);
            // List<PlaceVoiture> voitures = (List<PlaceVoiture>) DB.getAll(new PlaceVoiture(), connection);
            List<PlaceVoiture> voitures = (List<PlaceVoiture>) DB.getAllOrderAndLimitAndWhere(new PlaceVoiture(),"numero = '12'", "id DESC, numero ASC", 6,connection); 
            for (PlaceVoiture voiture : voitures) {
                System.out.println(voiture.getId()+" : "+voiture.getVoiture().getNom()+" : "+voiture.getNumero());
            }
            if (connection== null) {
                System.out.println("arakzany");
            }else{
                System.out.println("mety io eeh "); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        if (connection != null) {
            connection.close();
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