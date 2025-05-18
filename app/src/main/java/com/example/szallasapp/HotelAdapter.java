package com.example.szallasapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private List<Hotel> hotelList;
    private Context context;

    public HotelAdapter(List<Hotel> hotelList, Context context) {
        this.hotelList = hotelList;
        this.context = context;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_item, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.bind(hotel);
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    class HotelViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, locationTextView, priceTextView;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.hotelName);
            locationTextView = itemView.findViewById(R.id.hotelLocation);
            priceTextView = itemView.findViewById(R.id.hotelPrice);
        }

        void bind(Hotel hotel) {
            nameTextView.setText(hotel.getName());
            locationTextView.setText(hotel.getLocation());
            priceTextView.setText(hotel.getPrice() + " Ft");

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, HotelDetailsActivity.class);
                intent.putExtra("name", hotel.getName());
                intent.putExtra("location", hotel.getLocation());
                intent.putExtra("price", hotel.getPrice());
                intent.putExtra("description", hotel.getDescription());
                context.startActivity(intent);
            });
        }
    }
}
