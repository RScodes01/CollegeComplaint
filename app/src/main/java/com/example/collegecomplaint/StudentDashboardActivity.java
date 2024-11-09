package com.example.collegecomplaint;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class StudentDashboardActivity extends AppCompatActivity {

    private Button submitComplaintButton;
    private Button viewComplaintsButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        mAuth = FirebaseAuth.getInstance();

        submitComplaintButton = findViewById(R.id.submitComplaintButton);
        viewComplaintsButton = findViewById(R.id.viewComplaintsButton);

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "You need to log in first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        submitComplaintButton.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, SubmitComplaintActivity.class);
            startActivity(intent);
        });

        viewComplaintsButton.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, ViewComplaintsActivity.class);
            startActivity(intent);
        });
    }
}
