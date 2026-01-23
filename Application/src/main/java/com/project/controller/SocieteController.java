package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.SocieteDTO;
import com.project.model.table.Societe;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class SocieteController {

    @PostMapping("/saveSociete")
    public String saveSociete(@ModelAttribute SocieteDTO societeDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet Societe à partir du DTO
            Societe societe = new Societe();
            societe.setNom(societeDTO.getNom());
            
            // Sauvegarder dans la base de données
            DB.save(societe, connection);
            
            // Message de succès
            model.addAttribute("success", "Societe enregistré avec succès !");
            model.addAttribute("societeDTO", new SocieteDTO()); // Réinitialiser le formulaire
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("societeDTO", societeDTO); // Garder les données saisies
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/societe/creationSociete";
    }

    @GetMapping("/listeSociete")
    public String listeSociete(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des societes depuis la base
            List<Societe> societes = (List<Societe>) DB.getAll(new Societe(), connection);

            // Ajouter la liste des societes au modèle
            model.addAttribute("societes", societes);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des societes: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/societe/listeSociete";
    }
}
