package com.example.kinomania.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinomania.data.databases.FavouritesDatabase;
import com.example.kinomania.data.models.Cinema;
import com.example.kinomania.R;
import com.example.kinomania.data.models.Favourite;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CinemasAdapter extends RecyclerView.Adapter<CinemasAdapter.CinemasViewHolder> {

    Context context;
    private static List<Cinema> cinemas;
    private static FavouritesDatabase db;

    public CinemasAdapter(Context context, List<Cinema> Cinemas) {
        this.context = context;
        this.cinemas = Cinemas;
    }

    public void setFilteredCinemas(ArrayList<Cinema> filterCinemas){
        this.cinemas = filterCinemas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CinemasAdapter.CinemasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = new FavouritesDatabase(context);
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if(firstStart){
            createTableOnFirstStart();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.cinema_item, parent, false);
        return new CinemasViewHolder(view);
    }

    private void createTableOnFirstStart() {
        db.insertEmpty();

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    public static class CinemasViewHolder extends RecyclerView.ViewHolder {
        TextView cinemaTextView, addressTextView;
        ImageButton favouritebtn, goRight;
        public CinemasViewHolder(@NonNull View itemView) {
            super(itemView);
            cinemaTextView = itemView.findViewById(R.id.name_of_cinema);
            addressTextView = itemView.findViewById(R.id.address_of_cinema);
            favouritebtn = itemView.findViewById(R.id.button_favourites);
            goRight = itemView.findViewById(R.id.button_go_right);

            favouritebtn.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                Cinema cinemaItem = cinemas.get(position);

                if(cinemaItem.getFavStatus().equals("0")){
                    cinemaItem.setFavStatus("1");
                    db.insertIntoDatabase(cinemaItem.getName(), cinemaItem.getAddress(),
                            cinemaItem.getKey_id(), cinemaItem.getFavStatus());
                    favouritebtn.setImageResource(R.drawable.heart_in_cycle_red);
                } else {
                    cinemaItem.setFavStatus("0");
                    db.remove_fav(cinemaItem.getKey_id());
                    favouritebtn.setImageResource(R.drawable.heart_in_cycle);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CinemasAdapter.CinemasViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Cinema cinema = cinemas.get(position);
        readCursorData(cinema, holder);
        holder.cinemaTextView.setText(cinema.getName());
        holder.addressTextView.setText(cinema.getAddress());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("cinemaName", cinema.getName());
                bundle.putString("cinemaAddress", cinema.getAddress());
                bundle.putString("cinemaUrl", cinema.getUrl());
                Navigation.findNavController(view).navigate(R.id.action_fragmentCinemas_to_fragmentCinemaElement, bundle);
            }
        });
    }

    private void readCursorData(Cinema cinemaItem, @NonNull CinemasAdapter.CinemasViewHolder viewHolder) {
        Cursor cursor = db.read_all_data(cinemaItem.getKey_id());
        SQLiteDatabase dbfav = db.getReadableDatabase();
        try{
            while(cursor.moveToNext()){
                @SuppressLint("Range") String item_fav_status = cursor.getString(cursor.getColumnIndex(db.FAVOURITE_STATUS));
                cinemaItem.setFavStatus(item_fav_status);

                if(item_fav_status != null && item_fav_status.equals("1")){
                    viewHolder.favouritebtn.setImageResource(R.drawable.heart_in_cycle_red);
                } else if(item_fav_status != null && item_fav_status.equals("0")){
                    viewHolder.favouritebtn.setImageResource(R.drawable.heart_in_cycle);
                }
            }
        } finally {
            if(cursor != null && cursor.isClosed()){
                cursor.close();
            }
            db.close();
        }
    }

    @Override
    public int getItemCount() {
        return cinemas.size();
    }
}
