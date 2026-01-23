package com.project.dto;

public class VoitureDTO {
    private String numero;
    private String nom;

    public VoitureDTO() {
    }

    public VoitureDTO(String numero, String nom) {
        this.numero = numero;
        this.nom = nom;
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

}
