package com.tracking.models;

import java.util.Date;

/**
 * UserActivity Model. Here there is defined what need to store
 */
public class UserActivity {
    /**
     * Id of user
     */
    private int userId;
    /**
     * Last name of user
     */
    private String userLastName;
    /**
     * First name of user
     */
    private String userFirstName;
    /**
     * Filename of user image
     */
    private String userImage;
    /**
     * Is this user admin/user
     */
    private boolean isAdmin;
    /**
     * Id of activity
     */
    private int activityId;
    /**
     * Name of activity
     */
    private String activityName;
    /**
     * Start time of activity
     */
    private Date startTime;
    /**
     * Stop time of activity
     */
    private Date stopTime;
    /**
     * Total spent time on activity since adding in
     */
    private double spentTime;
    /**
     * Status of user in activity
     */
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

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
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
