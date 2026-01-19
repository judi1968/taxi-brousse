package com.project.model.table;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.project.pja.databases.generalisation.DB;
import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.ShowTable;
import com.project.pja.databases.generalisation.annotation.TableDb;

@TableDb(name = "voyage_voiture")
public class VoyageVoiture {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;
    
    @AttributDb(name = "id_voiture")
    private Voiture voiture;
    
    @AttributDb(name = "id_voyage")
    private Voyage voyage;

    public double prixMaximum;

    public double montantCA;

    public double getMontantCA() {
        return montantCA;
    }

    public void setMontantCA(double montantCa) {
        this.montantCA = montantCa;
    }

    public double getPrixMaximum() {
        return prixMaximum;
    }

    public void setPrixMaximum(double prixMaximum) {
        this.prixMaximum = prixMaximum;
    }

    public void calculPrixMaximum(Connection connection) throws Exception{
        double prixMax = 0;
        List<PlaceVoiture> placeVoitures = (List<PlaceVoiture>) DB.getAllWhere(new PlaceVoiture(), " id_voiture = " + this.getId(), connection);
        
        // for (PlaceVoiture placeVoiture : placeVoitures) {
            List<TypePlaceVoyage> typePlaceVoyages = (List<TypePlaceVoyage>) DB.getAllWhere(new TypePlaceVoyage(), " id_voyage_voiture  = "+ this.getId(), connection);
            for (TypePlaceVoyage typePlaceVoyage : typePlaceVoyages) {
                if (typePlaceVoyages.size() > 0) {
                    List<PrixTypePlaceVoyage> prixTypePlaceVoyage = (List<PrixTypePlaceVoyage>) DB.getAllWhere(new PrixTypePlaceVoyage(), "id_voyage = "+this.getVoyage().getId()+" AND id_type_place = "+typePlaceVoyage.getTypePlace().getId()  + " and id_type_client = 1", connection);
                    if (prixTypePlaceVoyage.size() > 0) {
                        prixMax += prixTypePlaceVoyage.get(0).getMontant();             
        
                    }
                
                }
                
            }
        // } 

        this.setPrixMaximum(prixMax);
    }

    public void calculCA(Connection connection) throws Exception{
        double CA = 0;
        List<TypePlaceVoyage> typePlaceVoyages = (List<TypePlaceVoyage>) DB.getAllWhere(new TypePlaceVoyage(), " id_voyage_voiture = " + this.getId(), connection);
        List<AchatClient> achatClients = new ArrayList<>();
        for (TypePlaceVoyage typePlaceVoyage : typePlaceVoyages) {
            List<AchatClient> achatClientsByIdPlaceVoyage = (List<AchatClient>) DB.getAllWhere(new AchatClient(), " id_type_place_voyage = "+typePlaceVoyage.getId(), connection);
            if (achatClientsByIdPlaceVoyage.size() > 0) {
                achatClients.add(achatClientsByIdPlaceVoyage.get(0));
            }
        }
        for (AchatClient achatClient : achatClients) {
            String whereCalculPrix = "";
            whereCalculPrix += " id_type_place = " + achatClient.getTypePlaceVoyage().getTypePlace().getId();
            whereCalculPrix += " AND id_voyage = " + achatClient.getTypePlaceVoyage().getVoyageVoiture().getVoyage().getId();
            whereCalculPrix += " AND id_type_client = " + achatClient.getTypeClient().getId();

            List<PrixTypePlaceVoyage> prixTypePlaceVoyages = (List<PrixTypePlaceVoyage>) DB.getAllWhere(new PrixTypePlaceVoyage(), whereCalculPrix, connection);
            if (prixTypePlaceVoyages.size() > 0) {
                CA += prixTypePlaceVoyages.get(0).getMontant();
            }
        }
        
        this.setMontantCA(CA);
    }
    public VoyageVoiture() {
    }

    public VoyageVoiture(int id, Voiture voiture, Voyage voyage) {
        this.id = id;
        this.voiture = voiture;
        this.voyage = voyage;
    }

    @ShowTable(name = "ID", numero = 1)
    public int getId() {
        return id;
    }

    @ShowTable(name = "Nom voyage", numero = 2)
    public String getNomVoyage(){
        return this.getVoyage().getNom();
    }

    public void setId(int id) {
        this.id = id;
    }

    public Voiture getVoiture() {
        return voiture;
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
    }

    public Voyage getVoyage() {
        return voyage;
    }

    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }

    @Override
    public String toString() {
        return "VoyageVoiture{" +
                "id=" + id +
                ", voiture=" + (voiture != null ? voiture.getId() : null) +
                ", voyage=" + (voyage != null ? voyage.getId() : null) +
                '}';
    }

    @ShowTable(name = "Date Voyage", numero = 3)
    public String getDateVoyageString(){
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(this.getVoyage().getDate());
    }
}