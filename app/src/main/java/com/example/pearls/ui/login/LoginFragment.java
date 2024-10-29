package com.example.pearls.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // Import Button class
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pearls.MainActivity;
import com.example.pearls.databinding.FragmentLoginBinding;
import com.example.pearls.ui.signup.SignupFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends AppCompatActivity {

    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // Declare Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Initialize Firestore
//
//        // Check if a user is already logged in
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            // User is signed in, navigate to MainActivity
//            Intent intent = new Intent(LoginFragment.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        // Login button click listener
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        // Sign-up text click listener
        binding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to SignupActivity
                Intent intent = new Intent(LoginFragment.this, SignupFragment.class);
                startActivity(intent);
            }
        });

        // Guest button click listener
        Button btnGuest = binding.btnGuest; // Assuming you added btnGuest in XML
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Allow access without logging in
                createAnonymousUser(); // Call method to create an anonymous user
            }
        });
    }

    private void createAnonymousUser() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginFragment.this, "Logged in as Guest!", Toast.LENGTH_SHORT).show();

                        // Save guest user info in Firestore
                        saveGuestData(user.getUid()); // Save data in Firestore

                        // Navigate to MainActivity
                        Intent intent = new Intent(LoginFragment.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginFragment.this, "Guest login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveGuestData(String userId) {
        // Create a map for guest data
        Map<String, Object> guestData = new HashMap<>();
        guestData.put("userId", userId);
        guestData.put("isGuest", true);
        guestData.put("createdAt", System.currentTimeMillis());

        // Save the guest data to Firestore under a "guests" collection
        db.collection("guests").document(userId) // Use userId as document ID
                .set(guestData)
                .addOnSuccessListener(aVoid -> {
                    // Successfully added guest data
                    Toast.makeText(LoginFragment.this, "Guest data saved!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LoginFragment.this, "Failed to save guest data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loginUser() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your email and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Authenticate the user with Firebase Auth
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginFragment.this, "Login successful!", Toast.LENGTH_SHORT).show();

                        // Navigate to MainActivity after successful login
                        Intent intent = new Intent(LoginFragment.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginFragment.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
