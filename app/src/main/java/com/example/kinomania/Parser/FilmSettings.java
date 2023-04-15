package com.example.kinomania.Parser;

import java.util.Calendar;
import java.util.List;


public class FilmSettings extends CinemaSettings{
    public String ScheduleUrl = "schedule/?date="; // мб сделать приватными поля...
    public String MovieUrl = "&order=movie";
    public List<String> FilmName;
    public List<Calendar> DateTimes;
    public List<String> Genre;
    public List<String> Description;
    public String ImageUrl;
    public List<String> FilmUrl;
    public List<String> Session;
    public List<String> Price;

    public List<String> getFilmName() {
        return FilmName;
    }

    public List<Calendar> getDateTimes() {
        return DateTimes;
    }

    public List<String> getGenre() {
        return Genre;
    }

    public String getMovieUrl() {
        return MovieUrl;
    }

    public List<String> getDescription() {
        return Description;
    }

    public List<String> getFilmUrl() {
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
