package com.allanlin97;

public class ExtinguisherItem {
    private long id;
    private String make;
    private String serialNumber;
    private String barcodeNumber;
    private String area;
    private String description;
    private String type;
    private String rating;
    private String mDate;
    private String hDate;
    private String sDate;
    private String nSDate;
    private String comment;
    private String status;
    private String photo;

    // getter and setting for extinguisher object

    public ExtinguisherItem(long id, String make, String serialNumber, String barcodeNumber,
                            String area, String description, String type, String rating,
                            String mDate, String hDate, String sDate, String nSDate,
                            String comment, String status, String photo) {
        this.id = id;
        this.make = make;
        this.serialNumber = serialNumber;
        this.barcodeNumber = barcodeNumber;
        this.area = area;
        this.description = description;
        this.type = type;
        this.rating = rating;
        this.mDate = mDate;
        this.hDate = hDate;
        this.sDate = sDate;
        this.nSDate = nSDate;
        this.comment = comment;
        this.status = status;
        this.photo = photo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBarcodeNumber() {
        return barcodeNumber;
    }

    public void setBarcodeNumber(String barcodeNumber) {
        this.barcodeNumber = barcodeNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String gethDate() {
        return hDate;
    }

    public void sethDate(String hDate) {
        this.hDate = hDate;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String getnSDate() {
        return nSDate;
    }

    public void setnSDate(String nSDate) {
        this.nSDate = nSDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
