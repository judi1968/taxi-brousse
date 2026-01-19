package com.project.model.table;

import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;

@TableDb(name = "parametre_caclul_prix_type")
public class ParametreCalculPrixType {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;
    
    @AttributDb(name = "id_reference_type_client")
    private TypeClient referenceTypeClient;
    
    @AttributDb(name = "id_object_type_client")
    private TypeClient objectTypeClient;
    
    @AttributDb(name = "pourcentage")
    private double pourcentage;
    
    @AttributDb(name = "signe")
    private Integer signe;

    public ParametreCalculPrixType() {
    }

    public ParametreCalculPrixType(int id, TypeClient referenceTypeClient, TypeClient objectTypeClient, 
                                  Double pourcentage, Integer signe) {
        this.id = id;
        this.referenceTypeClient = referenceTypeClient;
        this.objectTypeClient = objectTypeClient;
        this.pourcentage = pourcentage;
        this.signe = signe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeClient getReferenceTypeClient() {
        return referenceTypeClient;
    }

    public void setReferenceTypeClient(TypeClient referenceTypeClient) {
        this.referenceTypeClient = referenceTypeClient;
    }

    public TypeClient getObjectTypeClient() {
        return objectTypeClient;
    }

    public void setObjectTypeClient(TypeClient objectTypeClient) {
        this.objectTypeClient = objectTypeClient;
    }

    public Double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public Integer getSigne() {
        return signe;
    }

    public void setSigne(Integer signe) {
        this.signe = signe;
    }

    @Override
    public String toString() {
        return "ParametreCalculPrixType{" +
                "id=" + id +
                ", referenceTypeClient=" + (referenceTypeClient != null ? referenceTypeClient.getId() : null) +
                ", objectTypeClient=" + (objectTypeClient != null ? objectTypeClient.getId() : null) +
                ", pourcentage=" + pourcentage +
                ", signe=" + signe +
                '}';
    }
}