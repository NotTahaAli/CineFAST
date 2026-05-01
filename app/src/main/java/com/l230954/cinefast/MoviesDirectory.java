package com.l230954.cinefast;

import android.graphics.Bitmap;
import android.net.Uri;

import org.json.JSONArray;

import java.util.ArrayList;

public class MoviesDirectory {

    public static ArrayList<Movies> movies = new ArrayList<>();

    public static void loadMovies(JSONArray moviesArray) {
        for (int i = 0; i < moviesArray.length(); i++) {
            try {
                movies.add(Movies.fromJSON(moviesArray.getJSONObject(i)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static ArrayList<Movies> getTodayMovies() {
        return new ArrayList<>(movies.subList(0, movies.size()-1));
    }
    public static ArrayList<Movies> getTomorrowMovies() {
        return new ArrayList<>(movies.subList(1, movies.size()));
    }

    public static Movies getMovieFromName(String name) {
        for (Movies movie : movies) {
            if (movie.name.equals(name)) {
                return movie;
            }
        }
        return DefaultMovie;
    }

    private static final Movies DefaultMovie = new Movies("Dummy",
       "Action / 0 min",
        (Bitmap) null,
        Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ"),
        "ScreenY",
        "Amana Mall",
        "Hall 2",
        "22:15");
    public static Movies getMovie(int id) {
        if (id <= movies.size() && id > 0) {
            return movies.get(id - 1);
        }
        return DefaultMovie;
    }

    public static int getMovieIndex(Movies movie) {
        return movies.indexOf(movie) + 1;
    }
}
