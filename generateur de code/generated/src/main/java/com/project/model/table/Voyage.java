package com.project.model.table;

import com.project.model.table.Gare;
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

    @AttributDb(name = "id_gare_depart")
    private Gare gare_depart; // ← objet Gare

    @AttributDb(name = "id_gare_arrive")
    private Gare gare_arrive; // ← objet Gare

    public Voyage() {
    }

    public Voyage(int id, String nom, Date date, Gare gare_depart, Gare gare_arrive) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.gare_depart = gare_depart;
        this.gare_arrive = gare_arrive;
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

    public Gare getGare_depart() {
        return this.gare_depart;
    }

    public void setGare_depart(Gare gare_depart) {
        this.gare_depart = gare_depart;
    }

    public Gare getGare_arrive() {
        return this.gare_arrive;
    }

    public void setGare_arrive(Gare gare_arrive) {
        this.gare_arrive = gare_arrive;
    }

}
