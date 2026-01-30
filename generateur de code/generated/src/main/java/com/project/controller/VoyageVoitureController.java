package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.VoyageVoitureDTO;
import com.project.model.table.VoyageVoiture;
import com.project.model.table.Voiture;
import com.project.model.table.Voyage;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class VoyageVoitureController {

    @GetMapping("/creationVoyageVoiture")
    public String goToCreate(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();


            // Récupérer la liste des voitures
            List<Voiture> voitures = (List<Voiture>) DB.getAll(new Voiture(), connection);
            model.addAttribute("voitures", voitures);

            // Récupérer la liste des voyages
            List<Voyage> voyages = (List<Voyage>) DB.getAll(new Voyage(), connection);
            model.addAttribute("voyages", voyages);

            // Initialiser un DTO vide pour le formulaire
            model.addAttribute("voyage_voitureDTO", new VoyageVoitureDTO());

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
        return "pages/voyage_voiture/creation";
    }

    @PostMapping("/saveVoyageVoiture")
    public String saveVoyageVoiture(@ModelAttribute VoyageVoitureDTO voyage_voitureDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet VoyageVoiture à partir du DTO
            VoyageVoiture voyage_voiture = new VoyageVoiture();
            
            // Récupération de l'objet Voiture
            if (voyage_voitureDTO.getVoitureId() != 0) {
                Voiture voiture = Voiture.getById(voyage_voitureDTO.getVoitureId(), connection);
                voyage_voiture.setVoiture(voiture);
            }
            
            // Récupération de l'objet Voyage
            if (voyage_voitureDTO.getVoyageId() != 0) {
                Voyage voyage = Voyage.getById(voyage_voitureDTO.getVoyageId(), connection);
                voyage_voiture.setVoyage(voyage);
            }
            
            // Sauvegarder dans la base de données
            DB.save(voyage_voiture, connection);
            
            // Message de succès
            model.addAttribute("success", "VoyageVoiture enregistré avec succès !");
            model.addAttribute("voyage_voitureDTO", new VoyageVoitureDTO()); // Réinitialiser le formulaire
            
            // Recharger les listes pour les foreign keys
            List<Voiture> voitures = (List<Voiture>) DB.getAll(new Voiture(), connection);
            model.addAttribute("voitures", voitures);
            List<Voyage> voyages = (List<Voyage>) DB.getAll(new Voyage(), connection);
            model.addAttribute("voyages", voyages);
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("voyage_voitureDTO", voyage_voitureDTO); // Garder les données saisies
            
            // Recharger les listes en cas d'erreur
            try {
                List<Voiture> voitures = (List<Voiture>) DB.getAll(new Voiture(), connection);
                model.addAttribute("voitures", voitures);
                List<Voyage> voyages = (List<Voyage>) DB.getAll(new Voyage(), connection);
                model.addAttribute("voyages", voyages);
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
        
        return "pages/voyage_voiture/creation";
    }

    @GetMapping("/listeVoyageVoiture")
    public String listeVoyageVoiture(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des voyage_voitures depuis la base
            List<VoyageVoiture> voyage_voitures = (List<VoyageVoiture>) DB.getAll(new VoyageVoiture(), connection);

            // Ajouter la liste des voyage_voitures au modèle
            model.addAttribute("voyage_voitures", voyage_voitures);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des voyage_voitures: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/voyage_voiture/listeVoyageVoiture";
    }
}
