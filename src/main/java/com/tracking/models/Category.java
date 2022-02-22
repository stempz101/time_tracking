package com.tracking.models;

public class Category {

    private int id;
    private String nameEN;
    private String nameUA;

    public Category() {

    }

    public Category(int id, String nameEN, String nameUA) {
        this.id = id;
        this.nameEN = nameEN;
        this.nameUA = nameUA;
    }

    public Category(String nameEN, String nameUA) {
        this.nameEN = nameEN;
        this.nameUA = nameUA;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getNameUA() {
        return nameUA;
    }

    public void setNameUA(String nameUA) {
        this.nameUA = nameUA;
    }
}
