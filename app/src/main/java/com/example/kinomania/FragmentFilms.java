package com.example.kinomania;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinomania.Parser.FilmSettings;
import com.example.kinomania.Parser.ParseCinemaNames;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FragmentFilms extends Fragment {

    MainActivity mainActivity;
    Parse parse;
    public String BaseUrl = "https://msk.kinoafisha.info/cinema/";
    public List<String> CinemaUrl;
    private RecyclerView recyclerView;
    private FilmsAdapter adapter;
    private ArrayList<Film> filmItems = new ArrayList<>();
    private ArrayList<FilmSettings> filmSettings = new ArrayList<>();
    private ProgressBar progressBar;

    public FragmentFilms() {
        super(R.layout.fragment_films);
        parse = new Parse();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_films, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBarFilm);
        recyclerView = view.findViewById(R.id.recycler_view_films);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        adapter = new FilmsAdapter(mainActivity, filmItems);
    }

    public class Content_ extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(mainActivity, androidx.preference.R.anim.abc_fade_in));
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(mainActivity, androidx.preference.R.anim.abc_fade_out));
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Document doc = Jsoup.connect(BaseUrl).get();
                CinemaUrl = ParseCinemaNames.ParseUrls(doc);
                List<String> dates = new ArrayList<>();
                dates.addAll(SetData());
                for(int i = 0; i < dates.size(); i++){
                    filmSettings.addAll(parse.FilmWorker(CinemaUrl, dates.get(i)));
                }
                for(int i = 0; i < filmSettings.size(); i++){ // ниже перечисляем по порядку
                    Film film = new Film(filmSettings.get(i).getFilmName(), filmSettings.get(i).getGenre(), filmSettings.get(i).getFilmCountry(),
                            filmSettings.get(i).getDescription(), filmSettings.get(i).getImageUrl(),
                            filmSettings.get(i).getFilmUrl(), (ArrayList<String>) filmSettings.get(i).getPrice(), (ArrayList<String>) filmSettings.get(i).getSession());
                    filmItems.add(film);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        private List<String> SetData()
        {
            Calendar dt = Calendar.getInstance();
            List<String> days = null;
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            days.add(df.format(dt.getTime()));
            for (int i = 0; i < 4; i++)
            {
                dt.roll(Calendar.DATE, 1);
                days.add(df.format(dt.getTime()));
            }

            return days;
        }

        @Override
        protected void onCancelled(Void unused) {
            super.onCancelled(unused);
        }
    }
}