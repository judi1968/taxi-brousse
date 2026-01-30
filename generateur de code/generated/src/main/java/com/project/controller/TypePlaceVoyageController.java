package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.TypePlaceVoyageDTO;
import com.project.model.table.TypePlaceVoyage;
import com.project.model.table.VoyageVoiture;
import com.project.model.table.PlaceVoiture;
import com.project.model.table.TypePlace;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class TypePlaceVoyageController {

    @GetMapping("/creationTypePlaceVoyage")
    public String goToCreate(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();


            // Récupérer la liste des voyage_voitures
            List<VoyageVoiture> voyage_voitures = (List<VoyageVoiture>) DB.getAll(new VoyageVoiture(), connection);
            model.addAttribute("voyage_voitures", voyage_voitures);

            // Récupérer la liste des place_voitures
            List<PlaceVoiture> place_voitures = (List<PlaceVoiture>) DB.getAll(new PlaceVoiture(), connection);
            model.addAttribute("place_voitures", place_voitures);

            // Récupérer la liste des type_places
            List<TypePlace> type_places = (List<TypePlace>) DB.getAll(new TypePlace(), connection);
            model.addAttribute("type_places", type_places);

            // Initialiser un DTO vide pour le formulaire
            model.addAttribute("type_place_voyageDTO", new TypePlaceVoyageDTO());

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des données: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/type_place_voyage/creation";
    }

    @PostMapping("/saveTypePlaceVoyage")
    public String saveTypePlaceVoyage(@ModelAttribute TypePlaceVoyageDTO type_place_voyageDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet TypePlaceVoyage à partir du DTO
            TypePlaceVoyage type_place_voyage = new TypePlaceVoyage();
            
            // Récupération de l'objet VoyageVoiture
            if (type_place_voyageDTO.getVoyageVoitureId() != 0) {
                VoyageVoiture voyage_voiture = VoyageVoiture.getById(type_place_voyageDTO.getVoyageVoitureId(), connection);
                type_place_voyage.setVoyageVoiture(voyage_voiture);
            }
            
            // Récupération de l'objet PlaceVoiture
            if (type_place_voyageDTO.getPlaceVoitureId() != 0) {
                PlaceVoiture place = PlaceVoiture.getById(type_place_voyageDTO.getPlaceVoitureId(), connection);
                type_place_voyage.setPlaceVoiture(place);
            }
            
            // Récupération de l'objet TypePlace
            if (type_place_voyageDTO.getTypePlaceId() != 0) {
                TypePlace type_place = TypePlace.getById(type_place_voyageDTO.getTypePlaceId(), connection);
                type_place_voyage.setTypePlace(type_place);
            }
            
            // Sauvegarder dans la base de données
            DB.save(type_place_voyage, connection);
            
            // Message de succès
            model.addAttribute("success", "TypePlaceVoyage enregistré avec succès !");
            model.addAttribute("type_place_voyageDTO", new TypePlaceVoyageDTO()); // Réinitialiser le formulaire
            
            // Recharger les listes pour les foreign keys
            List<VoyageVoiture> voyage_voitures = (List<VoyageVoiture>) DB.getAll(new VoyageVoiture(), connection);
            model.addAttribute("voyage_voitures", voyage_voitures);
            List<PlaceVoiture> place_voitures = (List<PlaceVoiture>) DB.getAll(new PlaceVoiture(), connection);
            model.addAttribute("place_voitures", place_voitures);
            List<TypePlace> type_places = (List<TypePlace>) DB.getAll(new TypePlace(), connection);
            model.addAttribute("type_places", type_places);
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("type_place_voyageDTO", type_place_voyageDTO); // Garder les données saisies
            
            // Recharger les listes en cas d'erreur
            try {
                List<VoyageVoiture> voyage_voitures = (List<VoyageVoiture>) DB.getAll(new VoyageVoiture(), connection);
                model.addAttribute("voyage_voitures", voyage_voitures);
                List<PlaceVoiture> place_voitures = (List<PlaceVoiture>) DB.getAll(new PlaceVoiture(), connection);
                model.addAttribute("place_voitures", place_voitures);
                List<TypePlace> type_places = (List<TypePlace>) DB.getAll(new TypePlace(), connection);
                model.addAttribute("type_places", type_places);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/type_place_voyage/creation";
    }

    @GetMapping("/listeTypePlaceVoyage")
    public String listeTypePlaceVoyage(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des type_place_voyages depuis la base
            List<TypePlaceVoyage> type_place_voyages = (List<TypePlaceVoyage>) DB.getAll(new TypePlaceVoyage(), connection);

            // Ajouter la liste des type_place_voyages au modèle
            model.addAttribute("type_place_voyages", type_place_voyages);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des type_place_voyages: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/type_place_voyage/listeTypePlaceVoyage";
    }
}
