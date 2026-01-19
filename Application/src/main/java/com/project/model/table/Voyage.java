package com.project.model.table;

import java.util.Date;

import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;

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
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Voyage{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", date=" + date +
                '}';
    }
}