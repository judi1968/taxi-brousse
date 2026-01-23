package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.AchatClientDTO;
import com.project.model.table.AchatClient;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class AchatClientController {

    @PostMapping("/saveAchatClient")
    public String saveAchatClient(@ModelAttribute AchatClientDTO achat_clientDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet AchatClient à partir du DTO
            AchatClient achat_client = new AchatClient();
            
            // Récupération de l'objet TypeClient
            if (achat_clientDTO.getTypeClientId() != null) {
                TypeClient type_client = TypeClient.getById(achat_clientDTO.getTypeClientId(), connection);
                achat_client.setTypeClient(type_client);
            }
            
            // Récupération de l'objet TypePlaceVoyage
            if (achat_clientDTO.getTypePlaceVoyageId() != null) {
                TypePlaceVoyage type_place_voyage = TypePlaceVoyage.getById(achat_clientDTO.getTypePlaceVoyageId(), connection);
                achat_client.setTypePlaceVoyage(type_place_voyage);
            }
            
            // Sauvegarder dans la base de données
            DB.save(achat_client, connection);
            
            // Message de succès
            model.addAttribute("success", "AchatClient enregistré avec succès !");
            model.addAttribute("achat_clientDTO", new AchatClientDTO()); // Réinitialiser le formulaire
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("achat_clientDTO", achat_clientDTO); // Garder les données saisies
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/achat_client/creation";
    }

    @GetMapping("/listeAchatClient")
    public String listeAchatClient(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des achat_clients depuis la base
            List<AchatClient> achat_clients = (List<AchatClient>) DB.getAll(new AchatClient(), connection);

            // Ajouter la liste des achat_clients au modèle
            model.addAttribute("achat_clients", achat_clients);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des achat_clients: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/achat_client/listeAchatClient";
    }
}
