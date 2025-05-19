package com.example.szallasapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HotelDetailsActivity extends AppCompatActivity {

    private boolean isReserved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_hotel_details);

        TextView nameTextView = findViewById(R.id.detailName);
        TextView locationTextView = findViewById(R.id.detailLocation);
        TextView priceTextView = findViewById(R.id.detailPrice);
        TextView descriptionTextView = findViewById(R.id.detailDescription);
        Button reserveButton = findViewById(R.id.reserveButton);

        String name = getIntent().getStringExtra("name");
        String location = getIntent().getStringExtra("location");
        String price = getIntent().getStringExtra("price");
        String description = getIntent().getStringExtra("description");
        String userId = getIntent().getStringExtra("userid");

        nameTextView.setText(name);
        locationTextView.setText(location);
        priceTextView.setText(price + " Ft");
        descriptionTextView.setText(description);

        reserveButton.setOnClickListener(v -> {
            if (isReserved) {
                Toast.makeText(HotelDetailsActivity.this, "Már lefoglaltad ezt a szállást!", Toast.LENGTH_SHORT).show();
            } else {
                isReserved = true;
                Toast.makeText(HotelDetailsActivity.this, "Sikeresen lefoglaltad a szállást", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
