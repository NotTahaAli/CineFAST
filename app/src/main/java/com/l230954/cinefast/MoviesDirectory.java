package com.l230954.cinefast;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.net.URISyntaxException;
import java.util.Objects;

public class MoviesDirectory {

    public static final Movies[] movies = {
            new Movies(
                    "The Dark Knight",
                    "Action / 152 min",
                    R.drawable.movie1,
                    Uri.parse("https://www.youtube.com/watch?v=EXeTwQWrcwY"),
                    "ScreenX",
                    "Amana Mall",
                    "Hall 1",
                    "22:15"),
            new Movies(
                    "Inception",
                    "Sci-Fi / 148 min",
                    R.drawable.movie2,
                    Uri.parse("https://www.youtube.com/watch?v=YoHD9XEInc0"),
                    "ScreenY",
                    "Amana Mall",
                    "Hall 2",
                    "22:15"),
            new Movies(
                    "Interstellar",
                    "Sci-Fi / 169 min",
                    R.drawable.movie3,
                    Uri.parse("https://www.youtube.com/watch?v=zSWdZVtXT7E"),
                    "ScreenX",
                    "Amana Mall",
                    "Hall 2",
                    "16:15"),
            new Movies(
                    "Shawshank Redemption",
                    "Drama / 142 min",
                    R.drawable.movie4,
                    Uri.parse("https://www.youtube.com/watch?v=PLl99DlL6b4"),
                    "ScreenY",
                    "Packages Mall",
                    "Hall 1",
                    "22:15")
    };

    public static Movies[] getTodayMovies() {
        Movies[] todayMovies = new Movies[movies.length-1];
        System.arraycopy(movies, 0, todayMovies, 0, movies.length - 1);
        return todayMovies;
    }
    public static Movies[] getTomorrowMovies() {
        Movies[] tomorrowMovies = new Movies[movies.length-2];
        System.arraycopy(movies, 2, tomorrowMovies, 0, movies.length - 2);
        return tomorrowMovies;
    }
    public static Movies getMovie(int id) {
        if (id <= movies.length && id > 0) {
            return movies[id - 1];
        }
        return new Movies("Dummy",
                "Action / 0 min",
                R.drawable.movie1, Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ"),
                "ScreenY",
                "Amana Mall",
                "Hall 2",
                "22:15");
    }

    public static int getMovieIndex(Movies movie) {
        for (int i=0; i<movies.length; i++) {
            if (Objects.equals(movies[i].name, movie.name) && Objects.equals(movies[i].genre, movie.genre)) {
                return i+1;
            }
        }
        return 0;
    }
    public static void ShowBooking(Context c, int id, String date) {
        Intent i = new Intent(c, SeatSelectionActivity.class);
        i.putExtra("id_key", id);
        i.putExtra("date_key", date);
        c.startActivity(i);
    }
}
