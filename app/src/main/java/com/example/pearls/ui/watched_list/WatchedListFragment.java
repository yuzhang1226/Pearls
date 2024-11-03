package com.example.pearls.ui.watched_list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pearls.R;
import com.example.pearls.databinding.FragmentWatchedListBinding;

import java.util.ArrayList;
import java.util.List;

public class WatchedListFragment extends Fragment {

    private FragmentWatchedListBinding binding; // Declare the binding variable
    private WatchedListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout using View Binding
        binding = FragmentWatchedListBinding.inflate(inflater, container, false);

        // Set up RecyclerView
        binding.watchedListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample data
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Inception", R.drawable.inception, 5, "Amazing movie with a great plot twist!"));
        movies.add(new Movie("The Matrix", R.drawable.matrix, 4.5f, "A revolutionary film for its time."));
        movies.add(new Movie("Interstellar", R.drawable.interstellar, 4.8f, "A visually stunning space epic."));
        movies.add(new Movie("The Godfather", R.drawable.godfather, 5, "A timeless classic that redefined cinema."));

        // Set adapter
        adapter = new WatchedListAdapter(movies, getContext());
        binding.watchedListRecyclerView.setAdapter(adapter);

        return binding.getRoot(); // Return the root of the binding
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clear binding reference to avoid memory leaks
    }
}