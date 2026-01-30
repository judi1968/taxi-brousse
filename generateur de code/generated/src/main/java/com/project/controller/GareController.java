package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.GareDTO;
import com.project.model.table.Gare;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class GareController {

    @GetMapping("/creationGare")
    public String goToCreate(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();


            // Aucune donnée à charger

            // Initialiser un DTO vide pour le formulaire
            model.addAttribute("gareDTO", new GareDTO());

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
        return "pages/gare/creation";
    }

    @PostMapping("/saveGare")
    public String saveGare(@ModelAttribute GareDTO gareDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet Gare à partir du DTO
            Gare gare = new Gare();
            gare.setNom(gareDTO.getNom());
            
            // Sauvegarder dans la base de données
            DB.save(gare, connection);
            
            // Message de succès
            model.addAttribute("success", "Gare enregistré avec succès !");
            model.addAttribute("gareDTO", new GareDTO()); // Réinitialiser le formulaire
            
            // Recharger les listes pour les foreign keys
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("gareDTO", gareDTO); // Garder les données saisies
            
            // Recharger les listes en cas d'erreur
            try {
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
        
        return "pages/gare/creation";
    }

    @GetMapping("/listeGare")
    public String listeGare(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des gares depuis la base
            List<Gare> gares = (List<Gare>) DB.getAll(new Gare(), connection);

            // Ajouter la liste des gares au modèle
            model.addAttribute("gares", gares);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des gares: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/gare/listeGare";
    }
}
