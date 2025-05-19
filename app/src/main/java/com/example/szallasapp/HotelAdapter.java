package com.example.szallasapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private void deleteHotelFromDatabase(String hotelId, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("hotels").document(hotelId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    hotelList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Hotel sikeresen törölve", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Törlés sikertelen: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    class HotelViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, locationTextView, priceTextView;

        Button editButton, deleteButton;

        private final boolean showActions;

        public HotelViewHolder(@NonNull View itemView, boolean showActions) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.hotelName);
            locationTextView = itemView.findViewById(R.id.hotelLocation);
            priceTextView = itemView.findViewById(R.id.hotelPrice);
            this.showActions = showActions;

            if (showActions) {
                editButton = itemView.findViewById(R.id.editButton);
                deleteButton = itemView.findViewById(R.id.deleteButton);
            }
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
                    intent.putExtra("userid", hotel.getUserId());
                    context.startActivity(intent);
                });
            }

            if (showActions) {
                nameTextView.setText(hotel.getName());
                locationTextView.setText(hotel.getLocation());

                editButton.setOnClickListener(v -> {
                    Intent intent = new Intent(context, EditHotelActivity.class);
                    intent.putExtra("hotel_id", hotel.getUid());
                    context.startActivity(intent);
                });

                deleteButton.setOnClickListener(v -> {
                    // Call a listener to delete the item
                    new AlertDialog.Builder(context)
                            .setTitle("Biztosan törölni szeretnéd?")
                            .setMessage("Ez a művelet nem vonható vissza.")
                            .setPositiveButton("Törlés", (dialog, which) -> {
                                deleteHotelFromDatabase(hotel.getUid(), getAdapterPosition());
                            })
                            .setNegativeButton("Mégse", null)
                            .show();
//                    Toast.makeText(context, "Törlés: " + hotel.getUid(), Toast.LENGTH_SHORT).show();
                });
            }
        }
    }
}
