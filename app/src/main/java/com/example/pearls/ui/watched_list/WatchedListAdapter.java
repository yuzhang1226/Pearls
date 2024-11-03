package com.example.pearls.ui.watched_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pearls.databinding.FragmentWatchedListItemBinding;

import java.util.List;

public class WatchedListAdapter extends RecyclerView.Adapter<WatchedListAdapter.ViewHolder> {

    private List<Movie> movies;
    private Context context;

    public WatchedListAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using View Binding
        FragmentWatchedListItemBinding binding = FragmentWatchedListItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.binding.movieName.setText(movie.getTitle());
        holder.binding.movieImage.setImageResource(movie.getImageResourceId());
        holder.binding.movieRating.setRating(movie.getRating());
        holder.binding.movieReview.setText(movie.getReview());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        FragmentWatchedListItemBinding binding;

        public ViewHolder(@NonNull FragmentWatchedListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}