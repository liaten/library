package com.example.library.entity;

import java.util.Date;

public class Book {

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int ID, cover;
    private String title, author, theme, description;

    public Book(int ID, String title, String author, int cover, String theme, String description){

        setID(ID);
        setTitle(title);
        setCover(cover);
        setAuthor(author);
        setTheme(theme);
        setDescription(description);
    }
}
