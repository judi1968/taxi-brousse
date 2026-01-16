package com.project.dto;

public class AchatClientDTO {
    private Integer idTypeClient;
    private Integer idTypePlaceVoyage;

    public AchatClientDTO() {
    }

    public AchatClientDTO(Integer idTypeClient, Integer idTypePlaceVoyage) {
        this.idTypeClient = idTypeClient;
        this.idTypePlaceVoyage = idTypePlaceVoyage;
    }

    public Integer getIdTypeClient() {
        return idTypeClient;
    }

    public void setIdTypeClient(Integer idTypeClient) {
        this.idTypeClient = idTypeClient;
    }

    public Integer getIdTypePlaceVoyage() {
        return idTypePlaceVoyage;
    }

    public void setIdTypePlaceVoyage(Integer idTypePlaceVoyage) {
        this.idTypePlaceVoyage = idTypePlaceVoyage;
    }
}