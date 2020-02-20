package com.allanlin97gmail.sanal;

public class BuildingItem {

    private String buildingName;
    private String buildingAddress;
    private String buildingCity;
    private String buildingPostalCode;

    public BuildingItem(String name, String address, String city, String postalcode) {
        this.buildingName = name;
        this.buildingAddress = address;
        this.buildingCity = city;
        this.buildingPostalCode = postalcode;
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
}
