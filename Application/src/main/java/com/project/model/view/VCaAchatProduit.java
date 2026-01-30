package com.project.model.view;

import com.project.model.table.AchatProduit;
import com.project.model.table.Produit;
import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.ShowTable;
import com.project.pja.databases.generalisation.annotation.TableDb;
import java.util.Date;

@TableDb(name = "v_ca_achat_produit")
public class VCaAchatProduit {

    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "id_produit")
    private Produit produit; // ← objet Produit

    @AttributDb(name = "quantite")
    private Integer quantite;

    @AttributDb(name = "date_achat")
    private Date dateAchat;

    @AttributDb(name = "prix_unitaire")
    private Double prixUnitaire;

    @AttributDb(name = "montant_totale")
    private Double montantTotale;



    @ShowTable(name = "Nom produit", numero = 1)
    public String getNomproduit(){
        return this.getProduit().getNom();
    }

    @ShowTable(name = "date", numero = 2)
    public String getDateAchatString(){
        return this.getDateAchat().toString();
    }

    @ShowTable(name = "Pu", numero = 3)
    public String getPU(){
        return this.getPrixUnitaire()+" AR";
    }
    
    @ShowTable(name = "Quantite", numero = 4)
    public String getQuantiteString(){
        return this.getQuantite()+" ";
    }
    

    @ShowTable(name = "Montant totale", numero = 5)
    public String getMontantTotaleString(){
        return this.getMontantTotale()+" AR";
    }
    


    public VCaAchatProduit() {
    }

    public VCaAchatProduit(Integer id, Produit produit, Integer quantite, Date dateAchat, 
                          Double prixUnitaire, Double montantTotale) {
        this.id = id;
        this.produit = produit;
        this.quantite = quantite;
        this.dateAchat = dateAchat;
        this.prixUnitaire = prixUnitaire;
        this.montantTotale = montantTotale;
    }

    // Getters et Setters
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

    public double getPrixUnitaire() {
        return this.prixUnitaire != null ? this.prixUnitaire.doubleValue() : 0.0;
    }

    public Double getPrixUnitaireObject() {
        return this.prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getMontantTotale() {
        return this.montantTotale != null ? this.montantTotale.doubleValue() : 0.0;
    }

    public Double getMontantTotaleObject() {
        return this.montantTotale;
    }

    public void setMontantTotale(Double montantTotale) {
        this.montantTotale = montantTotale;
    }

    // Méthode utilitaire pour créer à partir d'un AchatProduit
    public static VCaAchatProduit fromAchatProduit(AchatProduit achat, Double prixUnitaire) {
        if (achat == null) return null;
        
        Double montant = prixUnitaire != null ? 
            prixUnitaire * achat.getQuantite() : 
            0.0;
        
        return new VCaAchatProduit(
            achat.getIdObject(),
            achat.getProduit(),
            achat.getQuantiteObject(),
            achat.getDateAchat(),
            prixUnitaire,
            montant
        );
    }

    @Override
    public String toString() {
        return "VCaAchatProduit{" +
                "id=" + id +
                ", produit=" + (produit != null ? produit.getId() : "null") +
                ", quantite=" + quantite +
                ", dateAchat=" + dateAchat +
                ", prixUnitaire=" + prixUnitaire +
                ", montantTotale=" + montantTotale +
                '}';
    }
}