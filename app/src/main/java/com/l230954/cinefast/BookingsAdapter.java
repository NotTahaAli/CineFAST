package com.l230954.cinefast;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class BookingsAdapter extends FirebaseRecyclerAdapter<Bookings, BookingsAdapter.ViewHolder> {

    private final Context context;
    public BookingsAdapter(@NonNull FirebaseRecyclerOptions<Bookings> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Bookings model) {
        DatabaseReference ref = getRef(position);
        Movies movie = MoviesDirectory.getMovieFromName(model.getMovie());
        holder.ivMoviePoster.setImageBitmap(movie.image);
        holder.tvMovieTitle.setText(model.getMovie());
        holder.tvBookingTime.setText(model.getDate() + ", " + model.getTime());
        holder.tvTicketCount.setText(model.getTickets() + " Tickets");
        try {
            if (CalenderHelper.hasPassed(model.getDate(), model.getTime())) {
                holder.ivRemoveBooking.setVisibility(INVISIBLE);
            } else {
                holder.ivRemoveBooking.setVisibility(VISIBLE);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        final boolean[] buttonEnabled = {true};
        holder.ivRemoveBooking.setOnClickListener(v -> {
            if (!buttonEnabled[0]) return;
            try {
                if (CalenderHelper.hasPassed(model.getDate(), model.getTime())) {
                    Toast.makeText(context, "This booking has already passed", Toast.LENGTH_SHORT).show();
                    holder.ivRemoveBooking.setVisibility(INVISIBLE);
                    return;
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            buttonEnabled[0] = false;
            ref.removeValue().addOnFailureListener(e->{
                buttonEnabled[0] = true;
                Toast.makeText(context, "Failed to remove booking, " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(unused -> {
                Toast.makeText(context, "Booking removed successfully.", Toast.LENGTH_SHORT).show();
                buttonEnabled[0] = true;
            });
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_line_item, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMoviePoster, ivRemoveBooking;
        TextView tvMovieTitle, tvBookingTime, tvTicketCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.ivMoviePoster);
            ivRemoveBooking = itemView.findViewById(R.id.ivRemoveBooking);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvBookingTime = itemView.findViewById(R.id.tvBookingTime);
            tvTicketCount = itemView.findViewById(R.id.tvTicketCount);
        }
    }
}
