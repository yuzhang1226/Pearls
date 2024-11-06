package com.example.pearls.ui.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.pearls.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreateMovieBookFragment extends Fragment {

    private EditText itemNameEditText, authorEditText, introductionEditText, yearEditText;
    private Spinner typeSpinner;
    private Button saveButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public CreateMovieBookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_movie, container, false);

        // Initialize views
        itemNameEditText = rootView.findViewById(R.id.itemName);
        authorEditText = rootView.findViewById(R.id.author);
        introductionEditText = rootView.findViewById(R.id.introduction);
        yearEditText = rootView.findViewById(R.id.year);
        typeSpinner = rootView.findViewById(R.id.typeSpinner);
        saveButton = rootView.findViewById(R.id.saveButton);

        // Initialize FirebaseAuth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Set up the Spinner for selecting movie or book using a List
        List<String> typeList = new ArrayList<>();
        typeList.add("Movie");
        typeList.add("Book");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, typeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Handle save button click
        saveButton.setOnClickListener(v -> saveItem());

        return rootView;
    }

    private void saveItem() {
        // Get the input values
        String itemName = itemNameEditText.getText().toString().trim();
        String author = authorEditText.getText().toString().trim();
        String introduction = introductionEditText.getText().toString().trim();
        String year = yearEditText.getText().toString().trim();
        String type = typeSpinner.getSelectedItem().toString().toLowerCase();
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "unknown";

        // Validate the input fields
        if (itemName.isEmpty() || author.isEmpty() || introduction.isEmpty() || year.isEmpty()) {
            Toast.makeText(getContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a random ID for the item
        String itemId = UUID.randomUUID().toString();

        // Create a map to store the data
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("id", itemId);
        itemData.put("itemName", itemName);
        itemData.put("author", author);
        itemData.put("introduction", introduction);
        itemData.put("year", year);
        itemData.put("type", type);
        itemData.put("contribution", userId);
        itemData.put("createdAt", System.currentTimeMillis());

        // Save the item to Firestore
        db.collection("collections")
                .document(itemId)
                .set(itemData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Item saved successfully.", Toast.LENGTH_SHORT).show();
                    // Optionally navigate back or clear the form
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error saving item.", Toast.LENGTH_SHORT).show());
    }
}
