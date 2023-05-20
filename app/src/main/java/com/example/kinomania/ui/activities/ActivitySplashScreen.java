package com.example.kinomania.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.kinomania.R;
import com.example.kinomania.data.databases.FirebaseDB;
import com.example.kinomania.data.models.Cinema;
import com.example.kinomania.data.models.Film;
import com.example.kinomania.ui.fragments.FragmentCinemas;
import com.example.kinomania.ui.fragments.FragmentFilms;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitySplashScreen extends AppCompatActivity {

    FirebaseDB firebaseDB;
    public String BaseUrl = "https://msk.kinoafisha.info/cinema/";
    public String BaseUrlFilms = "https://msk.kinoafisha.info/movies/";
    private ArrayList<Cinema> cinemaItems = new ArrayList<>();
    private ArrayList<Film> filmItems = new ArrayList<>();
    private ArrayList<Film> filmItems_an = new ArrayList<>();
    ArrayList<String> listOfUrls;
    FragmentCinemas fragmentCinemas;
    FragmentFilms fragmentFilms;
    public ArrayList<String> days;
    public String date;
    public Calendar dt;
    public Map<String, ArrayList<Film>> dateFilms;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        CardView logoImageView = findViewById(R.id.ss_cv);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation_splash_screen);
        animation.setRepeatCount(Animation.INFINITE);
        logoImageView.startAnimation(animation);

        dateFilms = new HashMap<>();
        firebaseDB = new FirebaseDB();

        dt = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String currentDate = df.format(dt.getTime());

        sharedPreferences = getPreferences(MODE_PRIVATE);
        String previousDate = sharedPreferences.getString("CurrentData", "");
        Log.i("Logcat", "previous Date" + previousDate);
        Log.i("Logcat", "current date" + currentDate);

        if(!currentDate.equals(previousDate)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("CurrentData", currentDate);
            editor.commit();
            firebaseDB.removeDatabase();
            ContentCinemas content = new ContentCinemas();
            content.execute();
        }
        else
        {
            goToMainActivity();
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class ContentCinemas extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            goToMainActivity();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Document doc = Jsoup.connect(BaseUrl).get();
                Log.i("Logcat", "connect to webpage");
                Elements itemsUrl = doc.select("a.cinemaList_ref");
                listOfUrls = new ArrayList<>();
                for(Element url : itemsUrl) {
                    String value = url.attr("href"); // ссылки на кинотеатры
                    listOfUrls.add(value);
                    Log.i("Logcat", "Url: " + value);
                }

                Document docu = Jsoup.connect("https://msk.kinoafisha.info/cinema/map/").get();
                Elements itemsName = docu.select("div.cinemaList_name"); // имена кинотеатров
                Log.i("Logcat", "searched all names");

                Elements addr = docu.select("div.cinemaList_addr.as-mobile"); // адреса
                Log.i("Logcat", "searched all addresses");
                for (int i = 0; i < 20; i++)
                {
                    String key_id = String.valueOf(i + 1);
                    Cinema cinema = new Cinema(itemsName.get(i).text(), addr.get(i).text(), listOfUrls.get(i), key_id, "0");
                    cinemaItems.add(cinema);
                }
                firebaseDB.addDataCinemas(cinemaItems);




                String ScheduleUrl = "schedule/?date=";
                String MovieUrl = "&order=movie";

                ArrayList<String> days = new ArrayList<>();
                dt = Calendar.getInstance();
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                days.add(df.format(dt.getTime()));
                for (int i = 0; i < 2; i++)
                {
                    dt.roll(Calendar.DATE, 1);
                    days.add(df.format(dt.getTime()));
                }

                for(int roll = 0; roll < 3; roll++)
                {
                    date = days.get(roll);
                    Log.i("Logcat", "date: " + date);

                    for(int k = 0; k < 20; k++){
                        String ListFilmURL = listOfUrls.get(k) + ScheduleUrl + date + MovieUrl;
                        Document document = Jsoup.connect(ListFilmURL).get();
                        Log.i("Logcat", "ListFilm Url: " + ListFilmURL);
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
                            Log.i("Logcat", " Cinema Name: " + cinemaItems.get(k).getName());

                            String key = String.valueOf(i + 1);

                            Film film = new Film(key, itemsFilmName.get(i).text(), itemsFilmGenre.get(i).text(),
                                    itemsFilmCountry.get(i).text(), description.get(i), imageLink.get(i),
                                    listOfFilmUrls.get(i), Price, Session, cinemaItems.get(k).getName());
                            filmItems.add(film);




                            Session.clear();
                            Price.clear();
                        }

                        description.clear();
                        imageLink.clear();
                    }
                    //dateFilms.put(date, filmItems);
                    Log.i("Logcat", " date films contains: " + dateFilms.size());
                    firebaseDB.addDataCinemaElements(date, filmItems);
                    filmItems.clear();
                }






                Document doco = Jsoup.connect(BaseUrlFilms).get();
                Elements NameOfFilm = doco.select("a.movieItem_title");
                Elements GenreOfFilm = doco.select("span.movieItem_genres");
                Elements ProductionOfFilm = doco.select("span.movieItem_year");

                List<String> Urls = new ArrayList<>();
                for (Element UrlOfFilm : NameOfFilm){
                    String value = UrlOfFilm.attr("href");
                    Log.i("Logcat", "Url of film: " + value);
                    Urls.add(value);
                }
                int size = Urls.size();
                Log.i("Logcat", "Size of urls: " + Urls.size());
                for (int i = 0; i < 20; i++) {
                    Log.i("Logcat", "Title of film: " + NameOfFilm.get(i).text());
                    Log.i("Logcat", "Genre of film: " + GenreOfFilm.get(i).text());
                    Log.i("Logcat", "Production of film: " + ProductionOfFilm.get(i).text());
                    Document document_p = Jsoup.connect(Urls.get(i)).get();
                    Element DescriptionOfFilm = document_p.selectFirst("div.visualEditorInsertion.filmDesc_editor.more_content").selectFirst("p");
                    Log.i("Logcat", "Description: " + DescriptionOfFilm);
                    Element itemsFilmPoster = document_p.selectFirst("a.filmInfo_posterLink");
                    String image_link = itemsFilmPoster.attr("href");
                    Log.i("Logcat", "Url image: " + image_link);

                    String key_id = "Film" + i;

                    Film film = new Film(key_id, NameOfFilm.get(i).text(), GenreOfFilm.get(i).text(), ProductionOfFilm.get(i).text(),
                            DescriptionOfFilm.text(), image_link, Urls.get(i));
                    filmItems_an.add(film);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            firebaseDB.addDataFilms(filmItems_an);
            return null;
        }
        @Override
        protected void onCancelled(Void unused) {
            super.onCancelled(unused);
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}