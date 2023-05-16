package com.example.kinomania.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.kinomania.ui.activities.MainActivity;
import com.example.kinomania.R;


public class FragmentNavigationBar extends Fragment {

    MainActivity mainActivity;

    public FragmentNavigationBar() {
        super(R.layout.fragment_navigation_bar);
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
        return inflater.inflate(R.layout.fragment_navigation_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView button_cinemas = view.findViewById(R.id.cinema_icon);
        ImageView button_films = view.findViewById(R.id.film_icon);
        ImageView button_favourites = view.findViewById(R.id.lovely_cinemas_icon);
        ImageView button_profile = view.findViewById(R.id.profile_icon);

        View mainContainer = mainActivity.findViewById(R.id.fragment_container_view);

        button_cinemas.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(mainContainer);
            int currentDestination = navController.getCurrentDestination().getId();
            int destination = R.id.fragmentCinemas;
            if(currentDestination != destination){
                navController.navigate(destination);
            }
        });

        button_films.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(mainContainer);
            int currentDestination = navController.getCurrentDestination().getId();
            int destination = R.id.fragmentFilms;
            if(currentDestination != destination){
                navController.navigate(destination);
            }
        });

        button_favourites.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(mainContainer);
            int currentDestination = navController.getCurrentDestination().getId();
            int destination = R.id.fragmentFavourites;
            if(currentDestination != destination) {
                navController.navigate(destination);
            }
        });

        button_profile.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(mainContainer);
            int currentDestination = navController.getCurrentDestination().getId();
            int destination = R.id.fragmentProfile;
            if(currentDestination != destination) {
                navController.navigate(destination);
            }
        });
    }
}