package com.example.collegecomplaint;

public class Complaint {

    private String id;
    private String description;
    private String status;
    private String category;
    private String priority;
    private String userId;

    public Complaint(String id, String description, String status, String category, String priority, String userId) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.category = category;
        this.priority = priority;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
