package com.example.szallasapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
    private final boolean showActions;

    public HotelAdapter(List<Hotel> hotelList, Context context, boolean showActions) {
        this.hotelList = hotelList;
        this.context = context;
        this.showActions = showActions;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (showActions) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_profile, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_item, parent, false);
        }
        return new HotelViewHolder(view, showActions);
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
        private final boolean showActions;

        public HotelViewHolder(@NonNull View itemView, boolean showActions) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.hotelName);
            locationTextView = itemView.findViewById(R.id.hotelLocation);
            priceTextView = itemView.findViewById(R.id.hotelPrice);
            this.showActions = showActions;
        }

        void bind(Hotel hotel) {
            Log.i("a", hotel.getName());
            if (!showActions) {
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
}
