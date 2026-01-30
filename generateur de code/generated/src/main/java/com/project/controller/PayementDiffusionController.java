package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.PayementDiffusionDTO;
import com.project.model.table.PayementDiffusion;
import com.project.model.table.DiffusionSociete;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class PayementDiffusionController {

    @GetMapping("/creationPayementDiffusion")
    public String goToCreate(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();


            // Récupérer la liste des diffusion_societes
            List<DiffusionSociete> diffusion_societes = (List<DiffusionSociete>) DB.getAll(new DiffusionSociete(), connection);
            model.addAttribute("diffusion_societes", diffusion_societes);

            // Initialiser un DTO vide pour le formulaire
            model.addAttribute("payement_diffusionDTO", new PayementDiffusionDTO());

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
        return "pages/payement_diffusion/creation";
    }

    @PostMapping("/savePayementDiffusion")
    public String savePayementDiffusion(@ModelAttribute PayementDiffusionDTO payement_diffusionDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet PayementDiffusion à partir du DTO
            PayementDiffusion payement_diffusion = new PayementDiffusion();
            
            // Récupération de l'objet DiffusionSociete
            if (payement_diffusionDTO.getDiffusionSocieteId() != 0) {
                DiffusionSociete societe_diffusion = DiffusionSociete.getById(payement_diffusionDTO.getDiffusionSocieteId(), connection);
                payement_diffusion.setDiffusionSociete(societe_diffusion);
            }
            payement_diffusion.setMontant(payement_diffusionDTO.getMontant());
            payement_diffusion.setDatePayement(payement_diffusionDTO.getDatePayement());
            
            // Sauvegarder dans la base de données
            DB.save(payement_diffusion, connection);
            
            // Message de succès
            model.addAttribute("success", "PayementDiffusion enregistré avec succès !");
            model.addAttribute("payement_diffusionDTO", new PayementDiffusionDTO()); // Réinitialiser le formulaire
            
            // Recharger les listes pour les foreign keys
            List<DiffusionSociete> diffusion_societes = (List<DiffusionSociete>) DB.getAll(new DiffusionSociete(), connection);
            model.addAttribute("diffusion_societes", diffusion_societes);
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("payement_diffusionDTO", payement_diffusionDTO); // Garder les données saisies
            
            // Recharger les listes en cas d'erreur
            try {
                List<DiffusionSociete> diffusion_societes = (List<DiffusionSociete>) DB.getAll(new DiffusionSociete(), connection);
                model.addAttribute("diffusion_societes", diffusion_societes);
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
        
        return "pages/payement_diffusion/creation";
    }

    @GetMapping("/listePayementDiffusion")
    public String listePayementDiffusion(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des payement_diffusions depuis la base
            List<PayementDiffusion> payement_diffusions = (List<PayementDiffusion>) DB.getAll(new PayementDiffusion(), connection);

            // Ajouter la liste des payement_diffusions au modèle
            model.addAttribute("payement_diffusions", payement_diffusions);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des payement_diffusions: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/payement_diffusion/listePayementDiffusion";
    }
}
