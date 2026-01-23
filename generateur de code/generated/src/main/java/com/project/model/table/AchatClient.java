package com.project.model.table;

import com.project.model.table.TypeClient;
import com.project.model.table.TypePlaceVoyage;
import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;

@TableDb(name = "achat_client")
public class AchatClient {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "id_type_client")
    private TypeClient type_client; // ← objet TypeClient

    @AttributDb(name = "id_type_place_voyage")
    private TypePlaceVoyage type_place_voyage; // ← objet TypePlaceVoyage

    public AchatClient() {
    }

    public AchatClient(int id, TypeClient type_client, TypePlaceVoyage type_place_voyage) {
        this.id = id;
        this.type_client = type_client;
        this.type_place_voyage = type_place_voyage;
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

    public TypeClient getType_client() {
        return this.type_client;
    }

    public void setType_client(TypeClient type_client) {
        this.type_client = type_client;
    }

    public TypePlaceVoyage getType_place_voyage() {
        return this.type_place_voyage;
    }

    public void setType_place_voyage(TypePlaceVoyage type_place_voyage) {
        this.type_place_voyage = type_place_voyage;
    }

}
