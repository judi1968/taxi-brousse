package com.project.dto;

public class TypePlaceVoyageDTO {
    private int voyage_voitureId;
    private int placeId;
    private int type_placeId;

    public TypePlaceVoyageDTO() {
    }

    public TypePlaceVoyageDTO(int voyage_voitureId, int placeId, int type_placeId) {
        this.voyage_voitureId = voyage_voitureId;
        this.placeId = placeId;
        this.type_placeId = type_placeId;
    }

    public int getVoyage_voitureId() {
        return voyage_voitureId;
    }

    public void setVoyage_voitureId(int voyage_voitureId) {
        this.voyage_voitureId = voyage_voitureId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getType_placeId() {
        return type_placeId;
    }

    public void setType_placeId(int type_placeId) {
        this.type_placeId = type_placeId;
    }

}
