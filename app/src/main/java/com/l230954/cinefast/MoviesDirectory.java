package com.l230954.cinefast;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.net.URISyntaxException;

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
    public static Movies getMovie(int id) {
        if (id <= movies.length) {
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

    public static void ViewTrailer(Context c, int id) {
        try {
            Intent i = Intent.parseUri(getMovie(id).trailer.toString(), 0);
            c.startActivity(i);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void ShowBooking(Context c, int id, String date) {
        Intent i = new Intent(c, SeatSelectionActivity.class);
        i.putExtra("id_key", id);
        i.putExtra("date_key", date);
        c.startActivity(i);
    }
}
