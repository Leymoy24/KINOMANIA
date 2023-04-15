package com.example.kinomania;

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

    public List<Cinema> CinemaWorker(Document document) throws IOException {
        ParseCinemaNames cinemaNames = new ParseCinemaNames();
        ParseCinemaAddresses cinemaAddresses = new ParseCinemaAddresses();
        Name = cinemaNames.ParseName(document);
        CinemaUrl = cinemaNames.ParseUrls(document);
        /*for (String name : Name)
        {
            //Console.WriteLine(name); должны в БД помещать
        }*/

        for (int i = 0; i < CinemaUrl.size(); i++)
        {
            document = Jsoup.connect(CinemaUrl.get(i)).get();
            Address.add(cinemaAddresses.ParseAddress(document));
        }

        //как раз тут и надо то же самое делать
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

    public List<Film> FilmWorker(List<String> CinemaUrl) throws IOException
    {
        ParseFilmNames filmNames = new ParseFilmNames();
        ParseFilmGenres filmgenre = new ParseFilmGenres();
        ParseFilmImage filmImage = new ParseFilmImage();
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

                FilmName = filmNames.ParseName(document);
                FilmCountry = filmCountry.ParseCountry(document);

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
                    FilmImage = Collections.singletonList(filmImage.ParseImage(document, name));
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
        } // нужно добавлять в список созданные данные с помошью конструктора в классе Film а затем его вернуть
        // аналогично с Cinema
        return null;
    }
}
