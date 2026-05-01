package com.l230954.cinefast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import org.json.JSONObject;

import java.io.Serializable;

public class Movies implements Serializable {
    public final String name;
    public final String genre;
    public final Bitmap image;
    public final Uri trailer;
    public final String screen;
    public final String theater;
    public final String hall;
    public final String time;

    public Movies(String name, String genre, Bitmap image, Uri trailer, String screen, String theater, String hall, String time) {
        this.name = name;
        this.genre = genre;
        this.trailer = trailer;
        this.screen = screen;
        this.theater = theater;
        this.hall = hall;
        this.time = time;
        this.image = image;
    }

   public static Movies fromJSON(JSONObject obj) {
        try {
            String name = obj.getString("name"),
                    genre = obj.getString("genre"),
                    trailer = obj.getString("trailer"),
                    screen = obj.getString("screen"),
                    theater = obj.getString("theater"),
                    hall = obj.getString("hall"),
                    time = obj.getString("time");
            Bitmap image = null;
            if (obj.has("imageBase64")) {
                byte[] imageBytes = Base64.decode(obj.getString("imageBase64"), Base64.DEFAULT);
                image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            } else if (obj.has("imageAssetName")) {
                image = CineFAST.loadBitmap(obj.getString("imageAssetName"));
            } else if (obj.has("imageResourceId")) {
                int imageResourceId = obj.getInt("imageResourceId");
                image = BitmapFactory.decodeResource(CineFAST.getAppContext().getResources(), imageResourceId);
            }
            if (image != null) {
                return new Movies(name, genre, image, Uri.parse(trailer), screen, theater, hall, time);
            }
            throw new Exception("No image found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
