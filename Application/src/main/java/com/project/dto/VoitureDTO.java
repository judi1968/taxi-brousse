package com.project.dto;

public class VoitureDTO {
    private String nom;
    private String numero;

    // Constructeurs
    public VoitureDTO() {
    }

    public VoitureDTO(String nom, String numero) {
        this.nom = nom;
        this.numero = numero;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}