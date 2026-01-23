package com.project.dto;

public class PrixTypePlaceVoyageDTO {
    private int type_placeId;
    private int voyageId;
    private double montant;
    private int type_clientId;

    public PrixTypePlaceVoyageDTO() {
    }

    public PrixTypePlaceVoyageDTO(int type_placeId, int voyageId, double montant, int type_clientId) {
        this.type_placeId = type_placeId;
        this.voyageId = voyageId;
        this.montant = montant;
        this.type_clientId = type_clientId;
    }

    public int getType_placeId() {
        return type_placeId;
    }

    public void setType_placeId(int type_placeId) {
        this.type_placeId = type_placeId;
    }

    public int getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(int voyageId) {
        this.voyageId = voyageId;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public int getType_clientId() {
        return type_clientId;
    }

    public void setType_clientId(int type_clientId) {
        this.type_clientId = type_clientId;
    }

}
