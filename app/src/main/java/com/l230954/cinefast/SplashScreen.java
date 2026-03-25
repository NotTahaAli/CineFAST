package com.l230954.cinefast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private ImageView ivLogo;
    private Animation logo_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        applyAnimation();
        moveToOnboarding();
    }

    private void moveToOnboarding() {
        new Handler().postDelayed(()->{
            Intent i = new Intent(this, OnboardingActivity.class);
            startActivity(i);
            finish();
        }, 5000);
    }

    private void applyAnimation() {
        ivLogo.startAnimation(logo_anim);
    }

    private void init() {
        ivLogo = findViewById(R.id.ivLogo);
        logo_anim = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
    }
}