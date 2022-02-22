package com.tracking.controllers.constants;

import java.io.File;

public abstract class FilePaths {

    private FilePaths() {

    }

    // DEFAULT
    public static final String ACTIVITY_DEFAULT_IMG =
            "img" + File.separator + "default" + File.separator + "activity.png";
    public static final String GET_ACTIVITY_DEFAULT_IMG = "/img/default/activity.png";
    public static final String GET_PEOPLE_SVG = "/img/default/people.svg";

    // UPLOAD
    public static final String ACTIVITY_IMG_UPLOAD_DIRECTORY =
            "img" + File.separator + "upload" + File.separator + "activities" + File.separator;
    public static final String GET_ACTIVITY_IMG_UPLOAD_DIRECTORY = "/img/upload/activities/";
}
