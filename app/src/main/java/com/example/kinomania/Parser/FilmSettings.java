package com.example.kinomania.Parser;

import java.util.Calendar;
import java.util.List;


public class FilmSettings{
    public String ScheduleUrl = "schedule/?date="; // мб сделать приватными поля...
    public String MovieUrl = "&order=movie";
    public String FilmName;
    public String DateTime;
    public String Genre;
    public String Description;
    public String ImageUrl;
    public String FilmUrl;
    public String FilmCountry;
    public List<String> Session;
    public List<String> Price;

    public FilmSettings(String filmName,
                        String dateTimes, String genre, String description,
                        String imageUrl, String filmUrl, String filmCountry, List<String> session, List<String> price) {
        FilmName = filmName;
        DateTime = dateTimes;
        Genre = genre;
        Description = description;
        ImageUrl = imageUrl;
        FilmUrl = filmUrl;
        FilmCountry = filmCountry;
        Session = session;
        Price = price;
    }

    public String getFilmCountry() {
        return FilmCountry;
    }

    public String getFilmName() {
        return FilmName;
    }

    public String getDateTimes() {
        return DateTime;
    }

    public String getGenre() {
        return Genre;
    }

    public String getMovieUrl() {
        return MovieUrl;
    }

    public String getDescription() {
        return Description;
    }

    public String getFilmUrl() {
        return FilmUrl;
    }

    public String getScheduleUrl() {
        return ScheduleUrl;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public List<String> getSession() {
        return Session;
    }

    public List<String> getPrice() {
        return Price;
    }
}
