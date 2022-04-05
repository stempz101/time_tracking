package com.tracking.models;

import java.util.Objects;

/**
 * User Model. Here there is defined what need to store
 */
public class User {

    /**
     * Id of user
     */
    private int id;
    /**
     * Last name of user
     */
    private String lastName;
    /**
     * First name of user
     */
    private String firstName;
    /**
     * Email of user
     */
    private String email;
    /**
     * Password of user
     */
    private String password;
    /**
     * Filename of user image
     */
    private String image;
    /**
     * Count of activities, in which user participates
     */
    private int activityCount;
    /**
     * Total spent time on activities
     */
    private double spentTime;
    /**
     * Is this user admin/user
     */
    private boolean isAdmin = false;
    /**
     * Is this user blocked/unblocked
     */
    private boolean isBlocked = false;

    public User() {

    }

    public User(String lastName, String firstName, String email, String password, String image) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public User(int id, String lastName, String firstName, String image) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(int activityCount) {
        this.activityCount = activityCount;
    }

    public double getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(long spentTime) {
        double st = (double) spentTime;
        this.spentTime = Math.round(st / 3600.0 * 10.0) / 10.0;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
