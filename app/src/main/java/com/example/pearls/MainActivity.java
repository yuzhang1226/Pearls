package com.example.pearls;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.pearls.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Setting up top-level destinations in the bottom navigation
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_movies_and_books, R.id.navigation_groups, R.id.navigation_profile)
                .build();

        // Find the NavController for navigation actions
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Set up the ActionBar to work with the NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Set up the bottom navigation with the NavController
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    // Override to handle "up" navigation with the NavController
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
