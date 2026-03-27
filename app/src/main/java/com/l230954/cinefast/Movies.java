package com.l230954.cinefast;

import android.net.Uri;

import java.io.Serializable;

public class Movies implements Serializable {
    public final String name;
    public final String genre;
    public final int imageId;
    public final Uri trailer;
    public final String screen;
    public final String theater;
    public final String hall;
    public final String time;

    public Movies(String name, String genre, int imageId, Uri trailer, String screen, String theater, String hall, String time) {
        this.name = name;
        this.genre = genre;
        this.imageId = imageId;
        this.trailer = trailer;
        this.screen = screen;
        this.theater = theater;
        this.hall = hall;
        this.time = time;
    }
}
