package com.example.tripplanner;

public class DestinationModel {
    private String name;
    private int price;
    private int radioButtonId;

    public DestinationModel(String name, int price, int radioButtonId) {
        this.name = name;
        this.price = price;
        this.radioButtonId = radioButtonId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getRadioButtonId() {
        return radioButtonId;
    }
}
