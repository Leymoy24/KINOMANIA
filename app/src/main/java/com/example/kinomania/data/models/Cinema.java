package com.example.kinomania.data.models;

public class Cinema {
    private String name, address, url, favStatus, key_id;

    public Cinema(String name, String address, String url, String key_id, String favStatus){
        this.name = name;
        this.address = address;
        this.url = url;
        this.favStatus = favStatus;
        this.key_id = key_id;
    }

    public String getFavStatus() {
        return favStatus;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
