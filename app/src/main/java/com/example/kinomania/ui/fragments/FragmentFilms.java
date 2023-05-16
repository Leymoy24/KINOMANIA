package com.example.kinomania.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinomania.data.models.Cinema;
import com.example.kinomania.data.models.Film;
import com.example.kinomania.ui.activities.MainActivity;
import com.example.kinomania.Parse;
import com.example.kinomania.Parser.FilmSettings;
import com.example.kinomania.Parser.ParseCinemaNames;
import com.example.kinomania.R;
import com.example.kinomania.ui.adapters.FilmsAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FragmentFilms extends Fragment {

    MainActivity mainActivity;
    public String BaseUrl = "https://msk.kinoafisha.info/movies/";
    private RecyclerView recyclerView;
    private FilmsAdapter adapter;
    private ArrayList<Film> filmItems = new ArrayList<>();
    private ProgressBar progressBar;
    SearchView searchView;

    public FragmentFilms() {
        super(R.layout.fragment_films);
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
        searchView = view.findViewById(R.id.search_film);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

        adapter = new FilmsAdapter(mainActivity, filmItems);
        recyclerView.setAdapter(adapter);

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        FragmentFilms.Content content = new FragmentFilms.Content();
        content.execute();
    }

    private void filterList(String text) {
        ArrayList<Film> filterFilms = new ArrayList<>();
        for(Film item: filmItems){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filterFilms.add(item);
            }
        }

        if(filterFilms.isEmpty()){
            Toast.makeText(getContext(), "Не найдено!", Toast.LENGTH_SHORT).show();
        } else{
            adapter.setFilteredFilms(filterFilms);
        }
    }

    private List<Film> getFilmItems() {
        return filmItems;
    }

    public void setFilmItems(List<Film> filmItemsNoReplay) {
        filmItems.addAll(filmItemsNoReplay);
    }

    public class Content extends AsyncTask<Void, Void, Void> {

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
            try {
                Document doc = Jsoup.connect(BaseUrl).get();
                Elements NameOfFilm = doc.select("a.movieItem_title");
                Elements GenreOfFilm = doc.select("span.movieItem_genres");
                Elements ProductionOfFilm = doc.select("span.movieItem_year");

                List<String> Urls = new ArrayList<>();
                for (Element UrlOfFilm : NameOfFilm){
                    String value = UrlOfFilm.attr("href");
                    Log.i("Logcat", "Url of film: " + value);
                    Urls.add(value);
                }
                int size = Urls.size();
                Log.i("Logcat", "Size of urls: " + Urls.size());
                for (int i = 0; i < 20; i++)
                {
                    Log.i("Logcat", "Title of film: " + NameOfFilm.get(i).text());
                    Log.i("Logcat", "Genre of film: " + GenreOfFilm.get(i).text());
                    Log.i("Logcat", "Production of film: " + ProductionOfFilm.get(i).text());
                    Document document = Jsoup.connect(Urls.get(i)).get();
                    Element DescriptionOfFilm = document.selectFirst("div.visualEditorInsertion.filmDesc_editor.more_content").selectFirst("p");
                    Log.i("Logcat", "Description: " + DescriptionOfFilm);
                    Element itemsFilmPoster = document.selectFirst("a.filmInfo_posterLink");
                    String image_link = itemsFilmPoster.attr("href");
                    Log.i("Logcat", "Url image: " + image_link);

                    String key_id = String.valueOf(i + 1);

                    Film film = new Film(key_id, NameOfFilm.get(i).text(), GenreOfFilm.get(i).text(), ProductionOfFilm.get(i).text(),
                            DescriptionOfFilm.text(), image_link, Urls.get(i));
                    filmItems.add(film);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled(Void unused) {
            super.onCancelled(unused);
        }
    }
}