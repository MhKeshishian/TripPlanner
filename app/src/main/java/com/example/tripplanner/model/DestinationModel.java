package com.example.tripplanner.model;

public class DestinationModel {
    private String name;
    private int price;
    private String description;
    private int dataImage;
    private long totalPrice; // New property to store the total price


    public DestinationModel(String name, int price, String description, int dataImage) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.dataImage = dataImage;
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

    // Setters for other properties if needed

    // Setter for dataImage
    public void setDataImage(int dataImage) {
        this.dataImage = dataImage;
    }

    // Setter for totalPrice
    public void setTotalPrice(TripDetail tripDetail) {
        long totalPrice = tripDetail.getNumberOfPassengers() * getPrice() * tripDetail.getDurationInDays();
        this.totalPrice = totalPrice;
    }
}
