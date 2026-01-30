package com.project.dto;

public class AchatProduitDTO {
    private int produitId;
    private int quantite;
    private String dateAchat;

    public AchatProduitDTO() {
    }

    public AchatProduitDTO(int produitId, int quantite, String dateAchat) {
        this.produitId = produitId;
        this.quantite = quantite;
        this.dateAchat = dateAchat;
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(String dateAchat) {
        this.dateAchat = dateAchat;
    }

}
