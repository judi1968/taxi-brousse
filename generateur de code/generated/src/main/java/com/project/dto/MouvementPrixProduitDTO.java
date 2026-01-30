package com.project.dto;

public class MouvementPrixProduitDTO {
    private int produitId;
    private double montant;
    private String dateMouvement;

    public MouvementPrixProduitDTO() {
    }

    public MouvementPrixProduitDTO(int produitId, double montant, String dateMouvement) {
        this.produitId = produitId;
        this.montant = montant;
        this.dateMouvement = dateMouvement;
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
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
