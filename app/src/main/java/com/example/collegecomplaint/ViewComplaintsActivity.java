package com.example.collegecomplaint;

import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;

public class ViewComplaintsActivity extends AppCompatActivity {

    private RecyclerView complaintsRecyclerView;
    private ComplaintAdapter complaintAdapter;
    private List<Complaint> complaintList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaints);

        // Initialize RecyclerView and complaint list
        complaintsRecyclerView = findViewById(R.id.complaintsRecyclerView);
        complaintList = new ArrayList<>();

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        complaintAdapter = new ComplaintAdapter(complaintList);
        complaintsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        complaintsRecyclerView.setAdapter(complaintAdapter);

        // Check if user is logged in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in to view complaints", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch complaints from Firestore
        fetchComplaintsFromFirestore();
    }

    private void fetchComplaintsFromFirestore() {
        // Fetch complaints from Firestore
        db.collection("Complaints") // Ensure the collection name matches 'Complaints'
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            Log.d("Firestore", "Complaints fetched successfully: " + querySnapshot.size());
                            complaintList.clear();
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Log.d("Firestore", "Complaint ID: " + document.getId());
                                String id = document.getId();
                                String description = document.getString("description");
                                String status = document.getString("status");
                                String category = document.getString("category");
                                String priority = document.getString("priority");
                                String userId = document.getString("userId");

                                // Add complaint to list
                                Complaint complaint = new Complaint(id, description, status, category, priority, userId);
                                complaintList.add(complaint);
                            }
                            complaintAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("Firestore", "Error fetching complaints: " + task.getException().getMessage());
                        Toast.makeText(ViewComplaintsActivity.this, "Error fetching complaints", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
