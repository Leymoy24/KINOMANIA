package com.example.kinomania;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.FilmsViewHolder> {

    Context context;
    private List<Film> films;
    private FilmsAdapter.OnItemClickListener listener;

    public FilmsAdapter(Context context, List<Film> Films) {
        this.context = context;
        this.films = Films;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CinemasAdapter.OnItemClickListener onItemClickListener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View filmItems = LayoutInflater.from(context).inflate(R.layout.film_item, parent, false);
        return new FilmsAdapter.FilmsViewHolder(filmItems);
    }

    public static class FilmsViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, genreTextView, countryTextView;
        ImageView imageFilm;
        public FilmsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_of_film);
            genreTextView = itemView.findViewById(R.id.genre_of_film);
            countryTextView = itemView.findViewById(R.id.country_of_production);
            imageFilm = itemView.findViewById(R.id.image_of_film);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FilmsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Film film = films.get(position);
        holder.nameTextView.setText(film.getName());
        holder.genreTextView.setText(film.getGenre());
        holder.countryTextView.setText(film.getCountry());
        //holder.imageFilm.setImage(film.getFilmImage());

        /* Обработчик нажатия на элемент
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return films.size();
    }
}
