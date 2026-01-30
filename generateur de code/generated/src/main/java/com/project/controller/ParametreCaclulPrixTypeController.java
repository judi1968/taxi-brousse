package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.ParametreCaclulPrixTypeDTO;
import com.project.model.table.ParametreCaclulPrixType;
import com.project.model.table.TypeClient;
import com.project.model.table.TypeClient;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class ParametreCaclulPrixTypeController {

    @GetMapping("/creationParametreCaclulPrixType")
    public String goToCreate(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();


            // Récupérer la liste des type_clients
            List<TypeClient> type_clients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);
            model.addAttribute("type_clients", type_clients);

            // Initialiser un DTO vide pour le formulaire
            model.addAttribute("parametre_caclul_prix_typeDTO", new ParametreCaclulPrixTypeDTO());

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
        return "pages/parametre_caclul_prix_type/creation";
    }

    @PostMapping("/saveParametreCaclulPrixType")
    public String saveParametreCaclulPrixType(@ModelAttribute ParametreCaclulPrixTypeDTO parametre_caclul_prix_typeDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet ParametreCaclulPrixType à partir du DTO
            ParametreCaclulPrixType parametre_caclul_prix_type = new ParametreCaclulPrixType();
            
            // Récupération de l'objet TypeClient
            if (parametre_caclul_prix_typeDTO.getTypeClientId() != 0) {
                TypeClient reference_type_client = TypeClient.getById(parametre_caclul_prix_typeDTO.getTypeClientId(), connection);
                parametre_caclul_prix_type.setTypeClient(reference_type_client);
            }
            
            // Récupération de l'objet TypeClient
            if (parametre_caclul_prix_typeDTO.getTypeClientId() != 0) {
                TypeClient object_type_client = TypeClient.getById(parametre_caclul_prix_typeDTO.getTypeClientId(), connection);
                parametre_caclul_prix_type.setTypeClient(object_type_client);
            }
            parametre_caclul_prix_type.setPourcentage(parametre_caclul_prix_typeDTO.getPourcentage());
            parametre_caclul_prix_type.setSigne(parametre_caclul_prix_typeDTO.getSigne());
            
            // Sauvegarder dans la base de données
            DB.save(parametre_caclul_prix_type, connection);
            
            // Message de succès
            model.addAttribute("success", "ParametreCaclulPrixType enregistré avec succès !");
            model.addAttribute("parametre_caclul_prix_typeDTO", new ParametreCaclulPrixTypeDTO()); // Réinitialiser le formulaire
            
            // Recharger les listes pour les foreign keys
            List<TypeClient> type_clients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);
            model.addAttribute("type_clients", type_clients);
            List<TypeClient> type_clients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);
            model.addAttribute("type_clients", type_clients);
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("parametre_caclul_prix_typeDTO", parametre_caclul_prix_typeDTO); // Garder les données saisies
            
            // Recharger les listes en cas d'erreur
            try {
                List<TypeClient> type_clients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);
                model.addAttribute("type_clients", type_clients);
                List<TypeClient> type_clients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);
                model.addAttribute("type_clients", type_clients);
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
        
        return "pages/parametre_caclul_prix_type/creation";
    }

    @GetMapping("/listeParametreCaclulPrixType")
    public String listeParametreCaclulPrixType(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des parametre_caclul_prix_types depuis la base
            List<ParametreCaclulPrixType> parametre_caclul_prix_types = (List<ParametreCaclulPrixType>) DB.getAll(new ParametreCaclulPrixType(), connection);

            // Ajouter la liste des parametre_caclul_prix_types au modèle
            model.addAttribute("parametre_caclul_prix_types", parametre_caclul_prix_types);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des parametre_caclul_prix_types: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/parametre_caclul_prix_type/listeParametreCaclulPrixType";
    }
}
