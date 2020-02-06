package com.allanlin97gmail.sanal;

public class ClientItem {
    private String clientName;
    private String clientEmail;
    private String clientAddress;
    private String clientPhoneNumber;

    public ClientItem(String name, String email, String phone, String address) {
        this.clientName = name;
        this.clientEmail = email;
        this.clientAddress = address;
        this.clientPhoneNumber = phone;

    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }
}
