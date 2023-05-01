package com.example.kinomania.Parser;

public class CinemaSettings {
    public String CinemaUrl;
    public String Name;
    public String Address;

    public CinemaSettings( String cinemaUrl, String name, String address) {
        CinemaUrl = cinemaUrl;
        Name = name;
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getCinemaUrl() {
        return CinemaUrl;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setCinemaUrl(String cinemaUrl) {
        CinemaUrl = cinemaUrl;
    }

    public void setName(String name) {
        Name = name;
    }
}
