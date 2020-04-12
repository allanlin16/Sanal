package com.allanlin97;

import androidx.annotation.NonNull;

// BuildingItem object class getters and setters
public class BuildingItem {

    private long id;
    private String buildingName;
    private String buildingAddress;
    private String buildingCity;
    private String buildingPostalCode;

    public BuildingItem(long id, String buildingName, String buildingAddress, String buildingCity, String buildingPostalCode) {
        this.id = id;
        this.buildingName = buildingName;
        this.buildingAddress = buildingAddress;
        this.buildingCity = buildingCity;
        this.buildingPostalCode = buildingPostalCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    public String getBuildingCity() {
        return buildingCity;
    }

    public void setBuildingCity(String buildingCity) {
        this.buildingCity = buildingCity;
    }

    public String getBuildingPostalCode() {
        return buildingPostalCode;
    }

    public void setBuildingPostalCode(String buildingPostalCode) {
        this.buildingPostalCode = buildingPostalCode;
    }

    @NonNull
    @Override
    public String toString() {
        return this.buildingName;
    }

}
