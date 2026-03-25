package com.l230954.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.HashMap;

public class SnacksActivity extends AppCompatActivity {
    MaterialButton btnConfirm;
    HashMap<String, Integer> snacks;
    TextView tvSnack1, tvSnack2, tvSnack3;
    ImageView ivAddBtn1, ivAddBtn2, ivAddBtn3, ivRemoveBtn1, ivRemoveBtn2, ivRemoveBtn3;
    int totalSnacks;
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
            if (totalSnacks > 0) {
                i.putExtra("snacks_key", snacks);
            }
            setResult(RESULT_OK, i);
            finish();
        });
        ivAddBtn1.setOnClickListener(v->{
            String name = getName(1);
            int count = getCount(name);
            ++count;
            tvSnack1.setText(Integer.toString(count));
            snacks.put(name, count);
            if (count > 0) ivRemoveBtn1.setImageResource(R.drawable.remove_active);
            totalSnacks++;
        });
        ivAddBtn2.setOnClickListener(v->{
            String name = getName(2);
            int count = getCount(name);
            ++count;
            tvSnack2.setText(Integer.toString(count));
            snacks.put(name, count);
            if (count > 0) ivRemoveBtn2.setImageResource(R.drawable.remove_active);
            totalSnacks++;
        });
        ivAddBtn3.setOnClickListener(v->{
            String name = getName(3);
            int count = getCount(name);
            ++count;
            tvSnack3.setText(Integer.toString(count));
            snacks.put(name, count);
            if (count > 0) ivRemoveBtn3.setImageResource(R.drawable.remove_active);
            totalSnacks++;
        });

        ivRemoveBtn1.setOnClickListener(v->{
            String name = getName(1);
            int count = getCount(name);
            if (count <= 0) return;
            --count;
            tvSnack1.setText(Integer.toString(count));
            snacks.put(name, count);
            if (count <= 0) ivRemoveBtn1.setImageResource(R.drawable.remove);
            totalSnacks++;
        });
        ivRemoveBtn2.setOnClickListener(v->{
            String name = getName(2);
            int count = getCount(name);
            if (count <= 0) return;
            --count;
            tvSnack2.setText(Integer.toString(count));
            snacks.put(name, count);
            if (count <= 0) ivRemoveBtn2.setImageResource(R.drawable.remove);
            totalSnacks++;
        });
        ivRemoveBtn3.setOnClickListener(v->{
            String name = getName(3);
            int count = getCount(name);
            if (count <= 0) return;
            --count;
            tvSnack3.setText(Integer.toString(count));
            snacks.put(name, count);
            if (count <= 0) ivRemoveBtn3.setImageResource(R.drawable.remove);
            totalSnacks++;
        });
    }

    private String getName(int id) {
        switch (id) {
            case 1:
                return getString(R.string.snacks1);
            case 2:
                return getString(R.string.snacks2);
            case 3:
                return getString(R.string.snacks3);
            default:
                return null;
        }
    }
    private int getCount(String name) {
        if (!snacks.containsKey(name)) {
            snacks.put(name, 0);
        }
        return snacks.get(name);
    }

    private void init() {
        btnConfirm = findViewById(R.id.btnConfirm);

        totalSnacks = 0;
        snacks = new HashMap<>();

        tvSnack1 = findViewById(R.id.tvSnack1);
        tvSnack2 = findViewById(R.id.tvSnack2);
        tvSnack3 = findViewById(R.id.tvSnack3);

        ivAddBtn1 = findViewById(R.id.ivAddBtn1);
        ivAddBtn2 = findViewById(R.id.ivAddBtn2);
        ivAddBtn3 = findViewById(R.id.ivAddBtn3);

        ivRemoveBtn1 = findViewById(R.id.ivRemoveBtn1);
        ivRemoveBtn2 = findViewById(R.id.ivRemoveBtn2);
        ivRemoveBtn3 = findViewById(R.id.ivRemoveBtn3);
    }
}