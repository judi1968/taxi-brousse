package com.project.model.table;

import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;

@TableDb(name = "voiture")
public class Voiture {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "numero")
    private String numero;

    @AttributDb(name = "nom")
    private String nom;

    public Voiture() {
    }

    public Voiture(int id, String numero, String nom) {
        this.id = id;
        this.numero = numero;
        this.nom = nom;
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

    public String getNumero() {
        return this.numero;
    }

    public void setNumero(String numero) {
        if (numero != null && numero.trim().length() > 0)
            this.numero = numero;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        if (nom != null && nom.trim().length() > 0)
            this.nom = nom;
    }

}
