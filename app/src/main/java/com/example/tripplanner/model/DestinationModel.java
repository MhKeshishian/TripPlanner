package com.example.tripplanner.model;

public class DestinationModel {
    private String name;
    private int price;
    private String description;
    private int dataImage;
    private long totalPrice;
    private double latitude; // Add latitude field
    private double longitude; // Add longitude field


    public DestinationModel(String name, int price, String description, int dataImage) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.dataImage = dataImage;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }
    public DestinationModel(String name, int price, String description, int dataImage, double latitude, double longitude) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.dataImage = dataImage;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }



    public String getDescription() {
        return description;
    }

    public int getDataImage() {
        return dataImage;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    // Setter for dataImage
    public void setDataImage(int dataImage) {
        this.dataImage = dataImage;
    }

    // Setter for totalPrice
    public void setTotalPrice(TripDetail tripDetail) {
        this.totalPrice = tripDetail.getNumberOfPassengers() * getPrice() * tripDetail.getDurationInDays();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
