package com.project.model.view;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.project.model.table.Produit;
import com.project.pja.databases.generalisation.DB;
import com.project.pja.databases.generalisation.annotation.ShowTable;

public class CAProduit {
    Produit produit;
    double montant;
    public Produit getProduit() {
        return produit;
    }
    public void setProduit(Produit produit) {
        this.produit = produit;
    }
    public double getMontant() {
        return montant;
    }
    public void setMontant(double montant) {
        this.montant = montant;
    }

    @ShowTable(name = "Nom produit", numero = 1)
    public String getNomproduit(){
        return this.getProduit().getNom();
    }

    @ShowTable(name = "Montant totale", numero = 5)
    public String getMontantTotaleString(){
        return this.getMontant()+" AR";
    }

    public static List<CAProduit> getAllCAProduit(List<VCaAchatProduit> vCaAchatProduits, Connection connection) throws Exception{
        List<Produit> produits = (List<Produit>) DB.getAll(new Produit(), connection);
        List<CAProduit> caProduits = new ArrayList<>();
        for (Produit produit : produits) {
            CAProduit caProduit = new CAProduit();
            caProduit.setProduit(produit);
            double montant = 0;
            for (VCaAchatProduit vCaAchatProduit : vCaAchatProduits) {
                if (vCaAchatProduit.getProduit().getId() == produit.getId()) {
                    montant += vCaAchatProduit.getMontantTotale();
                }
            }
            caProduit.setMontant(montant);
            caProduits.add(caProduit);
        }
        return caProduits;
    }
    
}
