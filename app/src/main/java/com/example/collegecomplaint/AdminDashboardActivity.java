package com.example.collegecomplaint;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView complaintsRecyclerView;
    private AdminComplaintAdapter complaintAdapter;
    private List<Complaint> complaintList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = FirebaseFirestore.getInstance();

        complaintsRecyclerView = findViewById(R.id.recyclerViewComplaints);
        progressBar = findViewById(R.id.progressBar);
        complaintList = new ArrayList<>();

        complaintAdapter = new AdminComplaintAdapter(complaintList, complaint -> {
            // Handle the status update for each individual complaint
            String[] statusOptions = {"Pending", "In Progress", "Resolved"};
            new AlertDialog.Builder(AdminDashboardActivity.this)
                    .setTitle("Update Status")
                    .setItems(statusOptions, (dialog, which) -> {
                        String selectedStatus = statusOptions[which];
                        updateComplaintStatusInFirestore(complaint, selectedStatus); // Call the method to update the status
                    })
                    .show();
        });

        complaintsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        complaintsRecyclerView.setAdapter(complaintAdapter);

        fetchComplaintsFromFirestore();
    }

    private void fetchComplaintsFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in to view complaints", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        db.collection("Complaints")
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            complaintList.clear();
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String id = document.getId();
                                String description = document.getString("description");
                                String status = document.getString("status");
                                String category = document.getString("category");
                                String priority = document.getString("priority");
                                String userId = document.getString("userId");

                                Complaint complaint = new Complaint(id, description, status, category, priority, userId);
                                complaintList.add(complaint);
                            }
                            complaintAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(AdminDashboardActivity.this, "Error fetching complaints", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to update complaint status
    private void updateComplaintStatusInFirestore(Complaint complaint, String newStatus) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in to update the status", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the custom claims of the current user to verify if they are an admin
        currentUser.getIdToken(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult().getToken();
                if (token != null && token.contains("\"admin\":true")) {
                    // The user is an admin
                    db.collection("Complaints").document(complaint.getId())
                            .update("status", newStatus)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Status updated successfully", Toast.LENGTH_SHORT).show();
                                complaint.setStatus(newStatus);
                                complaintAdapter.notifyDataSetChanged();  // Notify adapter to update the UI
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to update status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(this, "You do not have permission to update the status"+token, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error verifying admin status", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
