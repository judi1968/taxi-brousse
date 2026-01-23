package com.project.model.table;

import com.project.model.table.Voiture;
import com.project.model.table.Voyage;
import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;

@TableDb(name = "voyage_voiture")
public class VoyageVoiture {

    @AttributDb(name = "id_voiture")
    private Voiture voiture; // ← objet Voiture

    @AttributDb(name = "id_voyage")
    private Voyage voyage; // ← objet Voyage

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    public VoyageVoiture() {
    }

    public VoyageVoiture(int id, Voiture voiture, Voyage voyage) {
        this.id = id;
        this.voiture = voiture;
        this.voyage = voyage;
    }

    public Voiture getVoiture() {
        return this.voiture;
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
    }

    public Voyage getVoyage() {
        return this.voyage;
    }

    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
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

}
