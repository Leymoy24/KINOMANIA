package com.example.kinomania.data.models;

import java.util.ArrayList;

public class Film {
    private String name, genre, country, description, imageUrl, filmUrl, key;
    private ArrayList<String> prices;
    private ArrayList<String> sessions;
    private String NameCinema;

    public Film(){};

    public Film(String key, String name, String genre, String country, String description, String imageUrl, String filmUrl, ArrayList<String> prices,
    ArrayList<String> sessions, String nameCinema) {
        this.key = key;
        this.name = name;
        this.genre = genre;
        this.country = country;
        this.description = description;
        this.imageUrl = imageUrl;
        this.filmUrl = filmUrl;
        this.prices = new ArrayList<>();
        this.sessions = new ArrayList<>();
        this.prices.addAll(prices);
        this.sessions.addAll(sessions);
        this.NameCinema = nameCinema;
    }

    public Film(String key_id, String name, String genre, String country, String description, String imageUrl, String filmUrl) {
        this.key = key_id;
        this.name = name;
        this.genre = genre;
        this.country = country;
        this.description = description;
        this.imageUrl = imageUrl;
        this.filmUrl = filmUrl;
    }

    public String getNameCinema() {
        return NameCinema;
    }

    public String getKey() {
        return key;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFilmUrl(String filmUrl) {
        this.filmUrl = filmUrl;
    }

    public void setPrices(ArrayList<String> prices) {
        this.prices = prices;
    }

    public void setSessions(ArrayList<String> sessions) {
        this.sessions = sessions;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getFilmUrl() {
        return filmUrl;
    }

    public ArrayList<String> getPrices() {
        return prices;
    }

    public ArrayList<String> getSessions() {
        return sessions;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setFilmImage(String filmImage) {
        this.imageUrl = filmImage;
    }

    public String getFilmImage() {
        return imageUrl;
    }
}
