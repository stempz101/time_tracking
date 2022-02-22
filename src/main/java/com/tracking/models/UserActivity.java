package com.tracking.models;

public class UserActivity {
    private int userId;
    private String userLastName;
    private String userFirstName;
    private String userImage;
    private boolean isAdmin;
    private String startTime;
    private String stopTime;
    private double spentTime;
    private Status status;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public double getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(long spentTime) {
        double st = (double) spentTime;
        this.spentTime = Math.round(st / 3600.0 * 10.0) / 10.0;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        NOT_STARTED("NOT_STARTED"),
        STARTED("STARTED"),
        STOPPED("STOPPED");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
