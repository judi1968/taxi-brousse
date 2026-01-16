package com.project.model.table;

import java.sql.Connection;
import java.util.List;

import com.project.databases.generalisation.DB;
import com.project.databases.generalisation.annotation.AttributDb;
import com.project.databases.generalisation.annotation.IdDb;
import com.project.databases.generalisation.annotation.TableDb;

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
                    System.out.println("hhgf"); 
                    List<PrixTypePlaceVoyage> prixTypePlaceVoyage = (List<PrixTypePlaceVoyage>) DB.getAllWhere(new PrixTypePlaceVoyage(), "id_voyage = "+this.getVoyage().getId()+" AND id_type_place = "+typePlaceVoyage.getTypePlace().getId(), connection);
                    if (prixTypePlaceVoyage.size() > 0) {
                        prixMax += prixTypePlaceVoyage.get(0).getMontant();             
        
                    }
                
                }
                
            }
        // } 

        this.setPrixMaximum(prixMax);
    }
    public VoyageVoiture() {
    }

    public VoyageVoiture(int id, Voiture voiture, Voyage voyage) {
        this.id = id;
        this.voiture = voiture;
        this.voyage = voyage;
    }

    public int getId() {
        return id;
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
}