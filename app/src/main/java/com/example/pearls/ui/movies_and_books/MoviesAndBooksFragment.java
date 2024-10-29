package com.example.pearls.ui.movies_and_books;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pearls.databinding.FragmentMoviesAndBooksBinding;

public class MoviesAndBooksFragment extends Fragment {

    private FragmentMoviesAndBooksBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMoviesAndBooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnSearch.setOnClickListener( v-> {
            Toast.makeText(requireView().getContext(), "Clicked submit button", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}