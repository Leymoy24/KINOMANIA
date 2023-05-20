package com.example.kinomania.data.databases;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.kinomania.data.models.Cinema;
import com.example.kinomania.data.models.Film;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FirebaseDB {
    private DatabaseReference myRef;
    com.google.firebase.database.FirebaseDatabase database;

    public FirebaseDB() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Cinemas");
    }

    public void addDataCinemas(ArrayList<Cinema> cinemaItems){
        int size = cinemaItems.size();
        for(int i = 0; i < size; i++)
        {
            myRef.child(cinemaItems.get(i).getKey_id()).push().setValue(cinemaItems.get(i));
        }
    }

    public void addDataCinemaElements(String date, ArrayList<Film> filmItems){
        ArrayList<Film> items = new ArrayList<>();

        for(int l = 0; l < filmItems.size(); l++)
        {
            items.add(filmItems.get(l));
        }

        int size = items.size();
        for(int i = 0; i < size; i++){
            myRef.child(items.get(i).getNameCinema()).child(date).push().setValue(items.get(i));
        }
        items.clear();
    }

    public void addDataFilms(ArrayList<Film> filmItems){
        int size = filmItems.size();
        for(int i=0;i<size;i++){
            myRef.child(filmItems.get(i).getKey()).push().setValue(filmItems.get(i));
        }
    }

    public void removeDatabase(){
        myRef.removeValue();
    }

}
