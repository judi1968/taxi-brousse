package com.project.model.table;

import com.project.model.table.PlaceVoiture;
import com.project.model.table.TypePlace;
import com.project.model.table.VoyageVoiture;
import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;

@TableDb(name = "type_place_voyage")
public class TypePlaceVoyage {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "id_voyage_voiture")
    private VoyageVoiture voyage_voiture; // ← objet VoyageVoiture

    @AttributDb(name = "id_place")
    private PlaceVoiture place; // ← objet PlaceVoiture

    @AttributDb(name = "id_type_place")
    private TypePlace type_place; // ← objet TypePlace

    public TypePlaceVoyage() {
    }

    public TypePlaceVoyage(int id, VoyageVoiture voyage_voiture, PlaceVoiture place, TypePlace type_place) {
        this.id = id;
        this.voyage_voiture = voyage_voiture;
        this.place = place;
        this.type_place = type_place;
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

    public VoyageVoiture getVoyage_voiture() {
        return this.voyage_voiture;
    }

    public void setVoyage_voiture(VoyageVoiture voyage_voiture) {
        this.voyage_voiture = voyage_voiture;
    }

    public PlaceVoiture getPlace() {
        return this.place;
    }

    public void setPlace(PlaceVoiture place) {
        this.place = place;
    }

    public TypePlace getType_place() {
        return this.type_place;
    }

    public void setType_place(TypePlace type_place) {
        this.type_place = type_place;
    }

}
