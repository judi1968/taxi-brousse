package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.databases.MyConnection;
import com.project.databases.generalisation.DB;
import com.project.dto.AchatClientDTO;
import com.project.model.table.*;

@Controller
public class AchatClientController {
    
    @PostMapping("/saveAchatClient")
    public String saveAchatClient(@ModelAttribute AchatClientDTO achatDTO, Model model) {
        
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            
            // Récupérer les objets
            TypeClient typeClient = (TypeClient) DB.getById(new TypeClient(), achatDTO.getIdTypeClient(), connection);
            TypePlaceVoyage typePlaceVoyage = (TypePlaceVoyage) DB.getById(new TypePlaceVoyage(), achatDTO.getIdTypePlaceVoyage(), connection);
            
            // Créer l'objet AchatClient
            AchatClient achatClient = new AchatClient();
            achatClient.setTypeClient(typeClient);
            achatClient.setTypePlaceVoyage(typePlaceVoyage);
            
            // Sauvegarder
            DB.save(achatClient, connection);
            
            // Recharger les listes
            List<TypeClient> typeClients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);
            List<TypePlaceVoyage> typePlaceVoyages = (List<TypePlaceVoyage>) DB.getAll(new TypePlaceVoyage(), connection);
            
            model.addAttribute("typeClients", typeClients);
            model.addAttribute("typePlaceVoyages", typePlaceVoyages);
            model.addAttribute("success", "Achat client enregistré avec succès !");
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            model.addAttribute("achatDTO", achatDTO);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/voiture/achatClient";
    }
}