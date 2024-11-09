package com.example.collegecomplaint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ComplaintSubmissionActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private EditText titleField, descriptionField, categoryField, priorityField;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_submission);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        titleField = findViewById(R.id.titleField);
        descriptionField = findViewById(R.id.descriptionField);
        categoryField = findViewById(R.id.categoryField);
        priorityField = findViewById(R.id.priorityField);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> submitComplaint());
    }

    private void submitComplaint() {
        String title = titleField.getText().toString().trim();
        String description = descriptionField.getText().toString().trim();
        String category = categoryField.getText().toString().trim();
        String priority = priorityField.getText().toString().trim();
        String userId = mAuth.getCurrentUser().getUid();

        Map<String, Object> complaint = new HashMap<>();
        complaint.put("title", title);
        complaint.put("description", description);
        complaint.put("category", category);
        complaint.put("priority", priority);
        complaint.put("status", "Pending");
        complaint.put("userId", userId);

        db.collection("Complaints").add(complaint).addOnSuccessListener(documentReference ->
                Toast.makeText(ComplaintSubmissionActivity.this, "Complaint submitted!", Toast.LENGTH_SHORT).show()
        ).addOnFailureListener(e ->
                Toast.makeText(ComplaintSubmissionActivity.this, "Submission failed.", Toast.LENGTH_SHORT).show()
        );
    }
}
