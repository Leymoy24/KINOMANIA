package com.example.kinomania.data.models;

public class Favourite {
    private String name, address, key_id;

    public Favourite(String name, String address, String key_id) {
        this.name = name;
        this.address = address;
        this.key_id = key_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getKey_id() {
        return key_id;
    }


}
