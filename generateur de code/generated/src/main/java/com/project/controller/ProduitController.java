package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.ProduitDTO;
import com.project.model.table.Produit;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class ProduitController {

    @GetMapping("/creationProduit")
    public String goToCreate(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();


            // Aucune donnée à charger

            // Initialiser un DTO vide pour le formulaire
            model.addAttribute("produitDTO", new ProduitDTO());

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
        return "pages/produit/creation";
    }

    @PostMapping("/saveProduit")
    public String saveProduit(@ModelAttribute ProduitDTO produitDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet Produit à partir du DTO
            Produit produit = new Produit();
            produit.setNom(produitDTO.getNom());
            
            // Sauvegarder dans la base de données
            DB.save(produit, connection);
            
            // Message de succès
            model.addAttribute("success", "Produit enregistré avec succès !");
            model.addAttribute("produitDTO", new ProduitDTO()); // Réinitialiser le formulaire
            
            // Recharger les listes pour les foreign keys
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("produitDTO", produitDTO); // Garder les données saisies
            
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
        
        return "pages/produit/creation";
    }

    @GetMapping("/listeProduit")
    public String listeProduit(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des produits depuis la base
            List<Produit> produits = (List<Produit>) DB.getAll(new Produit(), connection);

            // Ajouter la liste des produits au modèle
            model.addAttribute("produits", produits);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des produits: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/produit/listeProduit";
    }
}
