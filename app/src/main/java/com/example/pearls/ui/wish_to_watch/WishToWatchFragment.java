package com.example.pearls.ui.wish_to_watch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pearls.R;
import com.example.pearls.databinding.FragmentWishToWatchBinding;

import com.example.pearls.ui.watched_list.Movie;

import java.util.ArrayList;
import java.util.List;

public class WishToWatchFragment extends Fragment {

    private FragmentWishToWatchBinding binding;
    private WishToWatchAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout using View Binding
        binding = FragmentWishToWatchBinding.inflate(inflater, container, false);

        // Set up RecyclerView
        binding.wishToWatchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample data
        List<MovieWished> movies = new ArrayList<>();
        movies.add(new MovieWished("Inception", R.drawable.inception, "Amazing movie with a great plot twist!"));
        movies.add(new MovieWished("The Matrix", R.drawable.matrix,  "A revolutionary film for its time."));
        movies.add(new MovieWished("Interstellar", R.drawable.interstellar,  "A visually stunning space epic."));
        movies.add(new MovieWished("The Godfather", R.drawable.godfather, "A timeless classic that redefined cinema."));

        // Set adapter
        adapter = new WishToWatchAdapter(movies, getContext());
        binding.wishToWatchRecyclerView.setAdapter(adapter);

        return binding.getRoot(); // Return the root of the binding
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clear binding reference to avoid memory leaks
    }
}