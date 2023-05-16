package com.example.kinomania.ui.adapters;

import static android.util.Log.i;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinomania.data.models.Film;
import com.example.kinomania.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentCinemaElementAdapter extends RecyclerView.Adapter<FragmentCinemaElementAdapter.CinemasElementViewHolder> {

    Context context;
    String date;
    String ScheduleUrl = "?date=";
    String MovieUrl = "&order=cinemas";
    private List<Film> films;

    public FragmentCinemaElementAdapter(Context context, List<Film> films){
        this.context = context;
        this.films = films;
    }

    @NonNull
    @Override
    public CinemasElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View filmItems = LayoutInflater.from(context).inflate(R.layout.film_and_session_item, parent, false);
        return new FragmentCinemaElementAdapter.CinemasElementViewHolder(filmItems);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemasElementViewHolder holder, int position) {
        Film film = films.get(position);
        holder.filmName.setText(film.getName());
        holder.filmGenre.setText(film.getGenre());
        holder.filmCountry.setText(film.getCountry());
        Picasso.get().load(film.getFilmImage()).into(holder.filmPoster);

        ArrayList<String> prices = new ArrayList<>();
        ArrayList<String> sessions = new ArrayList<>();
        prices.addAll(film.getPrices());
        sessions.addAll(film.getSessions());
        String delim = "\n";
        String resPrices = String.join(delim, prices);
        String resSessions = String.join(delim, sessions);
        holder.sessionPrice.setText(resPrices);
        holder.sessionTime.setText(resSessions);

        Calendar dt = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        date = df.format(dt.getTime());
        
        holder.itemView.setOnClickListener(v->{
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(film.getFilmUrl() + ScheduleUrl + date + MovieUrl));
            Log.i("Logcat", "go to webpage of " + film.getFilmUrl() + ScheduleUrl + date + MovieUrl);
            startActivity(context, browserIntent, null);
            //Toast.makeText(this, "Расписание фильма не найдено", Toast.LENGTH_SHORT).show();
        });

    }

    public static class CinemasElementViewHolder extends RecyclerView.ViewHolder{
        TextView filmName, filmGenre, filmCountry, sessionTime, sessionPrice;
        ImageView filmPoster;
        View view;
        public CinemasElementViewHolder(View filmItems) {
            super(filmItems);
            view = filmItems.findViewById(R.id.dividerView);
            filmName = filmItems.findViewById(R.id.name_of_film);
            filmGenre = filmItems.findViewById(R.id.genre_of_film);
            filmCountry = filmItems.findViewById(R.id.country_of_production);
            sessionTime = filmItems.findViewById(R.id.timeTextView);
            sessionPrice = filmItems.findViewById(R.id.moneyTextView);
            filmPoster = filmItems.findViewById(R.id.image_of_film);
        }
    }

    @Override
    public int getItemCount() {
        return films.size();
    }
}
