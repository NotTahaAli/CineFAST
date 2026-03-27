package com.l230954.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;

public class SnacksActivity extends AppCompatActivity {
    MaterialButton btnConfirm;
    ArrayList<Snack> snacks;
    ListView lvSnacks;
    SnacksAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_snacks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        hookButtons();
    }

    private void hookButtons() {
        btnConfirm.setOnClickListener(v->{
            Intent i = new Intent();
            i.putExtra("snacks_key", snacks);
            setResult(RESULT_OK, i);
            finish();
        });
    }
    private void init() {
        btnConfirm = findViewById(R.id.btnConfirm);
        lvSnacks = findViewById(R.id.lvSnacks);
        snacks = new ArrayList<>();
        snacks.add(new Snack(getResources().getString(R.string.snacks1), 5.0f, getResources().getString(R.string.snacks1_description), R.drawable.snacks1));
        snacks.add(new Snack(getResources().getString(R.string.snacks2), 10.0f, getResources().getString(R.string.snacks2_description), R.drawable.snacks2));
        snacks.add(new Snack(getResources().getString(R.string.snacks3), 15.0f, getResources().getString(R.string.snacks3_description), R.drawable.snacks3));
        snacks.add(new Snack(getResources().getString(R.string.snacks4), 2.5f, getResources().getString(R.string.snacks4_description), R.drawable.snacks4));
        adapter = new SnacksAdapter(this, snacks);
        lvSnacks.setAdapter(adapter);
    }
}