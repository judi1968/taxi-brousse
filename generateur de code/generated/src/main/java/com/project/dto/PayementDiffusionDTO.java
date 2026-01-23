package com.project.dto;

public class PayementDiffusionDTO {
    private int societe_diffusionId;
    private double montant;
    private String datePayement;

    public PayementDiffusionDTO() {
    }

    public PayementDiffusionDTO(int societe_diffusionId, double montant, String datePayement) {
        this.societe_diffusionId = societe_diffusionId;
        this.montant = montant;
        this.datePayement = datePayement;
    }

    public int getSociete_diffusionId() {
        return societe_diffusionId;
    }

    public void setSociete_diffusionId(int societe_diffusionId) {
        this.societe_diffusionId = societe_diffusionId;
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
