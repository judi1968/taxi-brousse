package com.project.dto;

public class PayementDiffusionDTO {
    private int societeId;
    private double montant;
    private String datePayement;

    public PayementDiffusionDTO() {
    }

    public PayementDiffusionDTO(int societeId, double montant, String datePayement) {
        this.societeId = societeId;
        this.montant = montant;
        this.datePayement = datePayement;
    }

    public int getSocieteId() {
        return societeId;
    }

    public void setSocieteId(int societeId) {
        this.societeId = societeId;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getDatePayement() {
        return datePayement;
    }

    public void setDatePayement(String datePayement) {
        this.datePayement = datePayement;
    }

}
