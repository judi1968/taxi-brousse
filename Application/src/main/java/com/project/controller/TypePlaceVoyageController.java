package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.TypePlaceVoyageDTO;
import com.project.model.table.*;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class TypePlaceVoyageController {
    
    @PostMapping("/saveTypePlaceVoyage")
    public String saveTypePlaceVoyage(@ModelAttribute TypePlaceVoyageDTO typePlaceVoyageDTO, Model model) {
        
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            
            // Récupérer les objets
            VoyageVoiture voyageVoiture = (VoyageVoiture) DB.getById(new VoyageVoiture(), typePlaceVoyageDTO.getIdVoyageVoiture(), connection);
            PlaceVoiture place = (PlaceVoiture) DB.getById(new PlaceVoiture(), typePlaceVoyageDTO.getIdPlace(), connection);
            TypePlace typePlace = (TypePlace) DB.getById(new TypePlace(), typePlaceVoyageDTO.getIdTypePlace(), connection);
            
            // Créer l'objet TypePlaceVoyage
            TypePlaceVoyage typePlaceVoyage = new TypePlaceVoyage();
            typePlaceVoyage.setVoyageVoiture(voyageVoiture);
            typePlaceVoyage.setPlace(place);
            typePlaceVoyage.setTypePlace(typePlace);
            
            // Sauvegarder
            DB.save(typePlaceVoyage, connection);
            
            // Recharger les listes
            List<VoyageVoiture> voyageVoitures = (List<VoyageVoiture>) DB.getAll(new VoyageVoiture(), connection);
            List<PlaceVoiture> places = (List<PlaceVoiture>) DB.getAll(new PlaceVoiture(), connection);
            List<TypePlace> typePlaces = (List<TypePlace>) DB.getAll(new TypePlace(), connection);
            
            model.addAttribute("voyageVoitures", voyageVoitures);
            model.addAttribute("places", places);
            model.addAttribute("typePlaces", typePlaces);
            model.addAttribute("success", "Association type-place-voyage enregistrée avec succès !");
            model.addAttribute("typePlaceVoyageDTO", new TypePlaceVoyageDTO());
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/voiture/ajoutTypePlaceVoyage";
    }
}