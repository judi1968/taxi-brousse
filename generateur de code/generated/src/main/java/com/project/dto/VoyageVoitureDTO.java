package com.project.dto;

public class VoyageVoitureDTO {
    private int voitureId;
    private int voyageId;

    public VoyageVoitureDTO() {
    }

    public VoyageVoitureDTO(int voitureId, int voyageId) {
        this.voitureId = voitureId;
        this.voyageId = voyageId;
    }

    public int getVoitureId() {
        return voitureId;
    }

    public void setVoitureId(int voitureId) {
        this.voitureId = voitureId;
    }

    public int getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(int voyageId) {
        this.voyageId = voyageId;
    }

}
