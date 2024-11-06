package com.example.pearls.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.pearls.R;
import com.example.pearls.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout using ViewBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Search button listener
        binding.btnSubmit.setOnClickListener(v -> {
            String queryText = binding.etInput.getText().toString().trim();
            if (!queryText.isEmpty()) {
                searchContent(queryText, v);
            } else {
                Toast.makeText(getContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });

        // Load latest group activities
        loadLatestGroupActivities();

        // Load latest movie recommendations
        loadLatestMovies();

        // Load latest book recommendations
        loadLatestBooks();

        // Navigation when clicking on sections
        setupNavigation();

        return root;
    }

    private void setupNavigation() {
        // Click listener for Group Activities section
        binding.groupActivitiesSection.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_groups);
        });

        // Click listener for Group Name
        binding.groupName.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_groups);
        });

        // Click listener for Post Name
        binding.postName.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_groups);
        });

        // Click listener for movie recommendations section
        binding.movieRecommendationsSection.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_movies_and_books);
        });

        // Click listener for movie images
        binding.movieImage1.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_movies_and_books);
        });
        binding.movieImage2.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_movies_and_books);
        });
        binding.movieImage3.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_movies_and_books);
        });

        binding.bookImage1.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_movies_and_books);
        });
        binding.bookImage2.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_movies_and_books);
        });
        binding.bookImage3.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_movies_and_books);
        });

        binding.bookRecommendationsSection.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_movies_and_books);
        });
    }

    // Search Firestore for content in books, groups, movies, collections
    private void searchContent(String queryText, View view) {
        db.collection("collections")
                .whereEqualTo("itemName", queryText)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // If content exists, navigate to item detail page
                        for (DocumentSnapshot document : task.getResult()) {
                            String itemId = document.getId(); // Assuming ID is needed to load item details
                            String itemName = document.getString("itemName");

                            // Create a bundle to pass item ID to the detail page
                            Bundle bundle = new Bundle();
                            bundle.putString("itemId", itemId);
                            bundle.putString("itemName", itemName);

                            // Navigate to the detail page with the bundle
                            Navigation.findNavController(view).navigate(R.id.action_home_to_movie_detail_from_search, bundle);
                        }
                    } else {
                        // Content doesn't exist, ask to add
                        showAddContentDialog(queryText, view);
                    }
                });
    }

    // Show dialog asking user if they want to add the content
    private void showAddContentDialog(String queryText, View view) {
        new AlertDialog.Builder(getContext())
                .setMessage("We don't have it right now, do you want to add it?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Navigate to Create Movie/Book page
                    Navigation.findNavController(view).navigate(R.id.navigation_create_movie_book);
                })
                .setNegativeButton("No", null)
                .show();
    }

    // Add content to Firestore
    private void addContent(String queryText) {
        db.collection("movies")
                .add(new Content(queryText))
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Added: " + queryText, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error adding content", Toast.LENGTH_SHORT).show();
                });
    }

    // Load latest updates from groups in Firestore
    private void loadLatestGroupActivities() {
        db.collection("groups")
                .orderBy("updatedAt", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            binding.groupName.setText(document.getString("groupName"));
                            binding.postName.setText(document.getString("postTitle"));
                        }
                    }
                });
    }

    // Load latest movie updates from Firestore
    private void loadLatestMovies() {
        db.collection("movies")
                .orderBy("updatedAt", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        int i = 1;
                        for (DocumentSnapshot document : task.getResult()) {
                            if (i == 1) {
                                binding.movieImage1.setImageResource(R.drawable.movie); // Assuming you have movie image URL
                            } else if (i == 2) {
                                binding.movieImage2.setImageResource(R.drawable.movie);
                            } else if (i == 3) {
                                binding.movieImage3.setImageResource(R.drawable.movie);
                            }
                            i++;
                        }
                    }
                });
    }

    private void loadLatestBooks() {
        db.collection("books")
                .orderBy("updatedAt", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        int i = 1;
                        for (DocumentSnapshot document : task.getResult()) {
                            if (i == 1) {
                                binding.bookImage1.setImageResource(R.drawable.book); // Assuming you have movie image URL
                            } else if (i == 2) {
                                binding.bookImage2.setImageResource(R.drawable.book);
                            } else if (i == 3) {
                                binding.bookImage3.setImageResource(R.drawable.book);
                            }
                            i++;
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Content model for adding to Firestore
    public static class Content {
        private String name;

        public Content() {}

        public Content(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
