package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.AchatProduitDTO;
import com.project.model.table.AchatProduit;
import com.project.model.table.Produit;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class AchatProduitController {

    @GetMapping("/creationAchatProduit")
    public String goToCreate(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();


            // Récupérer la liste des produits
            List<Produit> produits = (List<Produit>) DB.getAll(new Produit(), connection);
            model.addAttribute("produits", produits);

            // Initialiser un DTO vide pour le formulaire
            model.addAttribute("achat_produitDTO", new AchatProduitDTO());

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
        return "pages/achat_produit/creation";
    }

    @PostMapping("/saveAchatProduit")
    public String saveAchatProduit(@ModelAttribute AchatProduitDTO achat_produitDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet AchatProduit à partir du DTO
            AchatProduit achat_produit = new AchatProduit();
            
            // Récupération de l'objet Produit
            if (achat_produitDTO.getProduitId() != 0) {
                Produit produit = Produit.getById(achat_produitDTO.getProduitId(), connection);
                achat_produit.setProduit(produit);
            }
            achat_produit.setQuantite(achat_produitDTO.getQuantite());
            achat_produit.setDateAchat(achat_produitDTO.getDateAchat());
            
            // Sauvegarder dans la base de données
            DB.save(achat_produit, connection);
            
            // Message de succès
            model.addAttribute("success", "AchatProduit enregistré avec succès !");
            model.addAttribute("achat_produitDTO", new AchatProduitDTO()); // Réinitialiser le formulaire
            
            // Recharger les listes pour les foreign keys
            List<Produit> produits = (List<Produit>) DB.getAll(new Produit(), connection);
            model.addAttribute("produits", produits);
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("achat_produitDTO", achat_produitDTO); // Garder les données saisies
            
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
        
        return "pages/achat_produit/creation";
    }

    @GetMapping("/listeAchatProduit")
    public String listeAchatProduit(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des achat_produits depuis la base
            List<AchatProduit> achat_produits = (List<AchatProduit>) DB.getAll(new AchatProduit(), connection);

            // Ajouter la liste des achat_produits au modèle
            model.addAttribute("achat_produits", achat_produits);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des achat_produits: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/achat_produit/listeAchatProduit";
    }
}
