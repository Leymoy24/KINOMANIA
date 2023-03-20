package com.example.kinomania;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kinomania.databinding.FragmentCinemasBinding;


public class FragmentCinemas extends Fragment {
    FragmentCinemasBinding binding;
    public FragmentCinemas() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =FragmentCinemasBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}