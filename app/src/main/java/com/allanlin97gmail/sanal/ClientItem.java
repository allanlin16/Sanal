package com.allanlin97gmail.sanal;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ClientItem implements Parcelable {
    private String clientName;
    private String clientEmail;
    private String clientAddress;
    private String clientPhoneNumber;
    private  int imageResource;

    public ClientItem(int imageResource, String name, String email, String phone, String address) {
        this.imageResource = imageResource;
        this.clientName = name;
        this.clientEmail = email;
        this.clientAddress = address;
        this.clientPhoneNumber = phone;

    }


    protected ClientItem(Parcel in) {
        clientName = in.readString();
        clientEmail = in.readString();
        clientAddress = in.readString();
        clientPhoneNumber = in.readString();
        imageResource = in.readInt();
    }

    public static final Creator<ClientItem> CREATOR = new Creator<ClientItem>() {
        @Override
        public ClientItem createFromParcel(Parcel in) {
            return new ClientItem(in);
        }

        @Override
        public ClientItem[] newArray(int size) {
            return new ClientItem[size];
        }
    };

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(clientName);
        parcel.writeString(clientEmail);
        parcel.writeString(clientAddress);
        parcel.writeString(clientPhoneNumber);
        parcel.writeInt(imageResource);
    }
    
    @Override
    public String toString() {
        return this.clientName;
    }
}
