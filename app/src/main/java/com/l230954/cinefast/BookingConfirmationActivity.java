package com.l230954.cinefast;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class BookingConfirmationActivity extends AppCompatActivity {
    final float SEAT_COST = 16;

    int movieId;
    Movies movie;
    String date;
    ArrayList<String> seats;
    ArrayList<Snack> snacks;

    ImageView btnBack, ivMovie;
    TextView tvTheater, tvHall, tvDate, tvTime, tvScreen, tvTitle, tvTotal, tvTicketsDetails, tvTicketsPrices, tvSnacksDetails, tvSnacksPrices, tvSnacksHeader;
    MaterialButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_confirmation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        loadData();
        hookButtons();
        setMovieData();
        calculateAndSetBookingData();
    }

    private void loadData() {
        Intent i = getIntent();
        if (i == null) {
            finish();
            return;
        }
        movieId = i.getIntExtra("id_key", 0);
        date = i.getStringExtra("date_key");
        if (date == null) finish();

        movie = MoviesDirectory.getMovie(movieId);
        seats = i.getStringArrayListExtra("seats_key");
        snacks = (ArrayList<Snack>) i.getSerializableExtra("snacks_key");
    }

    private void hookButtons() {
        btnBack.setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v->{
            Intent i = new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_TEXT, getTicketText());
            i.putExtra(Intent.EXTRA_SUBJECT, "CineFAST Ticket");
            i.setType("text/plain");
            
            Intent share = Intent.createChooser(i,  "Choose an App to Share Ticket");

            if (share.resolveActivity(getPackageManager()) == null) {
                Toast.makeText(this, "No sharing functionality on your device!", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(share);
            }
        });
    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.btnSend);

        ivMovie = findViewById(R.id.ivMovie);
        tvDate = findViewById(R.id.tvDate);
        tvTheater = findViewById(R.id.tvTheater);
        tvHall = findViewById(R.id.tvHall);
        tvTime = findViewById(R.id.tvTime);
        tvTitle = findViewById(R.id.tvTitle);
        tvScreen = findViewById(R.id.tvScreen);

        tvTotal = findViewById(R.id.tvTotal);
        tvTicketsDetails = findViewById(R.id.tvTicketsDetails);
        tvTicketsPrices = findViewById(R.id.tvTicketsPrices);
        tvSnacksHeader = findViewById(R.id.tvSnacksHeader);
        tvSnacksDetails = findViewById(R.id.tvSnacksDetails);
        tvSnacksPrices = findViewById(R.id.tvSnacksPrices);
    }

    private void setMovieData() {
        ivMovie.setImageResource(movie.imageId);
        tvHall.setText(movie.hall);
        tvDate.setText(date);
        tvTheater.setText(movie.theater);
        tvTime.setText(movie.time);
        tvScreen.setText(movie.screen);
        tvTitle.setText(movie.name);
    }

    private void calculateAndSetBookingData() {
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
        } else {
            tvSnacksHeader.setVisibility(GONE);
            tvSnacksPrices.setVisibility(GONE);
            tvSnacksDetails.setVisibility(GONE);
        }
        tvTotal.setText(CurrencyHelper.formatCurrency(totalCost));
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