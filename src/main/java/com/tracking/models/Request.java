package com.tracking.models;

import java.util.Date;

/**
 * Category Model. Here there is defined what need to store
 */
public class Request {

    /**
     * Id of request
     */
    private int id;
    /**
     * Id of activity in request
     */
    private int activityId;
    /**
     * Status of request
     */
    private Status status;
    /**
     * Is this request sent for delete
     */
    private boolean forDelete;
    /**
     * Create time of request
     */
    private Date createTime;
    /**
     * Activity in request
     */
    private Activity activity;
    /**
     * Creator of request (activity)
     */
    private User creator;

    public Request(int id, int activityId, Status status, boolean forDelete, Date createTime) {
        this.id = id;
        this.activityId = activityId;
        this.status = status;
        this.forDelete = forDelete;
        this.createTime = createTime;
    }

    public Request() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isForDelete() {
        return forDelete;
    }

    public void setForDelete(boolean forDelete) {
        this.forDelete = forDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public enum Status {
        WAITING("WAITING"),
        CONFIRMED("CONFIRMED");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
