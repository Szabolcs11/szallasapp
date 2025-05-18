package com.example.szallasapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeText;
    private ViewFlipper imageSlider;

    private RecyclerView recyclerView;
    private HotelAdapter hotelAdapter;
    private List<Hotel> hotelList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Főoldal - Szálláshely lefoglaló app");
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        hotelList = new ArrayList<>();
        hotelAdapter = new HotelAdapter(hotelList, this);
        recyclerView.setAdapter(hotelAdapter);
        FirebaseApp.initializeApp(this);
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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_hotels) {
                startActivity(new Intent(this, HotelsActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else {
                return false;
            }
        });

        loadHotelsFromFirestore();
    }

    private void loadHotelsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Ellenőrizd, hogy a db nem null!
        if (db != null) {
            db.collection("hotels") // Kollekció neve
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot documentSnapshots = task.getResult();
                            hotelList.clear();
                            for (DocumentSnapshot document : documentSnapshots) {
                                Hotel hotel = document.toObject(Hotel.class);
                                hotelList.add(hotel);
                            }
                            hotelAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("Firestore", "Hiba történt: ", task.getException());
                        }
                    });
        } else {
            Log.e("Firestore", "FirebaseFirestore példány null!");
        }
    }
}
