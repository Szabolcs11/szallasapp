package com.example.szallasapp;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeText;
    private ViewFlipper imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Főoldal - Szálláshely lefoglaló app");
        setContentView(R.layout.activity_main);

        welcomeText = findViewById(R.id.welcomeText);
        imageSlider = findViewById(R.id.imageSlider);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            welcomeText.setText("Üdv, " + user.getEmail());
        }

        Animation slideIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation slideOut = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        imageSlider.setInAnimation(slideIn);
        imageSlider.setOutAnimation(slideOut);
        imageSlider.setFlipInterval(3000);
        imageSlider.startFlipping();
    }
}
