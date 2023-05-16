package com.example.kinomania.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ProgressBar;

import com.example.kinomania.R;
import com.example.kinomania.data.databases.FavouritesDatabase;
import com.example.kinomania.data.models.Cinema;
import com.example.kinomania.data.models.Favourite;
import com.example.kinomania.ui.activities.MainActivity;
import com.example.kinomania.ui.adapters.CinemasAdapter;
import com.example.kinomania.ui.adapters.FavouritesAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class FragmentFavourites extends Fragment {

    MainActivity mainActivity;
    private RecyclerView recyclerView;
    private ArrayList<Favourite> favouriteItems = new ArrayList<>();
    private FavouritesAdapter adapter;
    private FavouritesDatabase db;

    public FragmentFavourites() {
       super(R.layout.fragment_favourites);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new FavouritesDatabase(mainActivity);
        loadData();
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    private void loadData() {
        if(favouriteItems != null){
            favouriteItems.clear();
        }
        SQLiteDatabase dbFav = db.getReadableDatabase();
        Cursor cursor = db.select_all_favorite_list();
        try{
            while(cursor.moveToNext()){
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(db.ITEM_TITLE));
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(db.KEY_ID));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(db.ITEM_ADDRESS));
                Favourite favItem = new Favourite(title, address, id);
                favouriteItems.add(favItem);
            }
        } finally {
            if(cursor != null && cursor.isClosed()){
                cursor.close();
            }
            db.close();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_favourites);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

        adapter = new FavouritesAdapter(mainActivity,favouriteItems);
        recyclerView.setAdapter(adapter);
    }
}