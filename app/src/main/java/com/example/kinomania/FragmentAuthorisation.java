package com.example.kinomania;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class FragmentAuthorisation extends Fragment {

    MainActivity mainActivity;

    public FragmentAuthorisation() {
       super(R.layout.fragment_authorisation);
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
        return inflater.inflate(R.layout.fragment_authorisation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button_entry = view.findViewById(R.id.GoToAuth);
        ImageButton button_entry_right = view.findViewById(R.id.right_pointer);

        button_entry_right.setOnClickListener(v-> {
            View mainContainer = mainActivity.findViewById(R.id.fragment_container_view);
            NavController navController = Navigation.findNavController(mainContainer);
            int currentDestination = navController.getCurrentDestination().getId();
            int destination = R.id.fragmentProfile;
            if(currentDestination != destination){
                navController.navigate(R.id.action_fragmentAuthorisation_to_fragmentProfile);
            }
        });

        button_entry.setOnClickListener(v-> { // первая и вторая кнопка делают одно и то же действие
            View mainContainer = mainActivity.findViewById(R.id.fragment_container_view);
            NavController navController = Navigation.findNavController(mainContainer);
            int currentDestination = navController.getCurrentDestination().getId();
            int destination = R.id.fragmentProfile;
            if(currentDestination != destination){
                navController.navigate(R.id.action_fragmentAuthorisation_to_fragmentProfile);
            }
        });
    }
}