package com.project.dto;

public class AchatClientDTO {
    private int type_clientId;
    private int type_place_voyageId;

    public AchatClientDTO() {
    }

    public AchatClientDTO(int type_clientId, int type_place_voyageId) {
        this.type_clientId = type_clientId;
        this.type_place_voyageId = type_place_voyageId;
    }

    public int getType_clientId() {
        return type_clientId;
    }

    public void setType_clientId(int type_clientId) {
        this.type_clientId = type_clientId;
    }

    public int getType_place_voyageId() {
        return type_place_voyageId;
    }

    public void setType_place_voyageId(int type_place_voyageId) {
        this.type_place_voyageId = type_place_voyageId;
    }

}
