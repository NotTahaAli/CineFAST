package com.l230954.cinefast;

import java.io.Serializable;

public class Snack implements Serializable {
    public final String name;
    public final float price;
    public final String description;
    public final int imageId;
    public int quantity;
    public Snack(String name, float price, String description, int drawableId) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageId = drawableId;
        quantity = 0;
    }
}
