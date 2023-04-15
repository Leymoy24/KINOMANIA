package com.example.kinomania;

import java.util.ArrayList;

public class Film {
    private String name, genre, country, description, imageUrl, filmUrl;
    private ArrayList<String> prices;
    private ArrayList<String> sessions;

    public Film(String name, String genre, String country, String description, String imageUrl, String filmUrl, ArrayList<String> prices, ArrayList<String> sessions) {
        this.name = name;
        this.genre = genre;
        this.country = country;
        this.description = description;
        this.imageUrl = imageUrl;
        this.filmUrl = filmUrl;
        this.prices = prices;
        this.sessions = sessions;
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
