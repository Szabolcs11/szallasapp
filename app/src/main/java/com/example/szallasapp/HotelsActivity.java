package com.example.szallasapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class HotelsActivity extends AppCompatActivity {

    private HotelAdapter hotelAdapter;
    private List<Hotel> hotelList;
    private RecyclerView recyclerView;

    private EditText editTextLocation, editTextMinPrice, editTextMaxPrice;
    private Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Szálláshely lefoglaló app");
        setContentView(R.layout.hotels_view);

        FirebaseApp.initializeApp(this);

        hotelList = new ArrayList<>();
        hotelAdapter = new HotelAdapter(hotelList, this, false);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(hotelAdapter);

        editTextLocation = findViewById(R.id.editTextLocation);
        editTextMinPrice = findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = findViewById(R.id.editTextMaxPrice);
        buttonSearch = findViewById(R.id.buttonSearch);

        buttonSearch.setOnClickListener(v -> {
            String location = editTextLocation.getText().toString().trim();
            String minPriceStr = editTextMinPrice.getText().toString().trim();
            String maxPriceStr = editTextMaxPrice.getText().toString().trim();

            Double minPrice = TextUtils.isEmpty(minPriceStr) ? null : Double.parseDouble(minPriceStr);
            Double maxPrice = TextUtils.isEmpty(maxPriceStr) ? null : Double.parseDouble(maxPriceStr);

            queryHotelsFromFirestore(location, minPrice, maxPrice);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_hotels);
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
            } else if (itemId == R.id.nav_newhotel) {
                startActivity(new Intent(this, NewHotelActivity.class));
                return true;
            } else {
                return false;
            }
        });

        queryHotelsFromFirestore(null, null, null); // Load all hotels initially
    }

    private void queryHotelsFromFirestore(String location, Double minPrice, Double maxPrice) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("hotels");

        if (!TextUtils.isEmpty(location)) {
            query = query.whereEqualTo("location", location);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    hotelList.clear();
                    task.getResult().forEach(document -> {
                        Hotel hotel = document.toObject(Hotel.class);
                        hotelList.add(hotel);
                    });
                    hotelAdapter.notifyDataSetChanged();
                } else {
                    Log.e("Firestore", "Hiba történt: ", task.getException());
                }
            });
            return;
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                hotelList.clear();
                task.getResult().forEach(document -> {
                    Hotel hotel = document.toObject(Hotel.class);
                    hotelList.add(hotel);
                });
                hotelAdapter.notifyDataSetChanged();
            } else {
                Log.e("Firestore", "Hiba történt: ", task.getException());
            }
        });
    }
}
