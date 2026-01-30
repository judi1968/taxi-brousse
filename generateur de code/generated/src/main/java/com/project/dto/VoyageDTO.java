package com.project.dto;

public class VoyageDTO {
    private String nom;
    private String date;
    private int gare_departId;
    private int gare_arriveId;

    public VoyageDTO() {
    }

    public VoyageDTO(String nom, String date, int gare_departId, int gare_arriveId) {
        this.nom = nom;
        this.date = date;
        this.gare_departId = gare_departId;
        this.gare_arriveId = gare_arriveId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGare_departId() {
        return gare_departId;
    }

    public void setGare_departId(int gare_departId) {
        this.gare_departId = gare_departId;
    }

    public int getGare_arriveId() {
        return gare_arriveId;
    }

    public void setGare_arriveId(int gare_arriveId) {
        this.gare_arriveId = gare_arriveId;
    }

}
