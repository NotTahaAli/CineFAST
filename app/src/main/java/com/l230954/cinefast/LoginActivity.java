package com.l230954.cinefast;

import static com.l230954.cinefast.PasswordHelper.getPasswordError;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnLogin;
    TextView tvRegister;
    EditText etEmail, etPassword;
    CheckBox cbRememberMe;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        checkLoggedIn();

        btnBack.setOnClickListener(v -> finish());
        
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            boolean rememberMe = cbRememberMe.isChecked();

            if (email.isEmpty() || password.isEmpty()) {
                return;
            }

            String passwordError = getPasswordError(password);
            if (passwordError != null) {
                Toast.makeText(this, passwordError, Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    editor.putBoolean("rememberMe", rememberMe);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        });
    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        auth = FirebaseAuth.getInstance();
    }

    private void checkLoggedIn() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;
        if (sharedPreferences.getBoolean("rememberMe", false)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        auth.signOut();
    }
}
