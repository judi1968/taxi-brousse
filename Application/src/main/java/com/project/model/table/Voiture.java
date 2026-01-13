package com.project.model.table;


import java.util.List;

import com.project.databases.generalisation.annotation.AttributDb;
import com.project.databases.generalisation.annotation.IdDb;
import com.project.databases.generalisation.annotation.TableDb;
@TableDb(name = "voiture") 
public class Voiture {
    @IdDb
    @AttributDb(name = "id")
    private int id;
    @AttributDb(name = "numero")
    private String numero;
    @AttributDb(name = "nom")
    private String nom;

    private List<PlaceVoiture> places;

    public Voiture() {
    }

    public Voiture(int id, String numero, String nom) {
        this.id = id;
        this.numero = numero;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<PlaceVoiture> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceVoiture> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "Voiture{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", nom='" + nom + '\'' +
                '}';
    }
}
