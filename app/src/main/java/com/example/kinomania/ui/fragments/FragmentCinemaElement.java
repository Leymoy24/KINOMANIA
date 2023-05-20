package com.example.kinomania.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kinomania.data.models.Cinema;
import com.example.kinomania.data.models.Film;
import com.example.kinomania.ui.activities.MainActivity;
import com.example.kinomania.R;
import com.example.kinomania.ui.adapters.CinemasAdapter;
import com.example.kinomania.ui.adapters.FragmentCinemaElementAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentCinemaElement extends Fragment {

    public String urlOfCinema;
    public ArrayList<String> days;
    public String date;
    private RecyclerView recyclerView;
    private FragmentCinemaElementAdapter adapter;
    private ArrayList<Film> filmItems = new ArrayList<>();
    String CinemaName;
    MainActivity mainActivity;
    private DatabaseReference myRef;
    com.google.firebase.database.FirebaseDatabase database;

    public FragmentCinemaElement() {
        super(R.layout.fragment_cinema_element);
        days = new ArrayList<>();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Cinemas");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cinema_element, container, false);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view_films_sessions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

        TextView cinemaName, cinemaAddress, emptyTextView;
        cinemaName = view.findViewById(R.id.cinemaNameTextView);
        cinemaAddress = view.findViewById(R.id.cinemaAddressTextView);
        emptyTextView = view.findViewById(R.id.EmptyTextView);

        Button data_first, data_second, data_third;
        data_first = view.findViewById(R.id.dataFirstButton);
        data_second = view.findViewById(R.id.dataSecondButton);
        data_third = view.findViewById(R.id.dataThirdButton);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("cinemaName"))
                cinemaName.setText(bundle.getString("cinemaName"));
                CinemaName = bundle.getString("cinemaName");
            if(bundle.containsKey("cinemaAddress"))
               cinemaAddress.setText(bundle.getString("cinemaAddress"));
            if(bundle.containsKey("cinemaUrl"))
                urlOfCinema = bundle.getString("cinemaUrl");
        }

        SetData();
        data_first.setText(days.get(0));
        data_second.setText(days.get(1));
        data_third.setText(days.get(2));

        Calendar dt = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        date = df.format(dt.getTime());

        Query query = myRef;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filmItems.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child(CinemaName).child(date).getChildren()) {
                    Film film = dataSnapshot1.getValue(Film.class);
                    filmItems.add(film);
                }

                adapter = new FragmentCinemaElementAdapter(mainActivity, filmItems);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if (filmItems.isEmpty())
                    emptyTextView.setVisibility(View.VISIBLE);
                else
                    emptyTextView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        data_first.setOnClickListener(v->{
            Log.i("Logcat", "tap on 1 button " + date);
            filmItems.clear();
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    filmItems.clear();
                    for (DataSnapshot dataSnapshot1 : snapshot.child(CinemaName).child(date).getChildren()) {
                        Film film = dataSnapshot1.getValue(Film.class);
                        filmItems.add(film);
                    }

                    adapter = new FragmentCinemaElementAdapter(mainActivity, filmItems);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (filmItems.isEmpty())
                        emptyTextView.setVisibility(View.VISIBLE);
                    else
                        emptyTextView.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        data_second.setOnClickListener(v->{
            dt.roll(Calendar.DATE, 1);
            date = df.format(dt.getTime());
            Log.i("Logcat", "tap on 2 button " + date);
            filmItems.clear();
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    filmItems.clear();
                    for (DataSnapshot dataSnapshot1 : snapshot.child(CinemaName).child(date).getChildren()) {
                        Film film = dataSnapshot1.getValue(Film.class);
                        filmItems.add(film);
                    }

                    adapter = new FragmentCinemaElementAdapter(mainActivity, filmItems);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (filmItems.isEmpty())
                        emptyTextView.setVisibility(View.VISIBLE);
                    else
                        emptyTextView.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            dt.roll(Calendar.DATE, -1);
        });

        data_third.setOnClickListener(v->{
            dt.roll(Calendar.DATE, 2);
            date = df.format(dt.getTime());
            Log.i("Logcat", "tap on 3 button " + date);
            filmItems.clear();
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    filmItems.clear();
                    for (DataSnapshot dataSnapshot1 : snapshot.child(CinemaName).child(date).getChildren()) {
                        Film film = dataSnapshot1.getValue(Film.class);
                        filmItems.add(film);
                    }

                    adapter = new FragmentCinemaElementAdapter(mainActivity, filmItems);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (filmItems.isEmpty())
                        emptyTextView.setVisibility(View.VISIBLE);
                    else
                        emptyTextView.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            dt.roll(Calendar.DATE, -2);
        });


    }

    private void SetData()
    {
        Calendar dt = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("d MMM\nE");
        days.add(df.format(dt.getTime()));
        for (int i = 0; i < 2; i++)
        {
            dt.roll(Calendar.DATE, 1);
            days.add(df.format(dt.getTime()));
        }
    }
}