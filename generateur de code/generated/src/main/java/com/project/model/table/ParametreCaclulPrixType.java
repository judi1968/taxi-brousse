package com.project.model.table;

import com.project.model.table.TypeClient;
import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.TableDb;

@TableDb(name = "parametre_caclul_prix_type")
public class ParametreCaclulPrixType {

    @IdDb
    @AttributDb(name = "id")
    private Integer id;

    @AttributDb(name = "id_reference_type_client")
    private TypeClient reference_type_client; // ← objet TypeClient

    @AttributDb(name = "id_object_type_client")
    private TypeClient object_type_client; // ← objet TypeClient

    @AttributDb(name = "pourcentage")
    private Double pourcentage;

    @AttributDb(name = "signe")
    private Integer signe;

    public ParametreCaclulPrixType() {
    }

    public ParametreCaclulPrixType(int id, TypeClient reference_type_client, TypeClient object_type_client, Double pourcentage, Integer signe) {
        this.id = id;
        this.reference_type_client = reference_type_client;
        this.object_type_client = object_type_client;
        this.pourcentage = pourcentage;
        this.signe = signe;
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

    public TypeClient getReference_type_client() {
        return this.reference_type_client;
    }

    public void setReference_type_client(TypeClient reference_type_client) {
        this.reference_type_client = reference_type_client;
    }

    public TypeClient getObject_type_client() {
        return this.object_type_client;
    }

    public void setObject_type_client(TypeClient object_type_client) {
        this.object_type_client = object_type_client;
    }

    public double getPourcentage() {
        return this.pourcentage != null ? this.pourcentage.doubleValue() : 0;
    }

    public Double getPourcentageObject() {
        return this.pourcentage;
    }

    public void setPourcentage(Double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public int getSigne() {
        return this.signe != null ? this.signe.intValue() : 0;
    }

    public Integer getSigneObject() {
        return this.signe;
    }

    public void setSigne(Integer signe) {
        this.signe = signe;
    }

}
