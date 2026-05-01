package com.l230954.cinefast;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class CineFAST extends Application {

    private static Context context;

    public static String BitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) return "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // PNG keeps quality
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap Base64ToBitmap(String base64) {
        byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public static Bitmap loadBitmap(String fileName) throws IOException {
        return BitmapFactory.decodeStream(context.getAssets().open(fileName));
    }

    public static String loadJSON(String fileName) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public void onCreate() {
        super.onCreate();
        CineFAST.context = getApplicationContext();

        try {
            MoviesDirectory.loadMovies(new JSONArray(loadJSON("movies.json")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        };
    }

    public static Context getAppContext() {
        return CineFAST.context;
    }
}
