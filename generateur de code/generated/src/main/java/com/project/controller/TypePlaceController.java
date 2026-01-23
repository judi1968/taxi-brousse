package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.TypePlaceDTO;
import com.project.model.table.TypePlace;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class TypePlaceController {

    @PostMapping("/saveTypePlace")
    public String saveTypePlace(@ModelAttribute TypePlaceDTO type_placeDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet TypePlace à partir du DTO
            TypePlace type_place = new TypePlace();
            type_place.setNom(type_placeDTO.getNom());
            
            // Sauvegarder dans la base de données
            DB.save(type_place, connection);
            
            // Message de succès
            model.addAttribute("success", "TypePlace enregistré avec succès !");
            model.addAttribute("type_placeDTO", new TypePlaceDTO()); // Réinitialiser le formulaire
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("type_placeDTO", type_placeDTO); // Garder les données saisies
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/type_place/creation";
    }

    @GetMapping("/listeTypePlace")
    public String listeTypePlace(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des type_places depuis la base
            List<TypePlace> type_places = (List<TypePlace>) DB.getAll(new TypePlace(), connection);

            // Ajouter la liste des type_places au modèle
            model.addAttribute("type_places", type_places);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des type_places: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/type_place/listeTypePlace";
    }
}
