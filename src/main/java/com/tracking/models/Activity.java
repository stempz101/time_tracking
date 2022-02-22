package com.tracking.models;

import java.util.List;

public class Activity {

    private int id;
    private String name;
    private List<Integer> categories;
    private String description;
    private String image;
    private int peopleCount;
    private int creatorId;
    private String createTime;
    private Status status;

    public Activity() {

    }

    public Activity(String name, List<Integer> categories, String description, String image, int creatorId, Status status) {
        this.name = name;
        this.categories = categories;
        this.description = description;
        this.image = image;
        this.creatorId = creatorId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void clear() {
        name = null;
        categories = null;
        description = null;
        image = null;
    }

    public enum Status {
        WAITING,
        CONFIRMED,
        DECLINED,
        BY_ADMIN
    }
}
