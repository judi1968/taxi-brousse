package com.project.dto;

public class ParametreCaclulPrixTypeDTO {
    private int reference_type_clientId;
    private int object_type_clientId;
    private double pourcentage;
    private int signe;

    public ParametreCaclulPrixTypeDTO() {
    }

    public ParametreCaclulPrixTypeDTO(int reference_type_clientId, int object_type_clientId, double pourcentage, int signe) {
        this.reference_type_clientId = reference_type_clientId;
        this.object_type_clientId = object_type_clientId;
        this.pourcentage = pourcentage;
        this.signe = signe;
    }

    public int getReference_type_clientId() {
        return reference_type_clientId;
    }

    public void setReference_type_clientId(int reference_type_clientId) {
        this.reference_type_clientId = reference_type_clientId;
    }

    public int getObject_type_clientId() {
        return object_type_clientId;
    }

    public void setObject_type_clientId(int object_type_clientId) {
        this.object_type_clientId = object_type_clientId;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public int getSigne() {
        return signe;
    }

    public void setSigne(int signe) {
        this.signe = signe;
    }

}
