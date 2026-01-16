package com.project.dto;

public class VoyageVoitureDTO {
    private Integer idVoiture;
    private Integer idVoyage;

    public VoyageVoitureDTO() {
    }

    public VoyageVoitureDTO(Integer idVoiture, Integer idVoyage) {
        this.idVoiture = idVoiture;
        this.idVoyage = idVoyage;
    }

    public Integer getIdVoiture() {
        return idVoiture;
    }

    public void setIdVoiture(Integer idVoiture) {
        this.idVoiture = idVoiture;
    }

    public Integer getIdVoyage() {
        return idVoyage;
    }

    public void setIdVoyage(Integer idVoyage) {
        this.idVoyage = idVoyage;
    }
}