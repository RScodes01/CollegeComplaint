package com.example.collegecomplaint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SubmitComplaintActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private EditText descriptionField;
    private RadioGroup priorityGroup;
    private Button submitComplaintButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_complaint);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        categorySpinner = findViewById(R.id.categorySpinner);
        descriptionField = findViewById(R.id.descriptionField);
        priorityGroup = findViewById(R.id.priorityGroup);
        submitComplaintButton = findViewById(R.id.submitComplaintButton);

        // Populate Spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.complaint_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Handle Submit button click
        submitComplaintButton.setOnClickListener(v -> submitComplaint());
    }

    private void submitComplaint() {
        String category = categorySpinner.getSelectedItem().toString();
        String description = descriptionField.getText().toString().trim();

        // Get selected priority
        int selectedPriorityId = priorityGroup.getCheckedRadioButtonId();
        RadioButton selectedPriorityButton = findViewById(selectedPriorityId);
        String priority = selectedPriorityButton != null ? selectedPriorityButton.getText().toString() : "Low";

        // Validate fields
        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a complaint description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (category.equals("Select Category")) {
            Toast.makeText(this, "Please select a complaint category", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current user ID
        String userId = mAuth.getCurrentUser().getUid();

        // Create a new Complaint object
        Complaint complaint = new Complaint(
                null,  // Auto-generated ID will be set by Firestore
                description,
                "Pending",  // Initial status
                category,
                priority,
                userId
        );

        // Add complaint to Firestore
        db.collection("Complaints")
                .add(complaint)
                .addOnSuccessListener(documentReference -> {
                    // Complaint successfully added
                    Toast.makeText(SubmitComplaintActivity.this, "Complaint submitted successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to the dashboard
                })
                .addOnFailureListener(e -> {
                    // Handle error
                    Toast.makeText(SubmitComplaintActivity.this, "Failed to submit complaint: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}