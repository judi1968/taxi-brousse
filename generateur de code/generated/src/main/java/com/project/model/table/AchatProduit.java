package com.project.model.table;

import com.project.model.table.Produit;
import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;
import java.util.Date;

@TableDb(name = "achat_produit")
public class AchatProduit {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "id_produit")
    private Produit produit; // ← objet Produit

    @AttributDb(name = "quantite")
    private Integer quantite;

    @AttributDb(name = "date_achat")
    private Date dateAchat;

    public AchatProduit() {
    }

    public AchatProduit(int id, Produit produit, Integer quantite, Date dateAchat) {
        this.id = id;
        this.produit = produit;
        this.quantite = quantite;
        this.dateAchat = dateAchat;
    }

    public int getId() {
        return this.id != null ? this.id.intValue() : 0;
    }

    public Integer getIdObject() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Produit getProduit() {
        return this.produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return this.quantite != null ? this.quantite.intValue() : 0;
    }

    public Integer getQuantiteObject() {
        return this.quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Date getDateAchat() {
        return this.dateAchat;
    }

    public void setDateAchat(Date dateAchat) {
        this.dateAchat = dateAchat;
    }

}
