package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.databases.MyConnection;
import com.project.databases.generalisation.DB;
import com.project.dto.TypeClientDTO;
import com.project.model.table.TypeClient;

@Controller
public class TypeClientController {
    
    
    
    @PostMapping("/saveTypeClient")
    public String saveTypeClient(@ModelAttribute TypeClientDTO typeClientDTO, Model model) {
        
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            
            TypeClient typeClient = new TypeClient();
            typeClient.setNom(typeClientDTO.getNom());
            
            DB.save(typeClient, connection);
            
            model.addAttribute("success", "Type de client enregistré avec succès !");
            model.addAttribute("typeClientDTO", new TypeClientDTO());
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            model.addAttribute("typeClientDTO", typeClientDTO);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/voiture/ajoutTypeClient";
    }
    
    @GetMapping("/listeTypeClient")
    public String listeTypeClient(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            
            List<TypeClient> typeClients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);
            
            model.addAttribute("typeClients", typeClients);
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des types de client: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/voiture/listeTypeClient";
    }
}