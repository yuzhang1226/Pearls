package com.example.pearls.ui.wish_to_watch;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pearls.databinding.FragmentWishToWatchItemBinding;
import java.util.List;

public class WishToWatchAdapter extends RecyclerView.Adapter<WishToWatchAdapter.ViewHolder> {

    private List<MovieWished> movies;
    private Context context;

    public WishToWatchAdapter(List<MovieWished> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentWishToWatchItemBinding binding = FragmentWishToWatchItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieWished movie = movies.get(position);
        holder.binding.movieName.setText(movie.getTitle());
        holder.binding.movieImage.setImageResource(movie.getImageResourceId());

        holder.binding.movieIntro.setText(movie.getIntro() != null ? movie.getIntro() : "");
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        FragmentWishToWatchItemBinding binding;

        public ViewHolder(@NonNull FragmentWishToWatchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
