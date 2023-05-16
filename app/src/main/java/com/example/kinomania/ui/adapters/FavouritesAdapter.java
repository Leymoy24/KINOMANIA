package com.example.kinomania.ui.adapters;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinomania.R;
import com.example.kinomania.data.databases.FavouritesDatabase;
import com.example.kinomania.data.models.Cinema;
import com.example.kinomania.data.models.Favourite;

import java.util.ArrayList;
import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>{

    Context context;
    private List<Favourite> favouriteItems;
    private FavouritesDatabase db;

    public FavouritesAdapter(Context context, List<Favourite> favourites) {
        this.context = context;
        this.favouriteItems = favourites;
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = new FavouritesDatabase(context);
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_item, parent, false);
        return new FavouritesViewHolder(view);
    }

    public class FavouritesViewHolder extends RecyclerView.ViewHolder {
        TextView cinemaTextView, addressTextView;
        ImageButton favouritebtn, goRight;
        public FavouritesViewHolder(@NonNull View itemView) {
            super(itemView);
            cinemaTextView = itemView.findViewById(R.id.name_of_cinema);
            addressTextView = itemView.findViewById(R.id.address_of_cinema);
            favouritebtn = itemView.findViewById(R.id.button_favourites);
            goRight = itemView.findViewById(R.id.button_go_right);

            //убирать из избранного после нажатия на сердечко
            favouritebtn.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                final Favourite favouriteItem = favouriteItems.get(position);
                db.remove_fav(favouriteItem.getKey_id());
                removeItem(position);
            });
        }
    }

    private void removeItem(int position) {
        favouriteItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favouriteItems.size());
    }

    @Override
    public void onBindViewHolder(FavouritesViewHolder holder, int position) {
        final Favourite favouriteItem = favouriteItems.get(position);

        holder.cinemaTextView.setText(favouriteItems.get(position).getName());
        holder.addressTextView.setText(favouriteItems.get(position).getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("cinemaName", favouriteItem.getName());
                bundle.putString("cinemaAddress", favouriteItem.getAddress());
                bundle.putString("cinemaUrl", "favouriteItem.getUrl()");
                Navigation.findNavController(view).navigate(R.id.action_fragmentFavourites_to_fragmentCinemaElement, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteItems.size();
    }
}
