package com.project.dto;

public class PlaceVoitureDTO {
    private Integer idVoiture;
    private String numero;

    public PlaceVoitureDTO() {
    }

    public PlaceVoitureDTO(Integer idVoiture, String numero) {
        this.idVoiture = idVoiture;
        this.numero = numero;
    }

    public Integer getIdVoiture() {
        return idVoiture;
    }

    public void setIdVoiture(Integer idVoiture) {
        this.idVoiture = idVoiture;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}