package com.project.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.configuration.DatabaseConfigProperties;
import com.project.dto.AchatClientDTO;
import com.project.dto.FiltreCaDiffusion;
import com.project.dto.MouvementPrixPubDTO;
import com.project.dto.PrixTypePlaceVoyageDTO;
import com.project.dto.TypeClientDTO;
import com.project.dto.TypePlaceDTO;
import com.project.dto.TypePlaceVoyageDTO;
import com.project.model.table.DiffusionSociete;
import com.project.model.table.PlaceVoiture;
import com.project.model.table.Societe;
import com.project.model.table.TypeClient;
import com.project.model.table.TypePlace;
import com.project.model.table.TypePlaceVoyage;
import com.project.model.table.Voiture;
import com.project.model.table.Voyage;
import com.project.model.table.VoyageVoiture;
import com.project.model.view.CAProduit;
import com.project.model.view.DiffusionSocieteCA;
import com.project.model.view.VCaAchatProduit;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class RooterController {

    @Autowired
    private DatabaseConfigProperties dbConfig;

    @GetMapping("/home")
    public String home() throws Exception {
        System.out.println("Database Config: " + dbConfig.toString());

        System.out.println("MyConnection values:");
        System.out.println("Host: " + MyConnection.getIp());
        System.out.println("Port: " + MyConnection.getPort());
        System.out.println("Database: " + MyConnection.getDatabaseName());
        System.out.println("Username: " + MyConnection.getUserName());
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            Voiture fiara = new Voiture();
            fiara.setId(2);
            fiara.setNom("BMWx");
            fiara.setNumero("TDI8842");
            // DB.save(fiara, connection);

            PlaceVoiture placeVoiture = (PlaceVoiture) DB.getById(new PlaceVoiture(), 1, connection);
            placeVoiture.setVoiture(fiara);
            DB.save(placeVoiture, connection);
            // List<PlaceVoiture> voitures = (List<PlaceVoiture>) DB.getAll(new
            // PlaceVoiture(), connection);
            List<PlaceVoiture> voitures = (List<PlaceVoiture>) DB.getAllOrderAndLimitAndWhere(new PlaceVoiture(),
                    "numero = '12'", "id DESC, numero ASC", 6, connection);
            for (PlaceVoiture voiture : voitures) {
                System.out
                        .println(voiture.getId() + " : " + voiture.getVoiture().getNom() + " : " + voiture.getNumero());
            }
            if (connection == null) {
                System.out.println("arakzany");
            } else {
                System.out.println("mety io eeh ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (connection != null) {
            connection.close();
        }

        return "home";
    }

    @GetMapping("/form")
    public String form() {
        return "pages/form";
    }

    @GetMapping("/table")
    public String table() {
        return "pages/table";
    }

    @GetMapping("/log-out")
    public String logout() {
        return "pages/login";
    }

    @GetMapping("/")
    public String creationVoiture() {
        return "pages/voiture/creation";
    }

    @GetMapping("/place")
    public String creationPlaceVoiture(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des voitures depuis la base
            List<Voiture> voitures = (List<Voiture>) DB.getAll(new Voiture(), connection);

            // Ajouter la liste des voitures au modèle
            model.addAttribute("voitures", voitures);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des voitures: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/voiture/creationPlace";
    }

    @GetMapping("/voyage")
    public String creationVoyage() {
        return "pages/voiture/creationVoyage";
    }

    @GetMapping("/VoitureVoyage")
    public String asignationVoitureVoyage(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer les listes des voitures et voyages
            List<Voiture> voitures = (List<Voiture>) DB.getAll(new Voiture(), connection);
            List<Voyage> voyages = (List<Voyage>) DB.getAll(new Voyage(), connection);

            // Ajouter les listes au modèle
            model.addAttribute("voitures", voitures);
            model.addAttribute("voyages", voyages);

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
        return "pages/voiture/voitureVoyage";
    }

    @GetMapping("/listeVoitureParVoyage")
    public String listeVoitureVoyage(   @RequestParam(required = false) String moisAnne,Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            boolean dateExist = true;
            if (moisAnne == null) {
                dateExist = false;
            }else{
                if (moisAnne.trim().length() == 0) {
                    dateExist = false;
                }
            }

            if (!dateExist) {
                
            double caTotale = 0;
            // Récupérer la liste des associations voyage-voiture
            List<VoyageVoiture> voyageVoitures = (List<VoyageVoiture>) DB.getAll(new VoyageVoiture(), connection);
            for (int i = 0; i < voyageVoitures.size(); i++) {
                voyageVoitures.get(i).calculPrixMaximum(connection);
                voyageVoitures.get(i).calculCAPub(connection);
                voyageVoitures.get(i).calculCA(connection);
                voyageVoitures.get(i).calculMontantTotaleCA(connection);
                voyageVoitures.get(i).calculCAPubTotaleAPayer(connection);
                voyageVoitures.get(i).calculResteCAPubAPayer();
                caTotale += voyageVoitures.get(i).getPrixMaximum();
                caTotale += voyageVoitures.get(i).getMontantCAPub();
            }

            // System.out.println(moisAnne);
 
            List<VCaAchatProduit> vAchatProduits = (List<VCaAchatProduit>) DB.getAll(new VCaAchatProduit() , connection);
            List<CAProduit> caProduits = CAProduit.getAllCAProduit(vAchatProduits, connection);
            String voyageVoituresTab = DB.getTableau(voyageVoitures, new VoyageVoiture(),"Liste de voiture par voyages", "Voitures assignées à chaque voyage");
            String caProduitTab = DB.getTableau(caProduits, new CAProduit(),"Achat de produit generale", "");
            String achatProduitTab = DB.getTableau(vAchatProduits, new VCaAchatProduit(),"Achat de produit detailees", "");
            for (CAProduit caProduit : caProduits) {
                caTotale += caProduit.getMontant();
            }
            

            // Ajouter la liste au modèle
            model.addAttribute("ca", caTotale+" Ar");
            model.addAttribute("voyageVoitures", voyageVoitures);
            model.addAttribute("voyageParVoitureTab", voyageVoituresTab);
            model.addAttribute("achatProduitTab", achatProduitTab);
            model.addAttribute("caProduitTab", caProduitTab);

            } else {
                int mois = Integer.parseInt(moisAnne.split("-")[1]);
                int anne = Integer.parseInt(moisAnne.split("-")[0]);
                double caTotale = 0;

                // Récupérer la liste des associations voyage-voiture
                    List<VoyageVoiture> voyageVoituresTout = (List<VoyageVoiture>) DB.getAll(new VoyageVoiture(), connection);
                    List<VoyageVoiture> voyageVoitures = new ArrayList<>();
                    for (VoyageVoiture voyageVoiture : voyageVoituresTout) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(voyageVoiture.getVoyage().getDate());
                        
                        int moisVoyage = calendar.get(Calendar.MONTH) + 1; 
                        int anneeVoyage = calendar.get(Calendar.YEAR);
                        if (moisVoyage == mois && anneeVoyage == anne) {
                            voyageVoitures.add(voyageVoiture);
                        }
                    }
                    
                    for (int i = 0; i < voyageVoitures.size(); i++) {
                        voyageVoitures.get(i).calculPrixMaximum(connection);
                        voyageVoitures.get(i).calculCAPub(connection);
                        voyageVoitures.get(i).calculCA(connection);
                        voyageVoitures.get(i).calculMontantTotaleCA(connection);
                        voyageVoitures.get(i).calculCAPubTotaleAPayer(connection);
                        voyageVoitures.get(i).calculResteCAPubAPayer();
                        caTotale += voyageVoitures.get(i).getPrixMaximum();
                        caTotale += voyageVoitures.get(i).getMontantCAPub();
                    }

                    
                    // System.out.println(moisAnne);
                    String where = " EXTRACT(MONTH FROM date_achat) = "+mois+" AND EXTRACT(YEAR FROM date_achat) = "+anne;
                    List<VCaAchatProduit> vAchatProduits = (List<VCaAchatProduit>) DB.getAllWhere(new VCaAchatProduit(), where , connection);
                    List<CAProduit> caProduits = CAProduit.getAllCAProduit(vAchatProduits, connection);
 for (CAProduit caProduit : caProduits) {
                caTotale += caProduit.getMontant();
            }
                    String voyageVoituresTab = DB.getTableau(voyageVoitures, new VoyageVoiture(),"Liste de voiture par voyages", "Voitures assignées à chaque voyage");
                    String achatProduitTab = DB.getTableau(vAchatProduits, new VCaAchatProduit(),"Achat de produit detailees", "");
                    String caProduitTab = DB.getTableau(caProduits, new CAProduit(),"Achat de produit generale", "");

                                model.addAttribute("ca", caTotale+" Ar");

                    // Ajouter la liste au modèle
                    model.addAttribute("voyageVoitures", voyageVoitures);
                    model.addAttribute("voyageParVoitureTab", voyageVoituresTab);
                    model.addAttribute("achatProduitTab", achatProduitTab);
                    model.addAttribute("caProduitTab", caProduitTab);

            }

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
        return "pages/voiture/listeParVoyage";
    }

    @GetMapping("/listeVoyage")
    public String listeVoyage(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des voyages depuis la base
            List<Voyage> voyages = (List<Voyage>) DB.getAll(new Voyage(), connection);

            // Ajouter la liste des voyages au modèle
            model.addAttribute("voyages", voyages);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des voyages: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/voiture/listeVoyage";
    }

    @GetMapping("/typePlace")
    public String modificationTypePlace() {
        return "pages/voiture/typePlaceVoyage";
    }

    @GetMapping("/ajoutPrix")
    public String ajoutPrixPourChaqueTypePlace(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer les types de place et voyages
            List<TypePlace> typePlaces = (List<TypePlace>) DB.getAll(new TypePlace(), connection);
            List<Voyage> voyages = (List<Voyage>) DB.getAll(new Voyage(), connection);
            List<TypeClient> typeClients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);

            // Ajouter au modèle
            model.addAttribute("typePlaces", typePlaces);
            model.addAttribute("typeClients", typeClients);
            model.addAttribute("voyages", voyages);
            model.addAttribute("prixDTO", new PrixTypePlaceVoyageDTO());

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
        return "pages/voiture/ajoutPrix";
    }

    @GetMapping("/creertypePlace")
    public String modificationTypePlace(Model model) {
        // Ajouter un DTO vide pour le formulaire
        model.addAttribute("typePlaceDTO", new TypePlaceDTO());
        return "pages/voiture/creerTypePlace";
    }

    @GetMapping("/ajoutTypePlaceVoyage")
    public String ajoutTypePlaceVoyage(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer toutes les données nécessaires
            List<VoyageVoiture> voyageVoitures = (List<VoyageVoiture>) DB.getAll(new VoyageVoiture(), connection);
            List<PlaceVoiture> places = (List<PlaceVoiture>) DB.getAll(new PlaceVoiture(), connection);
            List<TypePlace> typePlaces = (List<TypePlace>) DB.getAll(new TypePlace(), connection);

            // Ajouter au modèle
            model.addAttribute("voyageVoitures", voyageVoitures);
            model.addAttribute("places", places);
            model.addAttribute("typePlaces", typePlaces);
            model.addAttribute("typePlaceVoyageDTO", new TypePlaceVoyageDTO());

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
        return "pages/voiture/ajoutTypePlaceVoyage";
    }

    @GetMapping("/typeClient")
    public String creationTypeClient(Model model) {
        return "pages/voiture/ajoutTypeClient";
    }

    @GetMapping("/achatClient")
    public String creationAchatClient(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer les types de client et les associations type-place-voyage
            List<TypeClient> typeClients = (List<TypeClient>) DB.getAll(new TypeClient(), connection);
            List<TypePlaceVoyage> typePlaceVoyages = (List<TypePlaceVoyage>) DB.getAll(new TypePlaceVoyage(),
                    connection);

            // Ajouter au modèle
            model.addAttribute("typeClients", typeClients);
            model.addAttribute("typePlaceVoyages", typePlaceVoyages);

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
        return "pages/voiture/achatClient";
    }

    @GetMapping("/ajoutSociete")
    public String addSociete(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

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
        return "pages/societe/creationSociete";
    }
    @GetMapping("/ajoutPrixPub")
    public String ajoutPrixPub(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

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
        return "pages/mouvement_prix_pub/creationMouvementPrixPub";
    }

    

    @GetMapping("/ajoutDiffusion")
    public String ajoutDiffusion(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            List<VoyageVoiture> voyageVoitures = (List<VoyageVoiture>) DB.getAll(new VoyageVoiture(), connection);
             List<Societe> societes = (List<Societe>) DB.getAll(new Societe(),
                    connection);

            // Ajouter au modèle
            model.addAttribute("societes", societes);
            model.addAttribute("voyageVoitures", voyageVoitures);
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
        return "pages/diffusion_societe/creationDiffusionSociete";
    }

    @GetMapping("/payerDiffusion")
    public String payerDiffusion(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();
             List<Societe> societes = (List<Societe>) DB.getAll(new Societe(),
                    connection);

            // Ajouter au modèle
            model.addAttribute("societes", societes);
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
        return "pages/payement_diffusion/creationPayementDiffusion";
    }

    @GetMapping("/caPublicite")
    public String caPublicite(@RequestParam(required = false) String dateDebut,
    @RequestParam(required = false) String dateFin,
    @RequestParam(required = false) String moisAnne,
    Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            String where = " 1 = 1 ";
            if (moisAnne != null) {
                System.out.println(moisAnne);
            }
            // Récupérer la liste des associations voyage-voiture
            List<DiffusionSocieteCA> diffusionSocieteCAs = (List<DiffusionSocieteCA>) DB.getAllWhere(new DiffusionSocieteCA(), where ,connection);
            double montantTotale = 0;
            for (DiffusionSocieteCA diffusionSocieteCA : diffusionSocieteCAs) {
                montantTotale += diffusionSocieteCA.getMontantTotale();
            }

            String diffusionSocieteCAsTab = DB.getTableau(diffusionSocieteCAs, new DiffusionSocieteCA(),"Montant totale : " +montantTotale+"Ar", "Avec leurs CA");

            // Ajouter la liste au modèle
            model.addAttribute("caTab", diffusionSocieteCAsTab);
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
        return "pages/diffusion/ca";
    }

}