package com.project.model.table;

import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;

@TableDb(name = "gare")
public class Gare {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "nom")
    private String nom;

    public Gare() {
    }

    public Gare(int id, String nom) {
        this.id = id;
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

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        if (nom != null && nom.trim().length() > 0)
            this.nom = nom;
    }

}
