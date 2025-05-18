package com.example.szallasapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView emailTextView;
    private RecyclerView userHotelsRecyclerView;
    private HotelAdapter hotelAdapter;
    private List<Hotel> userHotels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Profil");
        setContentView(R.layout.activity_profile);

        emailTextView = findViewById(R.id.emailTextView);
        userHotelsRecyclerView = findViewById(R.id.userHotelsRecyclerView);
        userHotelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userHotels = new ArrayList<>();
        hotelAdapter = new HotelAdapter(userHotels, this, true);
        userHotelsRecyclerView.setAdapter(hotelAdapter);

        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        emailTextView.setText("Email: " + currentUserEmail);

        loadUserHotels(currentUserId);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
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
    }

    private void loadUserHotels(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("hotels")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot documents = task.getResult();
                        userHotels.clear();
                        for (DocumentSnapshot doc : documents) {
                            Hotel hotel = doc.toObject(Hotel.class);
                            if (hotel != null) {
                                hotel.setUid(doc.getId());
                                userHotels.add(hotel);
                            }
                        }
                        hotelAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("Profile", "Hiba történt: ", task.getException());
                    }
                });
    }
}
