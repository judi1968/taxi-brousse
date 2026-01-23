package com.project.dto;

public class PlaceVoitureDTO {
    private int voitureId;
    private String numero;

    public PlaceVoitureDTO() {
    }

    public PlaceVoitureDTO(int voitureId, String numero) {
        this.voitureId = voitureId;
        this.numero = numero;
    }

    public int getVoitureId() {
        return voitureId;
    }

    public void setVoitureId(int voitureId) {
        this.voitureId = voitureId;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

}
