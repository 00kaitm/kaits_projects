package com.revature.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class FruitRequest {

    @NotBlank
    private String name;
    private String description;
    @Positive
    private double price;

    public FruitRequest() {
    }

    public FruitRequest(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}