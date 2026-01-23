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
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class DiffusionSocieteController {

    @PostMapping("/saveDiffusionSociete")
    public String saveDiffusionSociete(@ModelAttribute DiffusionSocieteDTO diffusion_societeDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet DiffusionSociete à partir du DTO
            DiffusionSociete diffusion_societe = new DiffusionSociete();
            
            // Récupération de l'objet Societe
            if (diffusion_societeDTO.getSocieteId() != null) {
                Societe societe = Societe.getById(diffusion_societeDTO.getSocieteId(), connection);
                diffusion_societe.setSociete(societe);
            }
            diffusion_societe.setDateDiffusion(diffusion_societeDTO.getDateDiffusion());
            diffusion_societe.setNombrePub(diffusion_societeDTO.getNombrePub());
            
            // Sauvegarder dans la base de données
            DB.save(diffusion_societe, connection);
            
            // Message de succès
            model.addAttribute("success", "DiffusionSociete enregistré avec succès !");
            model.addAttribute("diffusion_societeDTO", new DiffusionSocieteDTO()); // Réinitialiser le formulaire
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("diffusion_societeDTO", diffusion_societeDTO); // Garder les données saisies
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
