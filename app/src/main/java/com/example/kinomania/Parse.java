package com.example.kinomania;

import com.example.kinomania.Parser.CinemaSettings;
import com.example.kinomania.Parser.FilmSettings;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Parse {
    public String BaseUrl = "https://msk.kinoafisha.info/cinema/";

    public List<CinemaSettings> cinemaSettings_;
    public List<FilmSettings> filmSettings_;
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
    public List<String> FilmImage;

    public Collection<? extends CinemaSettings> CinemaWorker(Document document) throws IOException {
        ParseCinemaNames cinemaNames = new ParseCinemaNames();
        ParseCinemaAddresses cinemaAddresses = new ParseCinemaAddresses();
        Name = cinemaNames.ParseName(document);
        CinemaUrl = cinemaNames.ParseUrls(document);

        for (int i = 0; i < CinemaUrl.size(); i++)
        {
            document = Jsoup.connect(CinemaUrl.get(i)).get();
            Address.add(cinemaAddresses.ParseAddress(document));
        }

        for(int i = 0; i< Name.size(); i++){
            CinemaSettings cinemaSettings = new CinemaSettings(CinemaUrl.get(i), Name.get(i), Address.get(i));
            cinemaSettings_.add(cinemaSettings);
        }

        return cinemaSettings_;
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

    public Collection<? extends FilmSettings> FilmWorker(List<String> CinemaUrl, String date) throws IOException
    {
        ParseFilmNames filmNames = new ParseFilmNames();
        ParseFilmGenres filmgenre = new ParseFilmGenres();
        ParseFilmImage filmImage = new ParseFilmImage();
        ParseFilmURL filmurl = new ParseFilmURL();
        ParseFilmSessions filmsessions = new ParseFilmSessions();
        ParseFilmPrice filmprice = new ParseFilmPrice();
        ParseFilmDescription filmdescription = new ParseFilmDescription();
        ParseFilmCountry filmCountry = new ParseFilmCountry();
        for(String cinemaurl : CinemaUrl)
        {
            String ScheduleUrl = "schedule/?date=";
            String MovieUrl = "&order=movie";
            String ListFilmURL = cinemaurl + ScheduleUrl + date + MovieUrl;
            Document document = Jsoup.connect(ListFilmURL).get();

            FilmName = filmNames.ParseName(document);
            FilmCountry = filmCountry.ParseCountry(document);
            Genre = filmgenre.ParseGenre(document);
            FilmUrl = filmurl.ParseUrlOfFilm(document);

            Description = null;

            for(int i = 0; i <FilmUrl.size(); i++)
            {
                Document document_ = Jsoup.connect(FilmUrl.get(i)).get();
                Description.addAll(Collections.singletonList(filmdescription.ParseDescription(document_)));
                FilmImage.addAll(Collections.singletonList(filmImage.ParseImage(document, FilmName.get(i))));
            }

            for(int i = 0; i< FilmName.size(); i++)
            {
                Elements items = document.select("[data-schedulesearch-item*='" + FilmName.get(i) + "'] span.session_time");
                Elements prices = document.select("[data-schedulesearch-item*='" + FilmName.get(i) + "'] span.session_price");

                for (Element item : items)
                {
                    Session.add(item.text());
                }
                for(Element price : prices)
                {
                    Price.add(price.text());
                }

                FilmSettings filmSettings = new FilmSettings(FilmName.get(i), date, Genre.get(i), Description.get(i),
                        FilmImage.get(i), FilmUrl.get(i), FilmCountry.get(i), Session, Price);
                        filmSettings_.add(filmSettings);

                Session.clear();
                Price.clear();
            }

            Description.clear();
            FilmName.clear();
            FilmUrl.clear();
            Genre.clear();
            FilmCountry.clear();;
        }
        return filmSettings_;
    }
}
