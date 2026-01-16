package com.project.model.table;

import com.project.databases.generalisation.annotation.AttributDb;
import com.project.databases.generalisation.annotation.IdDb;
import com.project.databases.generalisation.annotation.TableDb;

@TableDb(name = "achat_client")
public class AchatClient {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;
    
    @AttributDb(name = "id_type_client")
    private TypeClient typeClient;
    
    @AttributDb(name = "id_type_place_voyage")
    private TypePlaceVoyage typePlaceVoyage;

    public AchatClient() {
    }

    public AchatClient(int id, TypeClient typeClient, TypePlaceVoyage typePlaceVoyage) {
        this.id = id;
        this.typeClient = typeClient;
        this.typePlaceVoyage = typePlaceVoyage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeClient getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(TypeClient typeClient) {
        this.typeClient = typeClient;
    }

    public TypePlaceVoyage getTypePlaceVoyage() {
        return typePlaceVoyage;
    }

    public void setTypePlaceVoyage(TypePlaceVoyage typePlaceVoyage) {
        this.typePlaceVoyage = typePlaceVoyage;
    }

    @Override
    public String toString() {
        return "AchatClient{" +
                "id=" + id +
                ", typeClient=" + (typeClient != null ? typeClient.getId() : null) +
                ", typePlaceVoyage=" + (typePlaceVoyage != null ? typePlaceVoyage.getId() : null) +
                '}';
    }
}