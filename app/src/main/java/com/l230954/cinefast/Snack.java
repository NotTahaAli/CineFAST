package com.l230954.cinefast;

public class Snack {
    public final String name;
    public final float price;
    public final String description;
    public final int drawableId;
    public int quantity;
    public Snack(String name, float price, String description, int drawableId) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.drawableId = drawableId;
        quantity = 0;
    }
}
