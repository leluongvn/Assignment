package com.example.FM_SHOP.model;

import java.io.Serializable;

public class Product implements Serializable {
    String id;
    String name;
    String type;
    float price;
    int amount;
    String image;


    public Product() {
    }

    public Product(String id, String name, String type, float price, int amount, String image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.amount = amount;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
