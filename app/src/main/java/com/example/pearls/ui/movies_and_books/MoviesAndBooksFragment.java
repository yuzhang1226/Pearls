package com.example.pearls.ui.movies_and_books;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.pearls.R;
import com.example.pearls.databinding.FragmentMoviesAndBooksBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MoviesAndBooksFragment extends Fragment {

    private FragmentMoviesAndBooksBinding binding;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMoviesAndBooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Click listeners for the movie images
        binding.movieImage1.setOnClickListener(v -> navigateToMovieDetail("movieId1"));
        binding.movieImage2.setOnClickListener(v -> navigateToMovieDetail("movieId2"));
        binding.movieImage3.setOnClickListener(v -> navigateToMovieDetail("movieId3"));
        binding.movieImage4.setOnClickListener(v -> navigateToMovieDetail("movieId4"));
        binding.movieImage5.setOnClickListener(v -> navigateToMovieDetail("movieId5"));
        binding.movieImage6.setOnClickListener(v -> navigateToMovieDetail("movieId6"));

        // Click listeners for "Wish List" buttons
        binding.btnMovie1.setOnClickListener(v -> addToWishList(R.id.movieImage1));
        binding.btnMovie2.setOnClickListener(v -> addToWishList(R.id.movieImage2));
        binding.btnMovie3.setOnClickListener(v -> addToWishList(R.id.movieImage3));
        binding.btnMovie4.setOnClickListener(v -> addToWishList(R.id.movieImage4));
        binding.btnMovie5.setOnClickListener(v -> addToWishList(R.id.movieImage5));
        binding.btnMovie6.setOnClickListener(v -> addToWishList(R.id.movieImage6));

        // Handle back navigation
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(root).navigate(R.id.navigation_profile, null);
            }
        });

        return root;
    }

    private void navigateToMovieDetail(String movieId) {
        Bundle bundle = new Bundle();
        bundle.putString("movieId", movieId);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_movies_and_books_to_movie_detail, bundle);
    }

    private void addToWishList(int buttonId) {
        String itemName = getItemNameById(buttonId);

        // Display a message to the user
        Toast.makeText(requireContext(), "Add to your wish list: " + itemName, Toast.LENGTH_SHORT).show();

        // Data to add to the Firebase collection "collections"
        Map<String, Object> wishItem = new HashMap<>();
        wishItem.put("itemName", itemName);
        wishItem.put("addedAt", System.currentTimeMillis());

        // Add the item to the Firestore collection "collections"
        firestore.collection("collections")
                .add(wishItem)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(requireContext(), "Item added to wish list successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Failed to add item.", Toast.LENGTH_SHORT).show());
    }

    private String getItemNameById(int buttonId) {
        // Directly return the item name based on the button ID
        if (buttonId == R.id.movieImage1) {
            return "Movie Title 1";
        } else if (buttonId == R.id.movieImage2) {
            return "Movie Title 2";
        } else if (buttonId == R.id.movieImage3) {
            return "Movie Title 3";
        } else if (buttonId == R.id.movieImage4) {
            return "Movie Title 4";
        } else if (buttonId == R.id.movieImage5) {
            return "Movie Title 5";
        } else if (buttonId == R.id.movieImage6) {
            return "Movie Title 6";
        }
        return "Unknown Movie";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
