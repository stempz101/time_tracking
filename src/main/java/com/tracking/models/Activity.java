package com.tracking.models;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Activity Model. Here there is defined what need to store
 */
public class Activity {

    /**
     * Id of activity
     */
    private int id;
    /**
     * Name of activity
     */
    private String name;
    /**
     * Categories of activity
     */
    private List<Integer> categories;
    /**
     * Description of activity
     */
    private String description;
    /**
     * Filename of activity image
     */
    private String image;
    /**
     * People count in activity
     */
    private int peopleCount;
    /**
     * Creator id of activity
     */
    private int creatorId;
    /**
     * Created by admin/user
     */
    private boolean byAdmin;
    /**
     * Creation time of activity
     */
    private Date createTime;
    /**
     * Is this activity sent for delete
     */
    private boolean forDelete = false;

    public Activity() {

    }

    public Activity(int id, String name, List<Integer> categories, String description, String image, int peopleCount, int creatorId, boolean byAdmin, Date createTime, boolean forDelete) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.description = description;
        this.image = image;
        this.peopleCount = peopleCount;
        this.creatorId = creatorId;
        this.byAdmin = byAdmin;
        this.createTime = createTime;
        this.forDelete = forDelete;
    }

    public Activity(int id, String name, String description, String image, int peopleCount, int creatorId, boolean byAdmin, Date createTime, boolean forDelete) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.peopleCount = peopleCount;
        this.creatorId = creatorId;
        this.byAdmin = byAdmin;
        this.createTime = createTime;
        this.forDelete = forDelete;
    }

    public Activity(String name, List<Integer> categories, String description, String image, int creatorId, boolean byAdmin) {
        this.name = name;
        this.categories = categories;
        this.description = description;
        this.image = image;
        this.creatorId = creatorId;
        this.byAdmin = byAdmin;
    }

    public Activity(int id, String name) {
        this.id = id;
        this.name = name;
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

    public boolean isByAdmin() {
        return byAdmin;
    }

    public void setByAdmin(boolean byAdmin) {
        this.byAdmin = byAdmin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isForDelete() {
        return forDelete;
    }

    public void setForDelete(boolean forDelete) {
        this.forDelete = forDelete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id == activity.id && peopleCount == activity.peopleCount && creatorId == activity.creatorId && byAdmin == activity.byAdmin && forDelete == activity.forDelete && Objects.equals(name, activity.name) && Objects.equals(categories, activity.categories) && Objects.equals(description, activity.description) && Objects.equals(image, activity.image) && Objects.equals(createTime, activity.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, categories, description, image, peopleCount, creatorId, byAdmin, createTime, forDelete);
    }
}
