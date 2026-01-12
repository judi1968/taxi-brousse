package com.project.model.table;

public class PlaceVoiture {

    private int id;
    private Voiture voiture; // ← objet Voiture
    private String numero;

    public PlaceVoiture() {
    }

    public PlaceVoiture(int id, Voiture voiture, String numero) {
        this.id = id;
        this.voiture = voiture;
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Voiture getVoiture() {
        return voiture;
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "PlaceVoiture{" +
                "id=" + id +
                ", voiture=" + (voiture != null ? voiture.getId() : null) +
                ", numero='" + numero + '\'' +
                '}';
    }
}
