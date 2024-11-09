package com.example.collegecomplaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminComplaintAdapter extends RecyclerView.Adapter<AdminComplaintAdapter.ComplaintViewHolder> {

    private List<Complaint> complaintList;
    private OnComplaintClickListener listener;

    public AdminComplaintAdapter(List<Complaint> complaintList, OnComplaintClickListener listener) {
        this.complaintList = complaintList;
        this.listener = listener;
    }

    @Override
    public ComplaintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_item, parent, false);
        return new ComplaintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ComplaintViewHolder holder, int position) {
        Complaint complaint = complaintList.get(position);
        holder.bind(complaint, listener);
    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    public static class ComplaintViewHolder extends RecyclerView.ViewHolder {
        private TextView description, status, category, priority;

        public ComplaintViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.descriptionTextView);
            status = itemView.findViewById(R.id.statusTextView);
            category = itemView.findViewById(R.id.categoryTextView);
            priority = itemView.findViewById(R.id.priorityTextView);
        }

        public void bind(Complaint complaint, OnComplaintClickListener listener) {
            description.setText(complaint.getDescription());
            status.setText(complaint.getStatus());
            category.setText(complaint.getCategory());
            priority.setText(complaint.getPriority());

            itemView.setOnClickListener(v -> listener.onComplaintClick(complaint));
        }
    }

    public interface OnComplaintClickListener {
        void onComplaintClick(Complaint complaint);
    }
}
