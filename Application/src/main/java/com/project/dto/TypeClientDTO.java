package com.project.dto;

public class TypeClientDTO {
    private String nom;

    public TypeClientDTO() {
    }

    public TypeClientDTO(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}