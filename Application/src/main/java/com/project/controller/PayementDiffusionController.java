package com.project.controller;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.PayementDiffusionDTO;
import com.project.model.table.DiffusionSociete;
import com.project.model.table.MouvementPrixPub;
import com.project.model.table.ParametreCalculPrixType;
import com.project.model.table.PayementDiffusion;
import com.project.model.table.Societe;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class PayementDiffusionController {

    @PostMapping("/savePayementDiffusion")
    public String savePayementDiffusion(@ModelAttribute PayementDiffusionDTO payement_diffusionDTO, Model model) throws Exception {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            connection.setAutoCommit(false);
            // Créer l'objet PayementDiffusion à partir du DTO
            
            // Récupération de l'objet DiffusionSociete
            Societe societe = null;
            Date date = null; 
            if ((Integer)payement_diffusionDTO.getSocieteId() != null) {
                // System.out.println("ici" + " " + payement_diffusionDTO.getSocieteId());
                societe = (Societe) DB.getById(new Societe(),payement_diffusionDTO.getSocieteId(), connection);
            }
            if (payement_diffusionDTO.getDatePayement() != null && !payement_diffusionDTO.getDatePayement().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                date = sdf.parse(payement_diffusionDTO.getDatePayement()+"T00:00");
            }
            
            List<DiffusionSociete> diffusionSocietes = (List<DiffusionSociete>) DB.getAllWhere(new DiffusionSociete(), " id_societe = "+societe.getId(), connection);
            System.out.println(diffusionSocietes.size()+" size ");
            double montantTotale = 0;
            List<MouvementPrixPub> mouvementPrixPubs = (List<MouvementPrixPub>) DB.getAll(new MouvementPrixPub(), connection);
            for (DiffusionSociete diffusionSociete : diffusionSocietes) {
                montantTotale += diffusionSociete.getNombrePub()*mouvementPrixPubs.get(0).getMontant();
            }
            
            for (DiffusionSociete diffusionSociete : diffusionSocietes) {
                double pourcentage = ((diffusionSociete.getNombrePub()*mouvementPrixPubs.get(0).getMontant()) * 100)/montantTotale;
                double montantAPayer = (payement_diffusionDTO.getMontant() * pourcentage)/100;
                PayementDiffusion payement_diffusion = new PayementDiffusion();
                payement_diffusion.setMontant(montantAPayer);
                payement_diffusion.setDatePayement(date);
                payement_diffusion.setSociete_diffusion(diffusionSociete);
                DB.save(payement_diffusion, connection);
            }

            // Sauvegarder dans la base de données
            // DB.save(payement_diffusion, connection);
            // System.out.println(payement_diffusion.getSociete().getId()+" societe diffusion");
            // Message de succès
            model.addAttribute("success", "PayementDiffusion enregistré avec succès !");
            model.addAttribute("payement_diffusionDTO", new PayementDiffusionDTO()); // Réinitialiser le formulaire
            List<Societe> societes = (List<Societe>) DB.getAll(new Societe(),
                    connection);

            // Ajouter au modèle
            model.addAttribute("societes", societes);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("payement_diffusionDTO", payement_diffusionDTO); // Garder les données saisies
            if (connection!=null) {
                connection.rollback();
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
        
        return "pages/payement_diffusion/creationPayementDiffusion";
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
