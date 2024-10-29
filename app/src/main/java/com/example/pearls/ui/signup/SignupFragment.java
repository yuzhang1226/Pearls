package com.example.pearls.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pearls.databinding.FragmentSignupBinding;
import com.example.pearls.ui.login.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupFragment extends AppCompatActivity {

    private FragmentSignupBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Sign-up button click listener
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupUser();
            }
        });

        // Login text click listener
        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to LoginActivity
                Intent intent = new Intent(SignupFragment.this, LoginFragment.class);
                startActivity(intent);
                finish(); // Close Signup activity
            }
        });
    }

    private void signupUser() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user with Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Save user info to Firestore
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("name", name);
                        userInfo.put("email", email);

                        db.collection("users")
                                .document(user.getUid())
                                .set(userInfo)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(SignupFragment.this, "Signup successful! Welcome, " + name, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupFragment.this, LoginFragment.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("SignupError", "Firestore error: ", e);
                                    Toast.makeText(SignupFragment.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Log.e("SignupError", "FirebaseAuth error: ", task.getException());
                        Toast.makeText(SignupFragment.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
