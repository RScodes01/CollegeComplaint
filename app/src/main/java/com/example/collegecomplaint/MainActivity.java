package com.example.collegecomplaint;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowInsetsController;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.database.FirebaseDatabase; // Import Firebase classes

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // You can use database instance to reference your Firebase database

        // Enable Edge-to-Edge layout for Android 13 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                // Hide system bars for immersive experience
                insetsController.hide(WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE);
            }
        }

        // Initialize buttons
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            // Go to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Register button click listener
        registerButton.setOnClickListener(v -> {
            // Go to RegisterActivity
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
