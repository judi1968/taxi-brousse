package com.project.dto;

public class DiffusionSocieteDTO {
    private int societeId;
    private String dateDiffusion;
    private double nombrePub;
    private int voyage_voitureId;

    public DiffusionSocieteDTO() {
    }

    public DiffusionSocieteDTO(int societeId, String dateDiffusion, double nombrePub, int voyage_voitureId) {
        this.societeId = societeId;
        this.dateDiffusion = dateDiffusion;
        this.nombrePub = nombrePub;
        this.voyage_voitureId = voyage_voitureId;
    }

    public int getSocieteId() {
        return societeId;
    }

    public void setSocieteId(int societeId) {
        this.societeId = societeId;
    }

    public String getDateDiffusion() {
        return dateDiffusion;
    }

    public void setDateDiffusion(String dateDiffusion) {
        this.dateDiffusion = dateDiffusion;
    }

    public double getNombrePub() {
        return nombrePub;
    }

    public void setNombrePub(double nombrePub) {
        this.nombrePub = nombrePub;
    }

    public int getVoyage_voitureId() {
        return voyage_voitureId;
    }

    public void setVoyage_voitureId(int voyage_voitureId) {
        this.voyage_voitureId = voyage_voitureId;
    }

}
