package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.MouvementPrixProduitDTO;
import com.project.model.table.MouvementPrixProduit;
import com.project.model.table.Produit;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class MouvementPrixProduitController {

    @GetMapping("/creationMouvementPrixProduit")
    public String goToCreate(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();


            // Récupérer la liste des produits
            List<Produit> produits = (List<Produit>) DB.getAll(new Produit(), connection);
            model.addAttribute("produits", produits);

            // Initialiser un DTO vide pour le formulaire
            model.addAttribute("mouvement_prix_produitDTO", new MouvementPrixProduitDTO());

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
        return "pages/mouvement_prix_produit/creation";
    }

    @PostMapping("/saveMouvementPrixProduit")
    public String saveMouvementPrixProduit(@ModelAttribute MouvementPrixProduitDTO mouvement_prix_produitDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet MouvementPrixProduit à partir du DTO
            MouvementPrixProduit mouvement_prix_produit = new MouvementPrixProduit();
            
            // Récupération de l'objet Produit
            if (mouvement_prix_produitDTO.getProduitId() != 0) {
                Produit produit = Produit.getById(mouvement_prix_produitDTO.getProduitId(), connection);
                mouvement_prix_produit.setProduit(produit);
            }
            mouvement_prix_produit.setMontant(mouvement_prix_produitDTO.getMontant());
            mouvement_prix_produit.setDateMouvement(mouvement_prix_produitDTO.getDateMouvement());
            
            // Sauvegarder dans la base de données
            DB.save(mouvement_prix_produit, connection);
            
            // Message de succès
            model.addAttribute("success", "MouvementPrixProduit enregistré avec succès !");
            model.addAttribute("mouvement_prix_produitDTO", new MouvementPrixProduitDTO()); // Réinitialiser le formulaire
            
            // Recharger les listes pour les foreign keys
            List<Produit> produits = (List<Produit>) DB.getAll(new Produit(), connection);
            model.addAttribute("produits", produits);
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("mouvement_prix_produitDTO", mouvement_prix_produitDTO); // Garder les données saisies
            
            // Recharger les listes en cas d'erreur
            try {
                List<Produit> produits = (List<Produit>) DB.getAll(new Produit(), connection);
                model.addAttribute("produits", produits);
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
        
        return "pages/mouvement_prix_produit/creation";
    }

    @GetMapping("/listeMouvementPrixProduit")
    public String listeMouvementPrixProduit(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des mouvement_prix_produits depuis la base
            List<MouvementPrixProduit> mouvement_prix_produits = (List<MouvementPrixProduit>) DB.getAll(new MouvementPrixProduit(), connection);

            // Ajouter la liste des mouvement_prix_produits au modèle
            model.addAttribute("mouvement_prix_produits", mouvement_prix_produits);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des mouvement_prix_produits: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/mouvement_prix_produit/listeMouvementPrixProduit";
    }
}
