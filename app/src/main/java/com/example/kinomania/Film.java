package com.example.kinomania;

public class Film {
    private String name, genre, country;
    private int filmImage;

    public Film(String name, String genre, String country, int image){
        this.name = name;
        this.genre = genre;
        this.country = country;
        this.filmImage = image;
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

    public void setFilmImage(int filmImage) {
        this.filmImage = filmImage;
    }

    public int getFilmImage() {
        return filmImage;
    }
}
