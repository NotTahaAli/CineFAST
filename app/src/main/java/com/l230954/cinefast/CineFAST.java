package com.l230954.cinefast;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class CineFAST extends Application {

    private static Context context;


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
