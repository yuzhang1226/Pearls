package com.example.pearls.ui.movies_and_books;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pearls.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MovieOrBookDetailsFragment extends Fragment {

    private FirebaseFirestore firestore;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private ImageView imageViewMovie;
    private RatingBar ratingBar;
    private EditText editTextReview;
    private Button buttonAddToWishList;
    private Button buttonSaveReview;
    private Button buttonBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_details, container, false);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Find views
        imageViewMovie = view.findViewById(R.id.imageViewMovie);
        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewDescription = view.findViewById(R.id.textViewDescription);
        ratingBar = view.findViewById(R.id.movie_rating);
        editTextReview = view.findViewById(R.id.movie_review);
        buttonAddToWishList = view.findViewById(R.id.buttonAddToWishList);
        buttonSaveReview = view.findViewById(R.id.buttonSaveReview); // Newly added Save button
        buttonBack = view.findViewById(R.id.buttonBack);

        // Load movie details
        String movieId = getArguments() != null ? getArguments().getString("movieId") : null;
        if (movieId != null) {
            loadMovieDetails(movieId);
        }

        // Set up Add to Wish List button click listener
        buttonAddToWishList.setOnClickListener(v -> addToWishList(movieId));

        // Set up Save Review button click listener
        buttonSaveReview.setOnClickListener(v -> saveReview(movieId));

        // Set up Back button to return to the previous page
        buttonBack.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }

    private void loadMovieDetails(String movieId) {
        firestore.collection("movies").document(movieId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String title = documentSnapshot.getString("title");
                        String description = documentSnapshot.getString("description");
                        String imageUrl = documentSnapshot.getString("imageUrl");

                        textViewTitle.setText(title);
                        textViewDescription.setText(description);

                        // Load image using Glide (assuming imageUrl is a valid URL)
                        if (imageUrl != null) {
                            Glide.with(requireContext())
                                    .load(imageUrl)
                                    .into(imageViewMovie);
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Failed to load movie details.", Toast.LENGTH_SHORT).show()
                );
    }

    private void addToWishList(String movieId) {
        // Logic to add the movie or book to the wishlist
        Toast.makeText(requireContext(), "Added to Wish List", Toast.LENGTH_SHORT).show();
    }

    private void saveReview(String movieId) {
        float rating = ratingBar.getRating();
        String reviewText = editTextReview.getText().toString().trim();

        if (movieId == null || reviewText.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a review and rating.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique ID for the review
        String reviewId = UUID.randomUUID().toString();

        // Create a review data object
        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("movieId", movieId);
        reviewData.put("review", reviewText);
        reviewData.put("stars", String.valueOf((int) rating)); // Convert rating to integer
        reviewData.put("userId", "user-id"); // Replace with actual user ID if available
        reviewData.put("timestamp", System.currentTimeMillis());
        reviewData.put("type", Collections.singletonList("movie")); // Use a List instead of an array

        // Save review and rating to Firestore in the "injection" collection
        firestore.collection("injection").document(reviewId).set(reviewData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Thanks for your rating!", Toast.LENGTH_SHORT).show();
                    editTextReview.setText(""); // Clear the review input
                    ratingBar.setRating(0);     // Reset the rating
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Failed to save review. Please try again.", Toast.LENGTH_SHORT).show()
                );
    }
}
