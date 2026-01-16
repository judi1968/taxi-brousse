package com.project.dto;

public class TypePlaceVoyageDTO {
    private Integer idVoyageVoiture;
    private Integer idPlace;
    private Integer idTypePlace;

    public TypePlaceVoyageDTO() {
    }

    public TypePlaceVoyageDTO(Integer idVoyageVoiture, Integer idPlace, Integer idTypePlace) {
        this.idVoyageVoiture = idVoyageVoiture;
        this.idPlace = idPlace;
        this.idTypePlace = idTypePlace;
    }

    public Integer getIdVoyageVoiture() {
        return idVoyageVoiture;
    }

    public void setIdVoyageVoiture(Integer idVoyageVoiture) {
        this.idVoyageVoiture = idVoyageVoiture;
    }

    public Integer getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(Integer idPlace) {
        this.idPlace = idPlace;
    }

    public Integer getIdTypePlace() {
        return idTypePlace;
    }

    public void setIdTypePlace(Integer idTypePlace) {
        this.idTypePlace = idTypePlace;
    }
} 