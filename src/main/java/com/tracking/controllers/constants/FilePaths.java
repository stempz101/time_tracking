package com.tracking.controllers.constants;

import java.io.File;

public abstract class FilePaths {

    private FilePaths() {

    }

    // DEFAULT
    public static final String USER_DEFAULT_IMG =
            "img" + File.separator + "default" + File.separator + "user.png";
    public static final String GET_USER_DEFAULT_IMG = "/img/default/user.png";

    public static final String ACTIVITY_DEFAULT_IMG =
            "img" + File.separator + "default" + File.separator + "activity.png";
    public static final String GET_ACTIVITY_DEFAULT_IMG = "/img/default/activity.png";

    // UPLOAD
    public static final String USER_IMG_UPLOAD_DIRECTORY =
            "img" + File.separator + "upload" + File.separator + "users" + File.separator;
    public static final String GET_USER_IMG_UPLOAD_DIRECTORY = "/img/upload/users/";

    public static final String ACTIVITY_IMG_UPLOAD_DIRECTORY =
            "img" + File.separator + "upload" + File.separator + "activities" + File.separator;
    public static final String GET_ACTIVITY_IMG_UPLOAD_DIRECTORY = "/img/upload/activities/";
}
