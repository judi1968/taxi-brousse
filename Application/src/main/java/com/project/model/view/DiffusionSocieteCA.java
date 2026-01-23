package com.project.model.view;

import com.project.model.table.Societe;
import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.ShowTable;
import com.project.pja.databases.generalisation.annotation.TableDb;
import java.util.Date;

@TableDb(name = "v_diffusion_ca")
public class DiffusionSocieteCA {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "id_societe")
    private Societe societe; // ← objet Societe

    @AttributDb(name = "date_diffusion")
    private Date dateDiffusion;

    @AttributDb(name = "nombre_pub")
    private Double nombrePub;

    @AttributDb(name = "pu")
    private Double prixUnitaire;

    @AttributDb(name = "reste")
    private Double reste;

    public Double getReste() {
        return reste;
    }

    @ShowTable(name = "reste", numero = 5)
    public String getResteString() {
        return reste+" Ar";
    }

    public void setReste(Double reste) {
        this.reste = reste;
    }

    @ShowTable(name = "PU", numero = 3)
    public String getPrixUnitaireString() {
        return prixUnitaire+" Ar";
    }

    @ShowTable(name = "Totale", numero = 4)
    public String getPrixTotaleString() {
        return montantTotale+" Ar";
    }
    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    @ShowTable(name = "Societe", numero = 1)
    public String getNomSociete(){
        System.out.println("ici"+ this.getSociete().getNom());
        String a = this.getSociete().getNom();;
        System.out.println(a);
        return a; 
    }

    @ShowTable(name = "Date diffusion", numero = 2)
    public String getDateDiffusionString(){
        return this.getDateDiffusion().toString();
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    @AttributDb(name = "montant_totale")
    private Double montantTotale;

    public Double getMontantTotale() {
        return montantTotale;
    }

    public void setMontantTotale(Double montantTotale) {
        this.montantTotale = montantTotale;
    }

    public DiffusionSocieteCA() {
    }

    public DiffusionSocieteCA(int id, Societe societe, Date dateDiffusion, Double nombrePub) {
        this.id = id;
        this.societe = societe;
        this.dateDiffusion = dateDiffusion;
        this.nombrePub = nombrePub;
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

    public Societe getSociete() {
        return this.societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public Date getDateDiffusion() {
        return this.dateDiffusion;
    }

    public void setDateDiffusion(Date dateDiffusion) {
        this.dateDiffusion = dateDiffusion;
    }

    public double getNombrePub() {
        return this.nombrePub != null ? this.nombrePub.doubleValue() : 0;
    }

    public Double getNombrePubObject() {
        return this.nombrePub;
    }

    public void setNombrePub(Double nombrePub) {
        this.nombrePub = nombrePub;
    }

}
