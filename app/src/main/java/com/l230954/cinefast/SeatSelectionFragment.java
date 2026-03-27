package com.l230954.cinefast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class SeatSelectionFragment extends Fragment {

    Activity activity;
    int movieId;
    Movies movie;
    String date;
    boolean isToday;

    FragmentController controller;

    MaterialButton snacksBtn, bookBtn;
    ImageView btnBack, ivBackground;
    TextView tvTheater, tvHall, tvDate, tvTime, tvScreen, tvTitle;
    GridLayout grid;
    ImageView selectedSeat, placeholderSeat, availableSeat, bookedSeat, disabledSeat;
    HashMap<Character, HashMap<Integer, Boolean>> seatSelectionMap;
    int selectedCount;

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args == null) return;
        loadDate();
        hookButtons();
        setMovieData();
        populateGrid();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seat_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = requireActivity();

        init(view);
        loadDate();
        hookButtons();
        setMovieData();
        loadViews();
        populateGrid();
    }

    private void loadDate() {
        Bundle args = this.getArguments();
        if (args == null) {
            movieId = 0;
            date = CalenderHelper.GetDate(0);
        } else {
            movieId = args.getInt("id_key", 0);
            date = args.getString("date_key");
        }
        if (date == null) activity.finish();

        isToday = date.equals(CalenderHelper.GetDate(0));

        movie = MoviesDirectory.getMovie(movieId);
    }

    private void hookButtons() {
        btnBack.setOnClickListener(v-> controller.showHome());
        if (isToday) {
            bookBtn.setText(R.string.book_seats);
            bookBtn.setEnabled(true);
            bookBtn.setOnClickListener(v -> {
                if (selectedCount <= 0) {
                    Toast.makeText(activity, "Please select at least one seat to book", Toast.LENGTH_SHORT).show();
                    return;
                }
                confirmBooking(false);
            });
            snacksBtn.setText(R.string.button_snack);
            snacksBtn.setOnClickListener(v -> {
                if (selectedCount <= 0) {
                    Toast.makeText(activity, "Please select at least one seat to book", Toast.LENGTH_SHORT).show();
                    return;
                }
                confirmBooking(true);
            });
        } else {
            bookBtn.setText(R.string.coming_soon);
            bookBtn.setEnabled(false);
            snacksBtn.setText(R.string.trailer);
            snacksBtn.setOnClickListener(v -> {
                try {
                    Intent i = Intent.parseUri(movie.trailer.toString(), 0);
                    activity.startActivity(i);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void init(View parentView) {
        snacksBtn = parentView.findViewById(R.id.snacksBtn);
        bookBtn = parentView.findViewById(R.id.bookBtn);
        btnBack = parentView.findViewById(R.id.btnBack);
        ivBackground = parentView.findViewById(R.id.ivBackground);

        tvDate = parentView.findViewById(R.id.tvDate);
        tvTheater = parentView.findViewById(R.id.tvTheater);
        tvHall = parentView.findViewById(R.id.tvHall);
        tvTime = parentView.findViewById(R.id.tvTime);
        tvTitle = parentView.findViewById(R.id.tvTitle);
        tvScreen = parentView.findViewById(R.id.tvScreen);

        grid = parentView.findViewById(R.id.grid);

        controller = (FragmentController) this.getActivity();
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

        selectedSeat = new ImageView(activity);
        selectedSeat.setImageResource(R.drawable.seat_selected);
        selectedSeat.setLayoutParams(params);

        placeholderSeat = new ImageView(activity);
        placeholderSeat.setImageResource(R.drawable.seat_placeholder);
        placeholderSeat.setLayoutParams(params);

        availableSeat = new ImageView(activity);
        availableSeat.setImageResource(R.drawable.seat_available);
        availableSeat.setLayoutParams(params);

        bookedSeat = new ImageView(activity);
        bookedSeat.setImageResource(R.drawable.seat_booked);
        bookedSeat.setLayoutParams(params);

        disabledSeat = new ImageView(activity);
        disabledSeat.setImageResource(R.drawable.seat_disabled);
        disabledSeat.setLayoutParams(params);
    }

    private void addSeat(ImageView seat) {
        ImageView newSeat = new ImageView(activity);
        newSeat.setImageDrawable(seat.getDrawable());
        newSeat.setLayoutParams(seat.getLayoutParams());

        grid.addView(newSeat);
    }
    private ImageView setSeat(ImageView seat, int index) {
        ImageView newSeat = new ImageView(activity);
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
            var seatRow = seatSelectionMap.get(rowLetter);
            assert seatRow != null;
            if (!seatRow.containsKey(seatNumber)) {
                seatRow.put(seatNumber, false);
            }
            if (Boolean.TRUE.equals(seatRow.get(seatNumber))) {
                seatRow.put(seatNumber, false);
                selectedCount--;
                newSeat = placeSeat(row, col, availableSeat);
            } else {
                seatRow.put(seatNumber, true);
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
            if (!isToday) {
                placeSeat(row, col, disabledSeat);
            } else {
                int randomVal = (int) (Math.random() * 10);
                if (randomVal < 8) {
                    ImageView placedSeat = placeSeat(row, col, availableSeat);
                    if (isToday) hookSeatClick(placedSeat, row, col, seatNumber);
                } else {
                    placeSeat(row, col, bookedSeat);
                }
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

    private void confirmBooking(Boolean askSnacks) {
        ArrayList<String> seats = new ArrayList<>();
        for (char row: seatSelectionMap.keySet()) {
            var seatRow = seatSelectionMap.get(row);
            assert seatRow != null;
            for (int seat: seatRow.keySet()) {
                if (Boolean.TRUE.equals(seatRow.get(seat))) {
                    seats.add("Row " + row + ", Seat " + seat);
                }
            }
        }
        if (askSnacks) {
            controller.showSnacks(movie, date, seats);
        } else {
            controller.showBookingConfirmation(movie, date, seats, null);
        }
    }
}