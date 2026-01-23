package com.project.model.table;

import com.project.model.table.Voiture;
import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;

@TableDb(name = "place_voiture")
public class PlaceVoiture {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "id_voiture")
    private Voiture voiture; // ← objet Voiture

    @AttributDb(name = "numero")
    private String numero;

    public PlaceVoiture() {
    }

    public PlaceVoiture(int id, Voiture voiture, String numero) {
        this.id = id;
        this.voiture = voiture;
        this.numero = numero;
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

    public Voiture getVoiture() {
        return this.voiture;
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
    }

    public String getNumero() {
        return this.numero;
    }

    public void setNumero(String numero) {
        if (numero != null && numero.trim().length() > 0)
            this.numero = numero;
    }

}
