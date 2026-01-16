package com.project.dto;

public class VoyageDTO {
    private String nom;
    private String dateStr; // Pour le format datetime-local

    // Constructeur par défaut
    public VoyageDTO() {
    }

    // Constructeur avec paramètres
    public VoyageDTO(String nom, String dateStr) {
        this.nom = nom;
        this.dateStr = dateStr;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
}