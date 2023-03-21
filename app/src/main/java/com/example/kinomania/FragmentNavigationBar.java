package com.example.kinomania;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kinomania.databinding.FragmentCinemasBinding;
import com.example.kinomania.databinding.FragmentNavigationBarBinding;


public class FragmentNavigationBar extends Fragment {

    FragmentNavigationBarBinding binding;
    public FragmentNavigationBar() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavigationBarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}