package com.project.model.table;

import com.project.databases.generalisation.annotation.AttributDb;
import com.project.databases.generalisation.annotation.IdDb;
import com.project.databases.generalisation.annotation.TableDb;

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

    public void setMontant(Double montant) {
        this.montant = montant;
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