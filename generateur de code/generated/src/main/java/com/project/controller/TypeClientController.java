package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.TypeClientDTO;
import com.project.model.table.TypeClient;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class TypeClientController {

    @PostMapping("/saveTypeClient")
    public String saveTypeClient(@ModelAttribute TypeClientDTO type_clientDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet TypeClient à partir du DTO
            TypeClient type_client = new TypeClient();
            type_client.setNom(type_clientDTO.getNom());
            
            // Sauvegarder dans la base de données
            DB.save(type_client, connection);
            
            // Message de succès
            model.addAttribute("success", "TypeClient enregistré avec succès !");
            model.addAttribute("type_clientDTO", new TypeClientDTO()); // Réinitialiser le formulaire
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("type_clientDTO", type_clientDTO); // Garder les données saisies
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/type_client/creation";
    }

    @GetMapping("/listeTypeClient")
    public String listeTypeClient(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des type_clients depuis la base
            List<TypeClient> type_clients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);

            // Ajouter la liste des type_clients au modèle
            model.addAttribute("type_clients", type_clients);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des type_clients: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/type_client/listeTypeClient";
    }
}
