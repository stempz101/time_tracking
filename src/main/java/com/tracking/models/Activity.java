package com.tracking.models;

import java.util.*;

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
     * Creation time of activity
     */
    private Date createTime;
    /**
     * Activity status
     */
    private Status status;

    public Activity() {

    }

    public Activity(int id, String name, List<Integer> categories, String description, String image, int peopleCount, int creatorId, Date createTime, Status status) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.description = description;
        this.image = image;
        this.peopleCount = peopleCount;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.status = status;
    }

    public Activity(int id, String name, String description, String image, int peopleCount, int creatorId, Date createTime, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.peopleCount = peopleCount;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.status = status;
    }

    public Activity(String name, List<Integer> categories, String description, String image, int creatorId, Status status) {
        this.name = name;
        this.categories = categories;
        this.description = description;
        this.image = image;
        this.creatorId = creatorId;
        this.status = status;
    }

    public Activity(int id, String name, Status status) {
        this.id = id;
        this.name = name;
        this.status = status;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        BY_ADMIN("BY_ADMIN"),
        BY_USER("BY_USER"),
        ADD_WAITING("ADD_WAITING"),
        ADD_DECLINED("ADD_DECLINED"),
        DEL_WAITING("DEL_WAITING"),
        DEL_CONFIRMED("DEL_CONFIRMED");

        private final String value;

        private static final Map<String, Status> lookup = new HashMap<>();

        static {
            for (Status s : Status.values()) {
                lookup.put(s.getValue(), s);
            }
        }

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Status get(String value) {
            return lookup.get(value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id == activity.id && peopleCount == activity.peopleCount && creatorId == activity.creatorId && Objects.equals(name, activity.name) && Objects.equals(categories, activity.categories) && Objects.equals(description, activity.description) && Objects.equals(image, activity.image) && Objects.equals(createTime, activity.createTime) && status == activity.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, categories, description, image, peopleCount, creatorId, createTime, status);
    }
}
