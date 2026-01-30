package com.project.dto;

public class ProduitDTO {
    private String nom;

    public ProduitDTO() {
    }

    public ProduitDTO(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
