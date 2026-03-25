package com.l230954.cinefast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    Button trailerBtn1, trailerBtn2, trailerBtn3, trailerBtn4;
    Button bookBtn1, bookBtn2, bookBtn3, bookBtn4;
    LinearLayout movie1, movie2, movie3, movie4;

    RadioGroup rgDaySelector;
    RadioButton btnToday, btnTomorrow;

    private int dayOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        hookTrailers();
        hookBooking();

        rgDaySelector.setOnCheckedChangeListener((group, id)->{
            if (id == R.id.btnTomorrow) {
                dayOffset = 1;
                movie1.setVisibility(GONE);
                movie4.setVisibility(GONE);
            } else {
                dayOffset = 0;
                movie1.setVisibility(VISIBLE);
                movie4.setVisibility(VISIBLE);
            }
        });
    }

    private void hookTrailers() {
        trailerBtn1.setOnClickListener((v)->{
            MoviesDirectory.ViewTrailer(this, 1);
        });
        trailerBtn2.setOnClickListener((v)->{
            MoviesDirectory.ViewTrailer(this, 2);
        });
        trailerBtn3.setOnClickListener((v)->{
            MoviesDirectory.ViewTrailer(this, 3);
        });
        trailerBtn4.setOnClickListener((v)->{
            MoviesDirectory.ViewTrailer(this, 4);
        });
    }

    private void hookBooking() {
        bookBtn1.setOnClickListener((v)->{
            MoviesDirectory.ShowBooking(this, 1, CalenderHelper.GetDate(dayOffset));
        });
        bookBtn2.setOnClickListener((v)->{
            MoviesDirectory.ShowBooking(this, 2, CalenderHelper.GetDate(dayOffset));
        });
        bookBtn3.setOnClickListener((v)->{
            MoviesDirectory.ShowBooking(this, 3, CalenderHelper.GetDate(dayOffset));
        });
        bookBtn4.setOnClickListener((v)->{
            MoviesDirectory.ShowBooking(this, 4, CalenderHelper.GetDate(dayOffset));
        });
    }

    private void init() {
        dayOffset = 0;
        rgDaySelector = findViewById(R.id.rgDaySelector);
        btnToday = findViewById(R.id.btnToday);
        btnTomorrow = findViewById(R.id.btnTomorrow);

        movie1 = findViewById(R.id.movie1);
        movie2 = findViewById(R.id.movie2);
        movie3 = findViewById(R.id.movie3);
        movie4 = findViewById(R.id.movie4);

        bookBtn1 = findViewById(R.id.bookBtn1);
        bookBtn2 = findViewById(R.id.bookBtn2);
        bookBtn3 = findViewById(R.id.bookBtn3);
        bookBtn4 = findViewById(R.id.bookBtn4);

        trailerBtn1 = findViewById(R.id.trailerBtn1);
        trailerBtn2 = findViewById(R.id.trailerBtn2);
        trailerBtn3 = findViewById(R.id.trailerBtn3);
        trailerBtn4 = findViewById(R.id.trailerBtn4);
    }
}