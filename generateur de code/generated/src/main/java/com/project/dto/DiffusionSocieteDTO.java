package com.project.dto;

public class DiffusionSocieteDTO {
    private int societeId;
    private String dateDiffusion;
    private double nombrePub;

    public DiffusionSocieteDTO() {
    }

    public DiffusionSocieteDTO(int societeId, String dateDiffusion, double nombrePub) {
        this.societeId = societeId;
        this.dateDiffusion = dateDiffusion;
        this.nombrePub = nombrePub;
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

}
