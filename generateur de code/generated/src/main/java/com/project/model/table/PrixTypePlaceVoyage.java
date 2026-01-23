package com.project.model.table;

import com.project.model.table.TypeClient;
import com.project.model.table.TypePlace;
import com.project.model.table.Voyage;
import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;

@TableDb(name = "prix_type_place_voyage")
public class PrixTypePlaceVoyage {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "id_type_place")
    private TypePlace type_place; // ← objet TypePlace

    @AttributDb(name = "id_voyage")
    private Voyage voyage; // ← objet Voyage

    @AttributDb(name = "montant")
    private Double montant;

    @AttributDb(name = "id_type_client")
    private TypeClient type_client; // ← objet TypeClient

    public PrixTypePlaceVoyage() {
    }

    public PrixTypePlaceVoyage(int id, TypePlace type_place, Voyage voyage, Double montant, TypeClient type_client) {
        this.id = id;
        this.type_place = type_place;
        this.voyage = voyage;
        this.montant = montant;
        this.type_client = type_client;
    }

    public int getId() {
        return this.id != null ? this.id.intValue() : 0;
    }

    public Integer getIdObject() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TypePlace getType_place() {
        return this.type_place;
    }

    public void setType_place(TypePlace type_place) {
        this.type_place = type_place;
    }

    public Voyage getVoyage() {
        return this.voyage;
    }

    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }

    public double getMontant() {
        return this.montant != null ? this.montant.doubleValue() : 0;
    }

    public Double getMontantObject() {
        return this.montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public TypeClient getType_client() {
        return this.type_client;
    }

    public void setType_client(TypeClient type_client) {
        this.type_client = type_client;
    }

}
