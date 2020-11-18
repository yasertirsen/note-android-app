package com.example.notes.model;

import java.util.List;

public class User {
    private String email;
    private String password;
    private String username;
    private String phone;
    private List<Note> notes;

    public User(String email, String password, String username, String phone, List<Note> notes) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phone = phone;
        this.notes = notes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
