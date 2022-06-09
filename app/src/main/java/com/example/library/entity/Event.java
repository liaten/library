package com.example.library.entity;

public class Event {
    private int id, visitors, age, cover;
    private String header, place, description, shortDescription;

    public Event(int id, int visitors, int age, int cover, String header, String place,
                 String description, String short_description) {
        this.id = id;
        this.visitors = visitors;
        this.age = age;
        this.cover = cover;
        this.header = header;
        this.place = place;
        this.description = description;
        this.shortDescription = short_description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVisitors() {
        return visitors;
    }

    public void setVisitors(int visitors) {
        this.visitors = visitors;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
