package com.project.model.table;

import com.project.model.table.DiffusionSociete;
import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;
import java.util.Date;

@TableDb(name = "payement_diffusion")
public class PayementDiffusion {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "id_societe_diffusion")
    private DiffusionSociete societe_diffusion; // ← objet DiffusionSociete

    @AttributDb(name = "montant")
    private Double montant;

    @AttributDb(name = "date_payement")
    private Date datePayement;

    public PayementDiffusion() {
    }

    public PayementDiffusion(int id, DiffusionSociete societe_diffusion, Double montant, Date datePayement) {
        this.id = id;
        this.societe_diffusion = societe_diffusion;
        this.montant = montant;
        this.datePayement = datePayement;
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

    public DiffusionSociete getSociete_diffusion() {
        return this.societe_diffusion;
    }

    public void setSociete_diffusion(DiffusionSociete societe_diffusion) {
        this.societe_diffusion = societe_diffusion;
    }

    public double getMontant() {
        return this.montant != null ? this.montant.doubleValue() : 0;
    }

    public Double getMontantObject() {
        return this.montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Date getDatePayement() {
        return this.datePayement;
    }

    public void setDatePayement(Date datePayement) {
        this.datePayement = datePayement;
    }

}
