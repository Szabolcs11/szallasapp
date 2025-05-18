package com.example.szallasapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditHotelActivity extends AppCompatActivity {
    private EditText nameEditText, locationEditText, descriptionEditText, priceEditText;
    private Button saveButton;
    private String hotelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hotel);

        nameEditText = findViewById(R.id.editHotelName);
        locationEditText = findViewById(R.id.editHotelLocation);
        descriptionEditText = findViewById(R.id.editHotelDescription);
        priceEditText = findViewById(R.id.editHotelPrice);
        saveButton = findViewById(R.id.saveHotelButton);

        hotelId = getIntent().getStringExtra("hotel_id");

        FirebaseFirestore.getInstance().collection("hotels").document(hotelId)
                .get().addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        nameEditText.setText(doc.getString("name"));
                        locationEditText.setText(doc.getString("location"));
                        descriptionEditText.setText(doc.getString("description"));
                        priceEditText.setText(doc.getString("price"));
                    }
                });

        saveButton.setOnClickListener(v -> {
            String updatedName = nameEditText.getText().toString();
            String updatedLocation = locationEditText.getText().toString();
            String updatedDescription = descriptionEditText.getText().toString();
            String updatedPrice = priceEditText.getText().toString();

            FirebaseFirestore.getInstance().collection("hotels").document(hotelId)
                    .update("name", updatedName, "location", updatedLocation, "description", updatedDescription, "price", updatedPrice)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Szállás frissítve!", Toast.LENGTH_SHORT).show();
                        finish(); // return to previous screen
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Hiba a frissítéskor", Toast.LENGTH_SHORT).show());
        });
    }
}
