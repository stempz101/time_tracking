package com.tracking.controllers.constants;

import java.io.File;

/**
 * Abstract class FilePaths, where the used paths are stored
 */
public abstract class FilePaths {

    private FilePaths() {

    }

    // DEFAULT
    /**
     * User's default image path
     */
    public static final String GET_USER_DEFAULT_IMG = "/img/default/user.png";
    /**
     * Activity default image path
     */
    public static final String GET_ACTIVITY_DEFAULT_IMG = "/img/default/activity.png";

    // UPLOAD
    /**
     * Path of directory, where user images are stored
     */
    public static final String USER_IMG_UPLOAD_DIRECTORY =
            "img" + File.separator + "upload" + File.separator + "users" + File.separator;
    /**
     * Path of directory, where activity images are stored
     */
    public static final String ACTIVITY_IMG_UPLOAD_DIRECTORY =
            "img" + File.separator + "upload" + File.separator + "activities" + File.separator;

    /**
     * User's upload image path
     */
    public static final String GET_USER_IMG_UPLOAD_DIRECTORY = "/img/upload/users/";
    /**
     * Activity upload image path
     */
    public static final String GET_ACTIVITY_IMG_UPLOAD_DIRECTORY = "/img/upload/activities/";
}
