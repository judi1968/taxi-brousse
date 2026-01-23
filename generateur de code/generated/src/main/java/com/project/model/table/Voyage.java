package com.project.model.table;

import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;
import java.util.Date;

@TableDb(name = "voyage")
public class Voyage {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "nom")
    private String nom;

    @AttributDb(name = "date")
    private Date date;

    public Voyage() {
    }

    public Voyage(int id, String nom, Date date) {
        this.id = id;
        this.nom = nom;
        this.date = date;
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

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
