package com.project.dto;

public class VoyageDTO {
    private String nom;
    private String date;

    public VoyageDTO() {
    }

    public VoyageDTO(String nom, String date) {
        this.nom = nom;
        this.date = date;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
