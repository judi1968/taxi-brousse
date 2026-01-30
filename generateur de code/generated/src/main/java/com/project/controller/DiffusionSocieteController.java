package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.DiffusionSocieteDTO;
import com.project.model.table.DiffusionSociete;
import com.project.model.table.Societe;
import com.project.model.table.VoyageVoiture;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class DiffusionSocieteController {

    @GetMapping("/creationDiffusionSociete")
    public String goToCreate(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();


            // Récupérer la liste des societes
            List<Societe> societes = (List<Societe>) DB.getAll(new Societe(), connection);
            model.addAttribute("societes", societes);

            // Récupérer la liste des voyage_voitures
            List<VoyageVoiture> voyage_voitures = (List<VoyageVoiture>) DB.getAll(new VoyageVoiture(), connection);
            model.addAttribute("voyage_voitures", voyage_voitures);

            // Initialiser un DTO vide pour le formulaire
            model.addAttribute("diffusion_societeDTO", new DiffusionSocieteDTO());

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
        return "pages/diffusion_societe/creation";
    }

    @PostMapping("/saveDiffusionSociete")
    public String saveDiffusionSociete(@ModelAttribute DiffusionSocieteDTO diffusion_societeDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet DiffusionSociete à partir du DTO
            DiffusionSociete diffusion_societe = new DiffusionSociete();
            
            // Récupération de l'objet Societe
            if (diffusion_societeDTO.getSocieteId() != 0) {
                Societe societe = Societe.getById(diffusion_societeDTO.getSocieteId(), connection);
                diffusion_societe.setSociete(societe);
            }
            diffusion_societe.setDateDiffusion(diffusion_societeDTO.getDateDiffusion());
            diffusion_societe.setNombrePub(diffusion_societeDTO.getNombrePub());
            
            // Récupération de l'objet VoyageVoiture
            if (diffusion_societeDTO.getVoyageVoitureId() != 0) {
                VoyageVoiture voyage_voiture = VoyageVoiture.getById(diffusion_societeDTO.getVoyageVoitureId(), connection);
                diffusion_societe.setVoyageVoiture(voyage_voiture);
            }
            
            // Sauvegarder dans la base de données
            DB.save(diffusion_societe, connection);
            
            // Message de succès
            model.addAttribute("success", "DiffusionSociete enregistré avec succès !");
            model.addAttribute("diffusion_societeDTO", new DiffusionSocieteDTO()); // Réinitialiser le formulaire
            
            // Recharger les listes pour les foreign keys
            List<Societe> societes = (List<Societe>) DB.getAll(new Societe(), connection);
            model.addAttribute("societes", societes);
            List<VoyageVoiture> voyage_voitures = (List<VoyageVoiture>) DB.getAll(new VoyageVoiture(), connection);
            model.addAttribute("voyage_voitures", voyage_voitures);
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("diffusion_societeDTO", diffusion_societeDTO); // Garder les données saisies
            
            // Recharger les listes en cas d'erreur
            try {
                List<Societe> societes = (List<Societe>) DB.getAll(new Societe(), connection);
                model.addAttribute("societes", societes);
                List<VoyageVoiture> voyage_voitures = (List<VoyageVoiture>) DB.getAll(new VoyageVoiture(), connection);
                model.addAttribute("voyage_voitures", voyage_voitures);
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
        
        return "pages/diffusion_societe/creation";
    }

    @GetMapping("/listeDiffusionSociete")
    public String listeDiffusionSociete(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des diffusion_societes depuis la base
            List<DiffusionSociete> diffusion_societes = (List<DiffusionSociete>) DB.getAll(new DiffusionSociete(), connection);

            // Ajouter la liste des diffusion_societes au modèle
            model.addAttribute("diffusion_societes", diffusion_societes);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des diffusion_societes: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/diffusion_societe/listeDiffusionSociete";
    }
}
