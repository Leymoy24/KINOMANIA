package com.example.kinomania.ui.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinomania.data.models.Cinema;
import com.example.kinomania.data.models.Film;
import com.example.kinomania.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.FilmsViewHolder> {

    Context context;
    private List<Film> films;

    public FilmsAdapter(Context context, List<Film> Films) {
        this.context = context;
        this.films = Films;
    }

    public void setFilteredFilms(ArrayList<Film> filterFilms){
        this.films = filterFilms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View filmItems = LayoutInflater.from(context).inflate(R.layout.film_item, parent, false);
        return new FilmsAdapter.FilmsViewHolder(filmItems);
    }

    public static class FilmsViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, genreTextView, countryTextView, descriptionTextView, descrTextView;
        ImageView imageFilm;
        View view;
        public FilmsViewHolder(@NonNull View filmItems) {
            super(filmItems);
            descrTextView = filmItems.findViewById(R.id.textDescription);
            view = filmItems.findViewById(R.id.dividerView);
            nameTextView = filmItems.findViewById(R.id.name_of_film);
            genreTextView = filmItems.findViewById(R.id.genre_of_film);
            countryTextView = filmItems.findViewById(R.id.country_of_production);
            imageFilm = filmItems.findViewById(R.id.image_of_film);
            descriptionTextView = filmItems.findViewById(R.id.description);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FilmsViewHolder holder, int position) {
        Film film = films.get(position);
        holder.nameTextView.setText(film.getName());
        holder.genreTextView.setText(film.getGenre());
        holder.countryTextView.setText(film.getCountry());
        holder.descriptionTextView.setText(film.getDescription());
        Picasso.get().load(film.getFilmImage()).into(holder.imageFilm);

        holder.itemView.setOnClickListener(v->{
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(film.getFilmUrl()));
            Log.i("Logcat", "go to webpage of " + film.getFilmUrl());
            startActivity(context, browserIntent, null);
            //Toast.makeText(this, "Расписание фильма не найдено", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return films.size();
    }
}
