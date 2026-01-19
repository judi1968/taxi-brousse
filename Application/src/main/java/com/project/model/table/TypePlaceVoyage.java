package com.project.model.table;

import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;

@TableDb(name = "type_place_voyage")
public class TypePlaceVoyage {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;
    
    @AttributDb(name = "id_voyage_voiture")
    private VoyageVoiture voyageVoiture;
    
    @AttributDb(name = "id_place")
    private PlaceVoiture place;
    
    @AttributDb(name = "id_type_place")
    private TypePlace typePlace;

    public TypePlaceVoyage() {
    }

    public TypePlaceVoyage(int id, VoyageVoiture voyageVoiture, PlaceVoiture place, TypePlace typePlace) {
        this.id = id;
        this.voyageVoiture = voyageVoiture;
        this.place = place;
        this.typePlace = typePlace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VoyageVoiture getVoyageVoiture() {
        return voyageVoiture;
    }

    public void setVoyageVoiture(VoyageVoiture voyageVoiture) {
        this.voyageVoiture = voyageVoiture;
    }

    public PlaceVoiture getPlace() {
        return place;
    }

    public void setPlace(PlaceVoiture place) {
        this.place = place;
    }

    public TypePlace getTypePlace() {
        return typePlace;
    }

    public void setTypePlace(TypePlace typePlace) {
        this.typePlace = typePlace;
    }

    @Override
    public String toString() {
        return "TypePlaceVoyage{" +
                "id=" + id +
                ", voyageVoiture=" + (voyageVoiture != null ? voyageVoiture.getId() : null) +
                ", place=" + (place != null ? place.getId() : null) +
                ", typePlace=" + (typePlace != null ? typePlace.getId() : null) +
                '}';
    }
}