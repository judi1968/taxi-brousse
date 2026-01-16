package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.databases.MyConnection;
import com.project.databases.generalisation.DB;
import com.project.dto.PrixTypePlaceVoyageDTO;
import com.project.model.table.*;

@Controller
public class PrixTypePlaceVoyageController {
    
    @PostMapping("/savePrixTypePlaceVoyage")
    public String savePrixTypePlaceVoyage(@ModelAttribute PrixTypePlaceVoyageDTO prixDTO, Model model) {
        
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            
            // Récupérer les objets
            TypePlace typePlace = (TypePlace) DB.getById(new TypePlace(), prixDTO.getIdTypePlace(), connection);
            TypeClient typeClient = (TypeClient) DB.getById(new TypeClient(), prixDTO.getIdTypeClient(), connection);
            Voyage voyage = (Voyage) DB.getById(new Voyage(), prixDTO.getIdVoyage(), connection);
            
            // Créer l'objet PrixTypePlaceVoyage
            PrixTypePlaceVoyage prixTypePlaceVoyage = new PrixTypePlaceVoyage();
            prixTypePlaceVoyage.setTypePlace(typePlace);
            prixTypePlaceVoyage.setVoyage(voyage);
            prixTypePlaceVoyage.setTypeClient(typeClient);
            prixTypePlaceVoyage.setMontant(prixDTO.getMontant());
            
            // Sauvegarder
            DB.save(prixTypePlaceVoyage, connection);
            
            // Recharger les listes
            List<TypePlace> typePlaces = (List<TypePlace>) DB.getAll(new TypePlace(), connection);
            List<Voyage> voyages = (List<Voyage>) DB.getAll(new Voyage(), connection);
            List<TypeClient> typeClients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);
            
            model.addAttribute("typePlaces", typePlaces);
            model.addAttribute("voyages", voyages);
            model.addAttribute("typeClients", typeClients);
            model.addAttribute("success", "Prix enregistré avec succès !");
            model.addAttribute("prixDTO", new PrixTypePlaceVoyageDTO());
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            model.addAttribute("prixDTO", prixDTO); // Garder les données
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/voiture/ajoutPrix";
    }
}