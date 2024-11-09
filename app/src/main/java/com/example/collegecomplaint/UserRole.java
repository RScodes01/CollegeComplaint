package com.example.collegecomplaint;

public class UserRole {
    private String role;

    // Default constructor required for Firebase Firestore
    public UserRole() {
    }

    // Constructor with role
    public UserRole(String role) {
        this.role = role;
    }

    // Getter for role
    public String getRole() {
        return role;
    }

    // Setter for role
    public void setRole(String role) {
        this.role = role;
    }
}
