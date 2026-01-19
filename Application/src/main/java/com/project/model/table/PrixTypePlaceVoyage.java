package com.project.model.table;

import java.sql.Connection;
import java.util.List;

import com.project.pja.databases.generalisation.DB;
import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;

@TableDb(name = "prix_type_place_voyage")
public class PrixTypePlaceVoyage {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;
    
    @AttributDb(name = "id_type_place")
    private TypePlace typePlace;
    
    @AttributDb(name = "id_voyage")
    private Voyage voyage;
    
    @AttributDb(name = "montant")
    private double montant;

    @AttributDb(name = "id_type_client")
    private TypeClient typeClient;

    public TypeClient getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(TypeClient typeClient) {
        this.typeClient = typeClient;
    }

    public PrixTypePlaceVoyage() {
    }

    public PrixTypePlaceVoyage(int id, TypePlace typePlace, Voyage voyage, Double montant) {
        this.id = id;
        this.typePlace = typePlace;
        this.voyage = voyage;
        this.montant = montant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypePlace getTypePlace() {
        return typePlace;
    }

    public void setTypePlace(TypePlace typePlace) {
        this.typePlace = typePlace;
    }

    public Voyage getVoyage() {
        return voyage;
    }

    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }
    public void setMontant(Double montant, Connection connection) throws Exception {
        if (this.getTypeClient().getId() !=3) {
            this.montant = montant;
        }else{
            montant = 0.0; 
            List<ParametreCalculPrixType> parametreCalculPrixTypes = (List<ParametreCalculPrixType>) DB.getAllWhere(new ParametreCalculPrixType(), " id_object_type_client = "+this.getTypeClient().getId(), connection);
            ParametreCalculPrixType parametreCalculPrixType = null;
            if (parametreCalculPrixTypes.size() > 0) {
                parametreCalculPrixType = parametreCalculPrixTypes.get(0);
                String whereCalculPrix = "";
            whereCalculPrix += " id_type_place = " + this.getTypePlace().getId();
            whereCalculPrix += " AND id_voyage = " + this.getVoyage().getId();
            whereCalculPrix += " AND id_type_client = " + parametreCalculPrixType.getReferenceTypeClient().getId();
                List<PrixTypePlaceVoyage> prixTypePlaceVoyages = (List<PrixTypePlaceVoyage>) DB.getAllWhere(new PrixTypePlaceVoyage(), whereCalculPrix, connection);
                if (prixTypePlaceVoyages.size() > 0) {
                    PrixTypePlaceVoyage prixTypePlaceVoyage = prixTypePlaceVoyages.get(0);
                    System.out.println(prixTypePlaceVoyage.getMontant());
                    if (parametreCalculPrixType.getSigne() == 1) {
                        montant = prixTypePlaceVoyage.getMontant() + ((prixTypePlaceVoyage.getMontant()*parametreCalculPrixType.getPourcentage())/100);
                    } else if (parametreCalculPrixType.getSigne() == 2) {
                        montant = prixTypePlaceVoyage.getMontant() - ((prixTypePlaceVoyage.getMontant()*parametreCalculPrixType.getPourcentage())/100);
                        System.out.println(((prixTypePlaceVoyage.getMontant()*parametreCalculPrixType.getPourcentage())/100)+ " : "+montant + " : pourcentage : "+parametreCalculPrixType.getPourcentage());
                    } else if (parametreCalculPrixType.getSigne() == 3) {
                        montant = prixTypePlaceVoyage.getMontant() * ((prixTypePlaceVoyage.getMontant()*parametreCalculPrixType.getPourcentage())/100);
                    }else if (parametreCalculPrixType.getSigne() == 4) {
                        montant = prixTypePlaceVoyage.getMontant() / ((prixTypePlaceVoyage.getMontant()*parametreCalculPrixType.getPourcentage())/100);
                    }else{
                    }
                }
            }
            setMontant(montant);
        }
    }

    @Override
    public String toString() {
        return "PrixTypePlaceVoyage{" +
                "id=" + id +
                ", typePlace=" + (typePlace != null ? typePlace.getId() : null) +
                ", voyage=" + (voyage != null ? voyage.getId() : null) +
                ", montant=" + montant +
                '}';
    }
}