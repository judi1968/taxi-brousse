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
import com.project.model.table.TypePlace;
import com.project.model.table.Voyage;
import com.project.model.table.TypeClient;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class PrixTypePlaceVoyageController {

    @GetMapping("/creationPrixTypePlaceVoyage")
    public String goToCreate(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();


            // Récupérer la liste des type_places
            List<TypePlace> type_places = (List<TypePlace>) DB.getAll(new TypePlace(), connection);
            model.addAttribute("type_places", type_places);

            // Récupérer la liste des voyages
            List<Voyage> voyages = (List<Voyage>) DB.getAll(new Voyage(), connection);
            model.addAttribute("voyages", voyages);

            // Récupérer la liste des type_clients
            List<TypeClient> type_clients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);
            model.addAttribute("type_clients", type_clients);

            // Initialiser un DTO vide pour le formulaire
            model.addAttribute("prix_type_place_voyageDTO", new PrixTypePlaceVoyageDTO());

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
        return "pages/prix_type_place_voyage/creation";
    }

    @PostMapping("/savePrixTypePlaceVoyage")
    public String savePrixTypePlaceVoyage(@ModelAttribute PrixTypePlaceVoyageDTO prix_type_place_voyageDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet PrixTypePlaceVoyage à partir du DTO
            PrixTypePlaceVoyage prix_type_place_voyage = new PrixTypePlaceVoyage();
            
            // Récupération de l'objet TypePlace
            if (prix_type_place_voyageDTO.getTypePlaceId() != 0) {
                TypePlace type_place = TypePlace.getById(prix_type_place_voyageDTO.getTypePlaceId(), connection);
                prix_type_place_voyage.setTypePlace(type_place);
            }
            
            // Récupération de l'objet Voyage
            if (prix_type_place_voyageDTO.getVoyageId() != 0) {
                Voyage voyage = Voyage.getById(prix_type_place_voyageDTO.getVoyageId(), connection);
                prix_type_place_voyage.setVoyage(voyage);
            }
            prix_type_place_voyage.setMontant(prix_type_place_voyageDTO.getMontant());
            
            // Récupération de l'objet TypeClient
            if (prix_type_place_voyageDTO.getTypeClientId() != 0) {
                TypeClient type_client = TypeClient.getById(prix_type_place_voyageDTO.getTypeClientId(), connection);
                prix_type_place_voyage.setTypeClient(type_client);
            }
            
            // Sauvegarder dans la base de données
            DB.save(prix_type_place_voyage, connection);
            
            // Message de succès
            model.addAttribute("success", "PrixTypePlaceVoyage enregistré avec succès !");
            model.addAttribute("prix_type_place_voyageDTO", new PrixTypePlaceVoyageDTO()); // Réinitialiser le formulaire
            
            // Recharger les listes pour les foreign keys
            List<TypePlace> type_places = (List<TypePlace>) DB.getAll(new TypePlace(), connection);
            model.addAttribute("type_places", type_places);
            List<Voyage> voyages = (List<Voyage>) DB.getAll(new Voyage(), connection);
            model.addAttribute("voyages", voyages);
            List<TypeClient> type_clients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);
            model.addAttribute("type_clients", type_clients);
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("prix_type_place_voyageDTO", prix_type_place_voyageDTO); // Garder les données saisies
            
            // Recharger les listes en cas d'erreur
            try {
                List<TypePlace> type_places = (List<TypePlace>) DB.getAll(new TypePlace(), connection);
                model.addAttribute("type_places", type_places);
                List<Voyage> voyages = (List<Voyage>) DB.getAll(new Voyage(), connection);
                model.addAttribute("voyages", voyages);
                List<TypeClient> type_clients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);
                model.addAttribute("type_clients", type_clients);
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
