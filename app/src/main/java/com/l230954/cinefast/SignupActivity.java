package com.l230954.cinefast;

import static com.l230954.cinefast.PasswordHelper.getPasswordError;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    ImageView btnBack;
    EditText etEmail, etPassword, etConfirmPassword, etName;
    Button btnSignup;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        btnBack.setOnClickListener(v -> finish());

        btnSignup.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            String name = etName.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(this, "Name must not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.isEmpty()) {
                Toast.makeText(this, "Email must not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Password must not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (confirmPassword.isEmpty()) {
                Toast.makeText(this, "Confirm Password must not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            String passwordError = getPasswordError(password);
            if (passwordError != null) {
                Toast.makeText(this, passwordError, Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    assert user != null;
                    String userId = user.getUid();
                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("name", name);
                    userData.put("email", email);
                    Log.i("SignupActivity", "userId: " + userId);
                    databaseReference.child("users").child(userId).setValue(userData)
                        .addOnSuccessListener(smth -> {
                            Log.i("SignupActivity", "Signup success");
                            Toast.makeText(this, "Signup success", Toast.LENGTH_SHORT).show();
                            editor.putBoolean("rememberMe", true);
                            editor.apply();
                            finish();
                        })
                        .addOnFailureListener(e->{
                            Log.e("SignupActivity", "Signup failed: " + e.getMessage());
                            Toast.makeText(this, "Signup failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            user.delete();
                        });
                })
                .addOnFailureListener(e->{
                    Toast.makeText(this, "Signup failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        });
    }

    private void init() {
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignup = findViewById(R.id.btnSignup);
        btnBack = findViewById(R.id.btnBack);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(getString(R.string.firebase_database_url));
        databaseReference = database.getReference();

        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}
