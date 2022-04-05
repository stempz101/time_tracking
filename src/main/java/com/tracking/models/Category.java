package com.tracking.models;

/**
 * Category Model. Here there is defined what need to store
 */
public class Category {

    /**
     * Id of category
     */
    private int id;
    /**
     * Name of category in English
     */
    private String nameEN;
    /**
     * Name of category in Ukrainian
     */
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
