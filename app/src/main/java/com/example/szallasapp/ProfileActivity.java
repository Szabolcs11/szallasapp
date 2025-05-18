package com.example.szallasapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ProfileActivity extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 100;
    private TextView emailTextView;
    private RecyclerView userHotelsRecyclerView;
    private HotelAdapter hotelAdapter;
    private List<Hotel> userHotels;

    private TextView deviceInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Profil");
        setContentView(R.layout.activity_profile);

        deviceInfoTextView = findViewById(R.id.deviceInfoTextView);
        emailTextView = findViewById(R.id.emailTextView);
        userHotelsRecyclerView = findViewById(R.id.userHotelsRecyclerView);
        userHotelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userHotels = new ArrayList<>();
        hotelAdapter = new HotelAdapter(userHotels, this, true);
        userHotelsRecyclerView.setAdapter(hotelAdapter);

        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        emailTextView.setText("Email: " + currentUserEmail);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        emailTextView.startAnimation(fadeIn);


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

        checkPhoneStatePermissionAndShowInfo();
    }

    private void checkPhoneStatePermissionAndShowInfo() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission nincs meg, kérjük be
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_REQUEST_READ_PHONE_STATE);
        } else {
            // Permission megvan, megjelenítjük az infókat
            showDeviceInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_READ_PHONE_STATE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDeviceInfo();
            } else {
                Toast.makeText(this, "Engedély szükséges a telefon állapotának lekéréséhez", Toast.LENGTH_SHORT).show();
                deviceInfoTextView.setText("Telefon állapota: engedély megtagadva.");
            }
        }
    }

    private void showDeviceInfo() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        StringBuilder sb = new StringBuilder();

        sb.append("Android verzió: ").append(Build.VERSION.RELEASE).append("\n");
        sb.append("SDK verzió: ").append(Build.VERSION.SDK_INT).append("\n");
        sb.append("Eszköz modell: ").append(Build.MODEL).append("\n");
        sb.append("Gyártó: ").append(Build.MANUFACTURER).append("\n");

        deviceInfoTextView.setText(sb.toString());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadUserHotels(currentUserId);
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
