package com.project.dto;

public class MouvementPrixPubDTO {
    private double montant;
    private String dateMouvement;

    public MouvementPrixPubDTO() {
    }

    public MouvementPrixPubDTO(double montant, String dateMouvement) {
        this.montant = montant;
        this.dateMouvement = dateMouvement;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(String dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

}
