package com.example.collegecomplaint;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    private Button registerButton;
    private RadioGroup roleGroup;  // RadioGroup for selecting role
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        registerButton = findViewById(R.id.registerButton);
        roleGroup = findViewById(R.id.roleGroup);  // Get the RadioGroup for selecting role

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Get selected role from RadioGroup
        int selectedRoleId = roleGroup.getCheckedRadioButtonId();
        String role;

        if (selectedRoleId == R.id.radioStudent) {
            role = "Student";
        } else if (selectedRoleId == R.id.radioAdmin) {
            role = "Admin";
        } else {
            Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
            return; // Exit if no role is selected
        }

        // Register the user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // After successful registration, store the user role in Firestore
                        String userId = mAuth.getCurrentUser().getUid();
                        Map<String, Object> user = new HashMap<>();
                        user.put("email", email);
                        user.put("role", role);

                        db.collection("Users").document(userId)
                                .set(user) // Save user data with role
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    // Redirect to LoginActivity or MainActivity
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();  // Close RegisterActivity so the user can't go back
                                })
                                .addOnFailureListener(e -> {
                                    // Handle error in saving user role
                                    Toast.makeText(RegisterActivity.this, "Error saving role: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // If registration fails, show a message
                        Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
