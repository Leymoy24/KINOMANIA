package com.example.kinomania;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kinomania.databinding.FragmentCinemasBinding;
import com.example.kinomania.databinding.FragmentFilmsBinding;

public class FragmentFilms extends Fragment {

    FragmentFilmsBinding binding;
    public FragmentFilms() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilmsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}