package com.example.szallasapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HotelDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_hotel_details);

        TextView nameTextView = findViewById(R.id.detailName);
        TextView locationTextView = findViewById(R.id.detailLocation);
        TextView priceTextView = findViewById(R.id.detailPrice);
        TextView descriptionTextView = findViewById(R.id.detailDescription);

        String name = getIntent().getStringExtra("name");
        String location = getIntent().getStringExtra("location");
        String price = getIntent().getStringExtra("price");
        String description = getIntent().getStringExtra("description");

        nameTextView.setText(name);
        locationTextView.setText(location);
        priceTextView.setText(price + " Ft");
        descriptionTextView.setText(description);
    }
}
