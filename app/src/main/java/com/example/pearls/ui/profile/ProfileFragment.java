package com.example.pearls.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pearls.R;
import com.example.pearls.databinding.FragmentProfileBinding;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Check if the result is OK
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Retrieve the intent data safely
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri imageUri = data.getData();
                        // Now use imageUri to load the image
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                            // Set the bitmap to the ImageView
                            binding.profilePicture.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error loading image", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle the case where data is null or no image was selected
                        Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle cases where the result is not OK
                    Toast.makeText(getActivity(), "Image selection failed", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        // Initialize the ActivityResultLauncher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();
                            try {
                                // Set the selected image to the ImageView
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                                binding.profilePicture.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        binding.uploadPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open gallery to pick an image
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagePickerLauncher.launch(intent);  // Use the launcher to start the activity
            }
        });
        // Set up the "Watched List" click listener
        binding.watchedListTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(binding.getRoot()); // Use the root view of the binding
                navController.navigate(R.id.action_profile_to_watched_list);
            }
        });

        //listener for wish to watch title
        binding.wishToWatchTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.navigate(R.id.action_profile_to_wish_to_watch);
            }
        });
        // Set up the edit button functionality
        binding.editPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the EditText and hide the TextView
                if (binding.introductionEditText.getVisibility() == View.GONE) {
                    // Copy text to EditText
                    binding.introductionEditText.setText(binding.introductionText.getText());
                    binding.introductionEditText.setVisibility(View.VISIBLE);
                    binding.introductionText.setVisibility(View.GONE);
                    binding.saveButton.setVisibility(View.VISIBLE); // Show save button
                } else {
                    // Hide the EditText and show the TextView
                    binding.introductionText.setText(binding.introductionEditText.getText()); // Update TextView with EditText text
                    binding.introductionEditText.setVisibility(View.GONE);
                    binding.introductionText.setVisibility(View.VISIBLE);
                    binding.saveButton.setVisibility(View.GONE); // Hide save button
                }
            }
        });
        // Set up the save button functionality
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the EditText and show the TextView
                binding.introductionText.setText(binding.introductionEditText.getText()); // Update TextView with EditText text
                binding.introductionEditText.setVisibility(View.GONE);
                binding.introductionText.setVisibility(View.VISIBLE);
                binding.saveButton.setVisibility(View.GONE); // Hide save button
            }

        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}