package com.example.szallasapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, goToRegisterButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Szálláshely lefoglaló app");
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        goToRegisterButton = findViewById(R.id.goToRegisterButton);

        loginButton.setOnClickListener(v -> loginUser());
        goToRegisterButton.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Írj be emailt és jelszót", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Sikeres bejelentkezés!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(tas -> {
                                    if (!tas.isSuccessful()) {
                                        Log.w("FCM", "Fetching FCM registration token failed", tas.getException());
                                        return;
                                    }

                                    // Token lekérve
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    String token = tas.getResult();
                                    saveTokenToFirestore(userId, token);
                                });

                        finish();
                    } else {
                        Toast.makeText(this, "Hiba: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveTokenToFirestore(String userId, String token) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Store the token under the user’s document in the "users" collection
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("fcmToken", token);

        db.collection("users").document(userId) // Create or update the user's document
                .set(userMap, SetOptions.merge()) // Merge so that other fields are not overwritten
                .addOnSuccessListener(aVoid -> Log.d("FCM", "Token saved to Firestore"))
                .addOnFailureListener(e -> Log.w("FCM", "Error saving token", e));
    }
}