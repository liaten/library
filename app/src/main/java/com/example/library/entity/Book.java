package com.example.library.entity;

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

    public int getCoverID() {
        return coverID;
    }

    public void setCoverID(int coverID) {
        this.coverID = coverID;
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

    private int ID, coverID;
    private String title, author, theme, description;

    public Book(int ID, String title, String author, int coverID, String theme, String description){
        setID(ID);
        setTitle(title);
        setCoverID(coverID);
        setAuthor(author);
        setTheme(theme);
        setDescription(description);
    }
}
