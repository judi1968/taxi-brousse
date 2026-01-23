package com.project.model.table;

import com.project.model.table.Societe;
import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;
import java.util.Date;

@TableDb(name = "diffusion_societe")
public class DiffusionSociete {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "id_societe")
    private Societe societe; // ← objet Societe

    @AttributDb(name = "date_diffusion")
    private Date dateDiffusion;

    @AttributDb(name = "nombre_pub")
    private Double nombrePub;

    public DiffusionSociete() {
    }

    public DiffusionSociete(int id, Societe societe, Date dateDiffusion, Double nombrePub) {
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
