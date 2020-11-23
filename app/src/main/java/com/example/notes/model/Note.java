package com.example.notes.model;

import java.util.Date;

public class Note {

    private String uid;
    private String title;
    private String tag;
    private String text;
    private String date;

    public Note(String uid, String title, String tag, String text, String date) {
        this.uid = uid;
        this.title = title;
        this.tag = tag;
        this.text = text;
        this.date = date;
    }

    public Note() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
