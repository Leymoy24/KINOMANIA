package com.example.kinomania.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kinomania.data.models.Film;
import com.example.kinomania.ui.activities.MainActivity;
import com.example.kinomania.R;
import com.example.kinomania.ui.adapters.FragmentCinemaElementAdapter;

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

public class FragmentCinemaElement extends Fragment {

    public String urlOfCinema;
    public ArrayList<String> days;
    public String date;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FragmentCinemaElementAdapter adapter;
    FragmentFilms fragmentFilms;
    private ArrayList<Film> filmItems = new ArrayList<>();
    private ArrayList<Film> filmItemsNoReplay = new ArrayList<>();
    MainActivity mainActivity;

    public FragmentCinemaElement() {
        super(R.layout.fragment_cinema_element);
        days = new ArrayList<>();
        //fragmentFilms = new FragmentFilms();
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
        return inflater.inflate(R.layout.fragment_cinema_element, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view_films_sessions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

        adapter = new FragmentCinemaElementAdapter(mainActivity, filmItems);
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBarFilm);

        TextView cinemaName, cinemaAddress;
        cinemaName = view.findViewById(R.id.cinemaNameTextView);
        cinemaAddress = view.findViewById(R.id.cinemaAddressTextView);

        Button data_first, data_second, data_third;
        data_first = view.findViewById(R.id.dataFirstButton);
        data_second = view.findViewById(R.id.dataSecondButton);
        data_third = view.findViewById(R.id.dataThirdButton);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("cinemaName"))
                cinemaName.setText(bundle.getString("cinemaName"));
            if(bundle.containsKey("cinemaAddress"))
               cinemaAddress.setText(bundle.getString("cinemaAddress"));
            if(bundle.containsKey("cinemaUrl"))
                urlOfCinema = bundle.getString("cinemaUrl");
        }

        SetData();
        data_first.setText(days.get(0));
        data_second.setText(days.get(1));
        data_third.setText(days.get(2));

        Calendar dt = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        date = df.format(dt.getTime());

        FragmentCinemaElement.Content content = new FragmentCinemaElement.Content();
        content.execute();

        data_first.setOnClickListener(v->{
            Log.i("Logcat", "tap on 1 button " + date);
            filmItems.clear();
            FragmentCinemaElement.Content content_1 = new FragmentCinemaElement.Content();
            content_1.execute();
        });

        data_second.setOnClickListener(v->{
            dt.roll(Calendar.DATE, 1);
            date = df.format(dt.getTime());
            Log.i("Logcat", "tap on 2 button " + date);
            filmItems.clear();
            FragmentCinemaElement.Content content_2 = new FragmentCinemaElement.Content();
            content_2.execute();
            dt.roll(Calendar.DATE, -1);
        });

        data_third.setOnClickListener(v->{
            dt.roll(Calendar.DATE, 2);
            date = df.format(dt.getTime());
            Log.i("Logcat", "tap on 3 button " + date);
            filmItems.clear();
            FragmentCinemaElement.Content content_3 = new FragmentCinemaElement.Content();
            content_3.execute();
            dt.roll(Calendar.DATE, -2);
        });


    }

    private void SetData()
    {
        Calendar dt = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("d MMM\nE");
        days.add(df.format(dt.getTime()));
        for (int i = 0; i < 2; i++)
        {
            dt.roll(Calendar.DATE, 1);
            days.add(df.format(dt.getTime()));
        }
    }


    public class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(mainActivity, "Ожидайте обновления", Toast.LENGTH_SHORT);
            progressBar.startAnimation(AnimationUtils.loadAnimation(mainActivity, androidx.preference.R.anim.abc_fade_in));
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(mainActivity, androidx.preference.R.anim.abc_fade_out));
            adapter.notifyDataSetChanged();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                String ScheduleUrl = "schedule/?date=";
                String MovieUrl = "&order=movie";
                Log.i("Logcat", "date: " + date);

                String ListFilmURL = urlOfCinema + ScheduleUrl + date + MovieUrl;
                Document document = Jsoup.connect(ListFilmURL).get();
                Log.i("Logcat", "connect to webpage");
                Elements itemsFilmName = document.select("span.showtimesMovie_name"); // названия фильмов
                Log.i("Logcat", "parse film names " + itemsFilmName.size());
                Elements itemsFilmGenre = document.select("span.showtimesMovie_categories"); // жанры фильмов
                Log.i("Logcat", "parse film genres");
                Elements itemsFilmCountry = document.select("span.showtimesMovie_details"); // страны производства фильмов
                Log.i("Logcat", "Country film:" + itemsFilmCountry.size());
                Elements itemsFilmUrl = document.select("a.showtimesMovie_link");
                ArrayList<String> listOfFilmUrls = new ArrayList<>();
                for(Element url : itemsFilmUrl) {
                    String value = url.attr("href"); // ссылки на фильмы
                    listOfFilmUrls.add(value);
                    Log.i("Logcat", "Url Film: " + value);
                }

                ArrayList<String> description = new ArrayList<>();
                ArrayList<String> imageLink = new ArrayList<>();

                int size = listOfFilmUrls.size();
                for(int i = 0; i < size; i++)
                {
                    Document document_ = Jsoup.connect(listOfFilmUrls.get(i)).get();
                    Element itemDescription = document_.selectFirst("div.visualEditorInsertion.filmDesc_editor.more_content").selectFirst("p");
                    description.add(itemDescription.text());
                    Log.i("Logcat", "Description film:" + itemDescription.text());
                    Element itemsFilmPoster = document_.selectFirst("div.filmInfo_poster");
                    String image_link = itemsFilmPoster.select("a.filmInfo_posterLink").attr("href");
                    Log.i("Logcat", "Url image:" + image_link);
                    imageLink.add(image_link);
                }

                int size_1 = itemsFilmName.size();
                ArrayList<String> Session = new ArrayList<>();
                ArrayList<String> Price = new ArrayList<>();

                for(int i = 0; i < size_1; i++) {
                    Elements sessions = document.selectFirst("div[data-schedulesearch-item*=" + itemsFilmName.get(i).text() + "]").
                            select("span.session_time");
                    Elements prices = document.selectFirst("div[data-schedulesearch-item*=" + itemsFilmName.get(i).text() + "]").
                            select("span.session_price");

                    for (Element session : sessions) {
                        Session.add(session.text());
                        Log.i("Logcat", "session film: " + Session);
                    }
                    for (Element price : prices) {
                        Price.add(price.text());
                        Log.i("Logcat", "price film: " + Price);
                    }
                    Log.i("Logcat", " parse sessions and prices");

                    String key = String.valueOf(i + 1);

                    Film film = new Film(key, itemsFilmName.get(i).text(), itemsFilmGenre.get(i).text(),
                            itemsFilmCountry.get(i).text(), description.get(i), imageLink.get(i),
                            listOfFilmUrls.get(i), Price, Session);
                    filmItems.add(film);

                    if (!filmItemsNoReplay.contains(film))
                    {
                        filmItemsNoReplay.add(film);
                    }

                    Session.clear();
                    Price.clear();
                }
                description.clear();
                imageLink.clear();
            }catch (IOException e){
                e.printStackTrace();
            }

            //fragmentFilms.setFilmItems(filmItemsNoReplay);
            return null;
        }

        @Override
        protected void onCancelled(Void unused) {
            super.onCancelled(unused);
        }
    }
}