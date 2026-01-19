package com.project.model.table;

import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;

@TableDb(name = "type_place")
public class TypePlace {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;
    
    @AttributDb(name = "nom")
    private String nom;

    public TypePlace() {
    }

    public TypePlace(int id, String nom) {
        this.id = id;
        this.nom = nom;
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

    @Override
    public String toString() {
        return "TypePlace{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }
}