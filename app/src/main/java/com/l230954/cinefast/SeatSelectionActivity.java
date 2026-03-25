package com.l230954.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SeatSelectionActivity extends AppCompatActivity {

    int movieId;
    Movies movie;
    String date;

    MaterialButton snacksBtn, bookBtn;
    ImageView btnBack, ivBackground;
    TextView tvTheater, tvHall, tvDate, tvTime, tvScreen, tvTitle;
    GridLayout grid;
    ImageView selectedSeat, placeholderSeat, availableSeat, bookedSeat;
    HashMap<Character, HashMap<Integer, Boolean>> seatSelectionMap;
    int selectedCount;

    ActivityResultLauncher<Intent> snacksLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seat_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        snacksLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleSnacksResult
        );

        init();
        loadData();
        hookButtons();
        setMovieData();
        loadViews();
        populateGrid();
    }

    private void hookButtons() {
        btnBack.setOnClickListener(v->{
            finish();
        });
        bookBtn.setOnClickListener(v->{
            if (selectedCount <= 0) {
                Toast.makeText(this, "Please select at least one seat to book", Toast.LENGTH_SHORT).show();
                return;
            }
            confirmBooking(null);
        });
        snacksBtn.setOnClickListener(v->{
            if (selectedCount <= 0) {
                Toast.makeText(this, "Please select at least one seat to book", Toast.LENGTH_SHORT).show();
                return;
            }
            snacksLauncher.launch(new Intent(this, SnacksActivity.class));
        });
    }

    private void init() {
        snacksBtn = findViewById(R.id.snacksBtn);
        bookBtn = findViewById(R.id.bookBtn);
        btnBack = findViewById(R.id.btnBack);
        ivBackground = findViewById(R.id.ivBackground);

        tvDate = findViewById(R.id.tvDate);
        tvTheater = findViewById(R.id.tvTheater);
        tvHall = findViewById(R.id.tvHall);
        tvTime = findViewById(R.id.tvTime);
        tvTitle = findViewById(R.id.tvTitle);
        tvScreen = findViewById(R.id.tvScreen);

        grid = findViewById(R.id.grid);
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
    }

    private void setMovieData() {
        ivBackground.setImageResource(movie.imageId);
        tvHall.setText(movie.hall);
        tvDate.setText(date);
        tvTheater.setText(movie.theater);
        tvTime.setText(movie.time);
        tvScreen.setText(movie.screen);
        tvTitle.setText(movie.name);
    }

    private void loadViews() {
        float density = getResources().getDisplayMetrics().density;
        int minDimension = Math.min(getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels);
        int margin = (int) (6*density);
        int scale = (minDimension - 10*margin - (int) (20*density))/12;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(scale, scale);
        params.setMargins(margin, margin, margin, margin);

        selectedSeat = new ImageView(this);
        selectedSeat.setImageResource(R.drawable.seat_selected);
        selectedSeat.setLayoutParams(params);

        placeholderSeat = new ImageView(this);
        placeholderSeat.setImageResource(R.drawable.seat_placeholder);
        placeholderSeat.setLayoutParams(params);

        availableSeat = new ImageView(this);
        availableSeat.setImageResource(R.drawable.seat_available);
        availableSeat.setLayoutParams(params);

        bookedSeat = new ImageView(this);
        bookedSeat.setImageResource(R.drawable.seat_booked);
        bookedSeat.setLayoutParams(params);
    }

    private void addSeat(ImageView seat) {
        ImageView newSeat = new ImageView(this);
        newSeat.setImageDrawable(seat.getDrawable());
        newSeat.setLayoutParams(seat.getLayoutParams());

        grid.addView(newSeat);
    }
    private ImageView setSeat(ImageView seat, int index) {
        ImageView newSeat = new ImageView(this);
        newSeat.setImageDrawable(seat.getDrawable());
        newSeat.setLayoutParams(seat.getLayoutParams());

        grid.addView(newSeat, index);
        return newSeat;
    }
    private ImageView placeSeat(int row, int col, ImageView seat) {
        int seats = grid.getChildCount();
        int seatPos = row*9+col;
        if (seats > seatPos) {
            grid.removeViews(seatPos, 1);
        } else {
            int seatsToAdd = seatPos-seats;
            for (int i = 0; i < seatsToAdd; i++) {
                addSeat(placeholderSeat);
            }
        }
        return setSeat(seat, seatPos);
    }

    private void hookSeatClick(ImageView seat, int row, int col, int seatNumber) {
        char rowLetter = (char) ('A' + row);
        seat.setOnClickListener((v)->{
            ImageView newSeat;
            if (!seatSelectionMap.containsKey(rowLetter)) {
                seatSelectionMap.put(rowLetter, new HashMap<>());
            }
            if (!seatSelectionMap.get(rowLetter).containsKey(seatNumber)) {
                seatSelectionMap.get(rowLetter).put(seatNumber, false);
            }
            if (seatSelectionMap.get(rowLetter).get(seatNumber)) {
                seatSelectionMap.get(rowLetter).put(seatNumber, false);
                selectedCount--;
                newSeat = placeSeat(row, col, availableSeat);
            } else {
                seatSelectionMap.get(rowLetter).put(seatNumber, true);
                selectedCount++;
                newSeat = placeSeat(row, col, selectedSeat);
            }
            hookSeatClick(newSeat, row, col, seatNumber);
        });
    }

    private void populateRow(int row) {
        int seatNumber = 1;
        for (int col = 0; col < 9; col++) {
            if (((col == 0 || col == 8) && (row == 0 || row == 7)) || col == 4) continue;
            int randomVal = (int) (Math.random()*10);
            if (randomVal < 8) {
                ImageView placedSeat = placeSeat(row, col, availableSeat);
                hookSeatClick(placedSeat, row, col, seatNumber);
            } else {
                placeSeat(row, col, bookedSeat);
            }
            seatNumber++;
        }
    }

    private void populateGrid() {
        seatSelectionMap = new HashMap<>();
        selectedCount = 0;
        for (int row = 0; row < 8; row++) {
            populateRow(row);
        }
    }

    private void handleSnacksResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_CANCELED) return;
        Intent i = result.getData();
        if (i == null) return;
        HashMap<String, Integer> snacks = (HashMap<String, Integer>) i.getSerializableExtra("snacks_key");
        confirmBooking(snacks);
    }

    private void confirmBooking(@Nullable HashMap<String, Integer> snacks) {
        Intent i = new Intent(this, BookingConfirmationActivity.class);
        ArrayList<String> seats = new ArrayList<>();
        for (char row: seatSelectionMap.keySet()) {
            for (int seat: seatSelectionMap.get(row).keySet()) {
                if (seatSelectionMap.get(row).get(seat) == true) {
                    seats.add("Row " + row + ", Seat " + seat);
                }
            }
        }
        i.putExtra("id_key", movieId)
                .putExtra("date_key", date)
                .putExtra("seats_key", seats)
                .putExtra("snacks_key", snacks);
        startActivity(i);
        finish();
    }
}