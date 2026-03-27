package com.l230954.cinefast;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.net.URISyntaxException;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    Movies[] movies;
    String date;
    public MoviesAdapter(@NonNull Movies[] objects, String date) {
        this.movies = objects;
        this.date = date;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_line_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMovieDescription.setText(movies[position].genre);
        holder.tvMovieTitle.setText(movies[position].name);
        holder.ivMovie.setImageResource(movies[position].imageId);
        holder.trailer = movies[position].trailer.toString();
        holder.movieIndex = MoviesDirectory.getMovieIndex(movies[position]);
    }

    @Override
    public int getItemCount() {
        return movies.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvMovieTitle, tvMovieDescription;
        ImageView ivMovie;
        MaterialButton bookBtn, trailerBtn;
        String trailer;
        int movieIndex;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitile);
            tvMovieDescription = itemView.findViewById(R.id.tvMovieDescription);
            ivMovie = itemView.findViewById(R.id.ivMovie);
            bookBtn = itemView.findViewById(R.id.bookBtn);
            trailerBtn = itemView.findViewById(R.id.trailerBtn);

            trailerBtn.setOnClickListener(v->{
                try {
                    Intent i = Intent.parseUri(trailer, 0);
                    itemView.getContext().startActivity(i);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
