package com.example.notes.model;

public class Note {

    private String uid;
    private String title;
    private String tag;
    private String text;

    public Note(String uid, String title, String tag, String text) {
        this.uid = uid;
        this.title = title;
        this.tag = tag;
        this.text = text;
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

}
