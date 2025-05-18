package com.example.szallasapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewHotelActivity extends AppCompatActivity {

    private EditText nameEditText, descriptionEditText, priceEditText, locationEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_hotel);

        nameEditText = findViewById(R.id.editHotelName);
        descriptionEditText = findViewById(R.id.editHotelDescription);
        priceEditText = findViewById(R.id.editHotelPrice);
        locationEditText = findViewById(R.id.editHotelLocation);
        saveButton = findViewById(R.id.saveHotelButton);

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String priceStr = priceEditText.getText().toString().trim();
            String location = locationEditText.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Kérlek tölts ki minden mezőt!", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Érvénytelen ár!", Toast.LENGTH_SHORT).show();
                return;
            }

            saveHotel(name, description, price, location);
        });
    }

    private void saveHotel(String name, String description, double price, String location) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Save current user's ID with the hotel
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> hotelData = new HashMap<>();
        hotelData.put("name", name);
        hotelData.put("description", description);
        hotelData.put("location", location);
        hotelData.put("price", String.valueOf(price));
        hotelData.put("userId", currentUserId);

        db.collection("hotels")
                .add(hotelData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Szállás hozzáadva!", Toast.LENGTH_SHORT).show();
                    finish();  // close activity and return
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Hiba történt a mentéskor!", Toast.LENGTH_SHORT).show();
                });
    }
}
