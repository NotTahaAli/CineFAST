package com.l230954.cinefast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class BookingConfirmationFragment extends Fragment {
    final float SEAT_COST = 16;

    int movieId;
    Movies movie;
    String date;
    ArrayList<String> seats;
    ArrayList<Snack> snacks;

    ImageView btnBack, ivMovie;
    TextView tvTheater, tvHall, tvDate, tvTime, tvScreen, tvTitle, tvTotal, tvTicketsDetails, tvTicketsPrices, tvSnacksDetails, tvSnacksPrices, tvSnacksHeader;
    MaterialButton btnSend;

    NavigationController controller;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booking_confirmation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        loadData();
        hookButtons();
        setMovieData();
        calculateAndSetBookingData();
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        loadData();
        hookButtons();
        setMovieData();
        calculateAndSetBookingData();
    }

    private void loadData() {
        Bundle args = getArguments();
        if (args == null) {
            movieId = 0;
            date = "";
            seats = new ArrayList<>();
            snacks = null;
        } else {
            movieId = args.getInt("id_key", 0);
            date = args.getString("date_key");
            seats = args.getStringArrayList("seats_key");
            snacks = (ArrayList<Snack>) args.getSerializable("snacks_key");
        }

        movie = MoviesDirectory.getMovie(movieId);
    }

    private void hookButtons() {
        btnBack.setOnClickListener(v -> controller.goBackToHome());
        btnSend.setOnClickListener(v->{
            Intent i = new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_TEXT, getTicketText());
            i.putExtra(Intent.EXTRA_SUBJECT, "CineFAST Ticket");
            i.setType("text/plain");
            
            Intent share = Intent.createChooser(i,  "Choose an App to Share Ticket");

            if (share.resolveActivity(context.getPackageManager()) == null) {
                Toast.makeText(context, "No sharing functionality on your device!", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(share);
            }
        });
    }

    private void init(View view) {
        context = requireContext();
        controller = (NavigationController) requireActivity();
        btnBack = view.findViewById(R.id.btnBack);
        btnSend = view.findViewById(R.id.btnSend);

        ivMovie = view.findViewById(R.id.ivMovie);
        tvDate = view.findViewById(R.id.tvDate);
        tvTheater = view.findViewById(R.id.tvTheater);
        tvHall = view.findViewById(R.id.tvHall);
        tvTime = view.findViewById(R.id.tvTime);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvScreen = view.findViewById(R.id.tvScreen);

        tvTotal = view.findViewById(R.id.tvTotal);
        tvTicketsDetails = view.findViewById(R.id.tvTicketsDetails);
        tvTicketsPrices = view.findViewById(R.id.tvTicketsPrices);
        tvSnacksHeader = view.findViewById(R.id.tvSnacksHeader);
        tvSnacksDetails = view.findViewById(R.id.tvSnacksDetails);
        tvSnacksPrices = view.findViewById(R.id.tvSnacksPrices);
    }

    private void setMovieData() {
        ivMovie.setImageBitmap(movie.image);
        tvHall.setText(movie.hall);
        tvDate.setText(date);
        tvTheater.setText(movie.theater);
        tvTime.setText(movie.time);
        tvScreen.setText(movie.screen);
        tvTitle.setText(movie.name);
    }

    private void calculateAndSetBookingData() {
        if (movieId == 0) return;
        float totalCost = 0;
        StringBuilder Items = new StringBuilder();
        StringBuilder Prices = new StringBuilder();
        boolean first = true;
        for (String seat: seats) {
            if (!first) {
                Items.append("\n");
                Prices.append("\n");
            }
            first = false;
            Items.append(seat);
            Prices.append(CurrencyHelper.formatCurrency(SEAT_COST));
            totalCost += SEAT_COST;
        }
        tvTicketsDetails.setText(Items.toString());
        tvTicketsPrices.setText(Prices.toString());
        if (snacks != null) {
            first = true;
            Items = new StringBuilder();
            Prices = new StringBuilder();
            for (Snack snack: snacks) {
                int count = snack.quantity;
                if (count == 0) continue;
                if (!first) {
                    Items.append("\n");
                    Prices.append("\n");
                }
                Items.append("X").append(count).append(" ").append(snack.name);
                Prices.append(CurrencyHelper.formatCurrency(snack.price*count));
                totalCost += snack.price*count;
                first = false;
            }
            tvSnacksDetails.setText(Items.toString());
            tvSnacksPrices.setText(Prices.toString());
            tvSnacksHeader.setVisibility(VISIBLE);
            tvSnacksPrices.setVisibility(VISIBLE);
            tvSnacksDetails.setVisibility(VISIBLE);
        } else {
            tvSnacksHeader.setVisibility(GONE);
            tvSnacksPrices.setVisibility(GONE);
            tvSnacksDetails.setVisibility(GONE);
        }
        tvTotal.setText(CurrencyHelper.formatCurrency(totalCost));

        SharedPreferences sPref = context.getSharedPreferences("last_booking", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString("name", movie.name);
        editor.putInt("seats", seats.size());
        editor.putFloat("total", totalCost);
        editor.apply();
    }

    private String getTicketText() {
        float totalCost = 0;
        StringBuilder text = new StringBuilder();
        text.append("-------Ticket------")
                .append("\nMovie: ").append(movie.name)
                .append("\nDate:").append(date)
                .append("\nTime:").append(movie.time)
                .append("\nTheater:").append(movie.theater)
                .append("\nHall:").append(movie.hall)
                .append("\n\n-------Seats------");
        for (String seat: seats) {
            text.append("\n").append(seat).append(" (")
                    .append(CurrencyHelper.formatCurrency(SEAT_COST)).append(")");
            totalCost += SEAT_COST;
        }
        if (snacks != null) {
            text.append("\n\n-------Snacks------");
            for (Snack snack: snacks) {
                int count = snack.quantity;
                if (count == 0) continue;
                text.append("\nX").append(count).append(" ").append(snack.name)
                        .append(" (").append(CurrencyHelper.formatCurrency(snack.price*count)).append(")");
                totalCost += snack.price*count;
            }
        }
        text.append("\n\n\nTOTAL: ").append(CurrencyHelper.formatCurrency(totalCost));
        return text.toString();
    }
}