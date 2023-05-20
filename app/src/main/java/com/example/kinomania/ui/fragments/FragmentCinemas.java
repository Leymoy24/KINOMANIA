package com.example.kinomania.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinomania.data.models.Cinema;
import com.example.kinomania.ui.activities.MainActivity;
import com.example.kinomania.Parse;
import com.example.kinomania.Parser.CinemaSettings;
import com.example.kinomania.R;
import com.example.kinomania.ui.adapters.CinemasAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class FragmentCinemas extends Fragment {

    MainActivity mainActivity;
    private RecyclerView recyclerView;
    private CinemasAdapter adapter;
    private ArrayList<Cinema> cinemaItems;
    private ArrayList<CinemaSettings> cinemaSettings = new ArrayList<>();
    private ProgressBar progressBar;
    private SearchView searchView;
    private DatabaseReference myRef;
    com.google.firebase.database.FirebaseDatabase database;


    public FragmentCinemas(){
        super(R.layout.fragment_cinemas);
        cinemaItems = new ArrayList<>();
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
        return inflater.inflate(R.layout.fragment_cinemas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBarCinema);
        recyclerView = view.findViewById(R.id.recycler_view_cinemas);
        searchView = view.findViewById(R.id.search_cinema);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(AnimationUtils.loadAnimation(mainActivity, androidx.preference.R.anim.abc_fade_in));



        Query query = myRef;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cinemaItems.clear();
                for(int i = 0; i < 20; i++){
                    for (DataSnapshot dataSnapshot1 : snapshot.child(String.valueOf(i)).getChildren()) {
                        Cinema cinema = dataSnapshot1.getValue(Cinema.class);
                        cinemaItems.add(cinema);
                    }
                }
                adapter = new CinemasAdapter(mainActivity, cinemaItems);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        progressBar.setVisibility(View.GONE);
        progressBar.startAnimation(AnimationUtils.loadAnimation(mainActivity, androidx.preference.R.anim.abc_fade_out));

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String text) {
        ArrayList<Cinema> filterCinemas = new ArrayList<>();
        for(Cinema item: cinemaItems){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filterCinemas.add(item);
            }
        }

        if(filterCinemas.isEmpty()){
            Toast.makeText(getContext(), "Не найдено!", Toast.LENGTH_SHORT).show();
        } else{
            adapter.setFilteredCinemas(filterCinemas);
        }
    }

    public class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(mainActivity, androidx.preference.R.anim.abc_fade_in));
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(mainActivity, androidx.preference.R.anim.abc_fade_out));
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onCancelled(Void unused) {
            super.onCancelled(unused);
        }
    }
}