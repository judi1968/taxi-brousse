package com.project.dto;

public class PrixTypePlaceVoyageDTO {
    private Integer idTypePlace;
    private Integer idVoyage;
    private Double montant;

    public PrixTypePlaceVoyageDTO() {
    }

    public PrixTypePlaceVoyageDTO(Integer idTypePlace, Integer idVoyage, Double montant) {
        this.idTypePlace = idTypePlace;
        this.idVoyage = idVoyage;
        this.montant = montant;
    }

    public Integer getIdTypePlace() {
        return idTypePlace;
    }

    public void setIdTypePlace(Integer idTypePlace) {
        this.idTypePlace = idTypePlace;
    }

    public Integer getIdVoyage() {
        return idVoyage;
    }

    public void setIdVoyage(Integer idVoyage) {
        this.idVoyage = idVoyage;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }
}