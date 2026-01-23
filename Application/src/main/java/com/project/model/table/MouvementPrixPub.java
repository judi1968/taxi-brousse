package com.project.model.table;

import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;
import java.util.Date;

@TableDb(name = "mouvement_prix_pub")
public class MouvementPrixPub {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "montant")
    private Double montant;

    @AttributDb(name = "date_mouvement")
    private Date dateMouvement;

    public MouvementPrixPub() {
    }

    public MouvementPrixPub(int id, Double montant, Date dateMouvement) {
        this.id = id;
        this.montant = montant;
        this.dateMouvement = dateMouvement;
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

    public double getMontant() {
        return this.montant != null ? this.montant.doubleValue() : 0;
    }

    public Double getMontantObject() {
        return this.montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Date getDateMouvement() {
        return this.dateMouvement;
    }

    public void setDateMouvement(Date dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

}
