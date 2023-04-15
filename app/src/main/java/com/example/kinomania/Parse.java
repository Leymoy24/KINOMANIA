package com.example.kinomania;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.kinomania.MainActivity;
import com.example.kinomania.Parser.ParseCinemaAddresses;
import com.example.kinomania.Parser.ParseCinemaNames;
import com.example.kinomania.Parser.ParseFilmCountry;
import com.example.kinomania.Parser.ParseFilmDescription;
import com.example.kinomania.Parser.ParseFilmGenres;
import com.example.kinomania.Parser.ParseFilmImage;
import com.example.kinomania.Parser.ParseFilmNames;
import com.example.kinomania.Parser.ParseFilmPrice;
import com.example.kinomania.Parser.ParseFilmSessions;
import com.example.kinomania.Parser.ParseFilmURL;
import com.google.android.gms.tasks.Task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Parse extends AsyncTask<Void, Void, Void> {
    public String BaseUrl = "https://msk.kinoafisha.info/cinema/";

    public List<String> Name;
    public List<String> CinemaUrl;
    public List<String> Address;
    public List<String> FilmName;
    public List<String> FilmCountry;
    public List<String> Session;
    public List<String> Price;
    public List<String> Genre;
    public List<String> Description;
    public List<String> FilmUrl;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Document doc = Jsoup.connect(BaseUrl).get();
            CinemaWorker(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void CinemaWorker(Document document) throws IOException {
        ParseCinemaNames cinemaNames = new ParseCinemaNames();
        ParseCinemaAddresses cinemaAddresses = new ParseCinemaAddresses();
        Name = cinemaNames.ParseName(document);
        CinemaUrl = cinemaNames.ParseUrls(document);
        for (String name : Name)
        {
            //Console.WriteLine(name); должны в БД помещать
        }

        for (int i = 0; i < CinemaUrl.size(); i++)
        {
            document = Jsoup.connect(CinemaUrl.get(i)).get();
            Address.add(cinemaAddresses.ParseAddress(document));
        }

        FilmWorker(CinemaUrl);
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

    private void FilmWorker(List<String> CinemaUrl) throws IOException
    {
        ParseFilmNames filmNames = new ParseFilmNames();
        ParseFilmGenres filmgenre = new ParseFilmGenres();
        ParseFilmImage filmimage = new ParseFilmImage();
        ParseFilmURL filmurl = new ParseFilmURL();
        ParseFilmSessions filmsessions = new ParseFilmSessions();
        ParseFilmPrice filmprice = new ParseFilmPrice();
        ParseFilmDescription filmdescription = new ParseFilmDescription();
        ParseFilmCountry filmCountry = new ParseFilmCountry();
        List<String> days = SetData();
        for(String cinemaurl : CinemaUrl)
        {
            for(String day : days) // получаю список фильмов на сегодня, завтра, послезавтра, послепослезавтра
            {
                String ScheduleUrl = "schedule/?date=";
                String MovieUrl = "&order=movie";
                String ListFilmURL = cinemaurl + ScheduleUrl + day + MovieUrl;
                Document document = Jsoup.connect(ListFilmURL).get();

                Document document_image = Jsoup.connect("https://www.film.ru/soon/year/2023").get();
                FilmName = filmNames.ParseName(document);
                FilmCountry = filmCountry.ParseCountry(document);

                /*for (int k = 5; k < FilmName.size(); k++)
                {
                    String ImageUrl = filmimage.ParseImage(document_image, FilmName.get(k));
                    ImageUrl= filmimage.ParseImage((IHtmlDocument)document);
                }*/
                for(String name : FilmName)
                {
                    Elements items = document.select("[data-schedulesearch-item*='" + name + "'] span.session_time");
                    Elements prices = document.select("[data-schedulesearch-item*='" + name + "'] span.session_price");

                    for (Element item : items)
                    {
                        Session.add(item.text());
                    }
                    for(Element price : prices)
                    {
                        Price.add(price.text());
                    }
                    for (int k = 0; k < Session.size(); k++)
                    {
                        //Console.WriteLine("   " + Session[k].ToString() + "   " + Price[k].ToString());

                    }
                    Session.clear();
                    Price.clear();
                }
                Genre = filmgenre.ParseGenre(document);
                Description = null;
                FilmUrl = filmurl.ParseUrlOfFilm(document);
                for (String url : FilmUrl)
                {
                    document = Jsoup.connect(url).get();
                    Description = Collections.singletonList(filmdescription.ParseDescription(document));
                }

                FilmName.clear();
                FilmUrl.clear();
                Genre.clear();
                Description.clear();
            }

        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}
