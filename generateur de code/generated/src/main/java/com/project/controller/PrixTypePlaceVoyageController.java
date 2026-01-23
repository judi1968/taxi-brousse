package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.PrixTypePlaceVoyageDTO;
import com.project.model.table.PrixTypePlaceVoyage;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class PrixTypePlaceVoyageController {

    @PostMapping("/savePrixTypePlaceVoyage")
    public String savePrixTypePlaceVoyage(@ModelAttribute PrixTypePlaceVoyageDTO prix_type_place_voyageDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet PrixTypePlaceVoyage à partir du DTO
            PrixTypePlaceVoyage prix_type_place_voyage = new PrixTypePlaceVoyage();
            
            // Récupération de l'objet TypePlace
            if (prix_type_place_voyageDTO.getTypePlaceId() != null) {
                TypePlace type_place = TypePlace.getById(prix_type_place_voyageDTO.getTypePlaceId(), connection);
                prix_type_place_voyage.setTypePlace(type_place);
            }
            
            // Récupération de l'objet Voyage
            if (prix_type_place_voyageDTO.getVoyageId() != null) {
                Voyage voyage = Voyage.getById(prix_type_place_voyageDTO.getVoyageId(), connection);
                prix_type_place_voyage.setVoyage(voyage);
            }
            prix_type_place_voyage.setMontant(prix_type_place_voyageDTO.getMontant());
            
            // Récupération de l'objet TypeClient
            if (prix_type_place_voyageDTO.getTypeClientId() != null) {
                TypeClient type_client = TypeClient.getById(prix_type_place_voyageDTO.getTypeClientId(), connection);
                prix_type_place_voyage.setTypeClient(type_client);
            }
            
            // Sauvegarder dans la base de données
            DB.save(prix_type_place_voyage, connection);
            
            // Message de succès
            model.addAttribute("success", "PrixTypePlaceVoyage enregistré avec succès !");
            model.addAttribute("prix_type_place_voyageDTO", new PrixTypePlaceVoyageDTO()); // Réinitialiser le formulaire
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("prix_type_place_voyageDTO", prix_type_place_voyageDTO); // Garder les données saisies
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/prix_type_place_voyage/creation";
    }

    @GetMapping("/listePrixTypePlaceVoyage")
    public String listePrixTypePlaceVoyage(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des prix_type_place_voyages depuis la base
            List<PrixTypePlaceVoyage> prix_type_place_voyages = (List<PrixTypePlaceVoyage>) DB.getAll(new PrixTypePlaceVoyage(), connection);

            // Ajouter la liste des prix_type_place_voyages au modèle
            model.addAttribute("prix_type_place_voyages", prix_type_place_voyages);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des prix_type_place_voyages: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/prix_type_place_voyage/listePrixTypePlaceVoyage";
    }
}
