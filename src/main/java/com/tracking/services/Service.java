package com.tracking.services;

import com.tracking.controllers.constants.FilePaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

public abstract class Service {

    public static final int TOTAL_ACTIVITIES = 6;
    public static final int TOTAL_CATEGORIES = 10;
    public static final int TOTAL_USERS = 10;
    public static final int TOTAL_USERS_ACTIVITY = 6;

    public void setQueryStringForPagination(HttpServletRequest req) {
        Enumeration<String> params = req.getParameterNames();
        StringBuilder query = new StringBuilder();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            if (paramName.equals("page"))
                continue;
            String[] paramValues = req.getParameterValues(paramName);
            for (String paramValue : paramValues) {
                query.append(paramName)
                        .append("=")
                        .append(paramValue)
                        .append("&");
            }
        }
        if (query.length() > 0)
            query.delete(query.length() - 1, query.length());
        System.out.println(req.getContextPath() + "?" + query);
        req.setAttribute("queryForPagination", req.getContextPath() + "?" + query);
    }

    protected int getPageCount(int itemCount, int total) {
        int pageCount = itemCount % total == 0 ? itemCount / total
                : itemCount / total + 1;
        return pageCount;
    }

    public String setImageName(Part part) {
        String filename = part.getSubmittedFileName();
        if (filename == null || filename.isEmpty())
            return null;
        String[] splittedFilename = filename.split("\\.");
        String ext = splittedFilename[splittedFilename.length - 1];
        return new Date().getTime() + "." + ext;
    }

    public void saveUserImage(Part part, String filename, String realPath) throws IOException {
        if (filename == null || filename.isEmpty())
            return;
        String uploadPath = realPath + FilePaths.USER_IMG_UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists())
            uploadDir.mkdirs();
        try {
            part.write(uploadPath + filename);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    public void saveActivityImage(Part part, String filename, String realPath) throws IOException {
        if (filename == null || filename.isEmpty())
            return;
        String uploadPath = realPath + FilePaths.ACTIVITY_IMG_UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists())
            uploadDir.mkdirs();
        try {
            part.write(uploadPath + filename);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    public void updateActivityImage(Part part, String filename, String oldImage, String realPath) throws IOException {
        if (filename == null || filename.isEmpty())
            return;
        if (oldImage == null || oldImage.isEmpty()) {
            String uploadPath = realPath + FilePaths.ACTIVITY_IMG_UPLOAD_DIRECTORY;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists())
                uploadDir.mkdirs();
            try {
                part.write(uploadPath + filename);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException();
            }
        } else {
            String oldImagePath = realPath + FilePaths.ACTIVITY_IMG_UPLOAD_DIRECTORY + oldImage;
            File oldImageDir = new File(oldImagePath);
            oldImageDir.delete();

            String newImagePath = realPath + FilePaths.ACTIVITY_IMG_UPLOAD_DIRECTORY + filename;
            try {
                part.write(newImagePath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException();
            }
        }
    }

    public void deleteActivityImage(String image, String realPath) {
        if (image == null || image.isEmpty())
            return;
        String oldImagePath = realPath + FilePaths.ACTIVITY_IMG_UPLOAD_DIRECTORY + image;
        File oldImageDir = new File(oldImagePath);
        oldImageDir.delete();
    }
}
