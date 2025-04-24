package com.example.szallasapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private List<Hotel> hotelList;

    // Constructor
    public HotelAdapter(List<Hotel> hotelList) {
        this.hotelList = hotelList;
    }

    @Override
    public HotelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_item, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.name.setText(hotel.getName());
        holder.description.setText(hotel.getDescription());
        holder.location.setText(hotel.getLocation());
        holder.price.setText("$" + hotel.getPrice());

        // Ha van kép, töltsd be a képet (Glide vagy Picasso segítségével)
        Glide.with(holder.itemView.getContext()).load(hotel.getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, location, price;
        ImageView image;

        public HotelViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.hotelName);
            description = itemView.findViewById(R.id.hotelDescription);
            location = itemView.findViewById(R.id.hotelLocation);
            price = itemView.findViewById(R.id.hotelPrice);
            image = itemView.findViewById(R.id.hotelImage);
        }
    }
}
