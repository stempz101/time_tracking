package com.tracking.controllers.services;

import com.tracking.controllers.constants.FilePaths;
import com.tracking.controllers.exceptions.DBException;
import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.dao.mysql.MysqlUserDAO;
import com.tracking.models.User;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service, that contains helper methods
 */
public abstract class Service {

    private static final Logger logger = Logger.getLogger(Service.class);

    public static final int TOTAL_ACTIVITIES = 6;
    public static final int TOTAL_CATEGORIES = 10;
    public static final int TOTAL_USERS = 10;
    public static final int TOTAL_USERS_ACTIVITY = 6;
    public static final int TOTAL_REQUESTS = 5;

    /**
     * Getting full URL
     * @param requestUrl received url
     * @param requestUri received uri
     * @param uri path of page
     * @return URL link
     */
    public static String getFullURL(String requestUrl, String requestUri, String uri) {
        return requestUrl.replace(requestUri, uri);
    }

    /**
     * Getting current Locale
     * @param lang received language
     * @return current Locale
     */
    public static Locale getLocale(String lang) {
        String[] splCurrLang = lang.split("_");
        return new Locale(splCurrLang[0], splCurrLang[1]);
    }

    /**
     * Building query for pagination
     * @param req for getting session to get/set attributes, getting parameters
     */
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

        if (!query.isEmpty())
            req.setAttribute("queryForPagination", req.getContextPath() + "?" + query + "&");
        else
            req.setAttribute("queryForPagination", req.getContextPath() + "?");
    }

    /**
     * Redirecting to the page with saved parameters, but without param "page". It used in case if param "page" was
     * equal or less than 0, and greater than assigned page count
     * @param req for getting session to get/set attributes, getting parameters
     * @param resp for sending redirect
     * @param path path to page
     * @param authUser authorized user
     * @throws IOException if something went wrong while redirecting
     */
    public void redirectWithoutPage(HttpServletRequest req, HttpServletResponse resp, String path, User authUser) throws IOException {
        List<String> paramsList = Arrays.stream(req.getQueryString().split("&")).collect(Collectors.toList());
        String role = authUser.isAdmin() ? "/a/" : "/u/";
        if (paramsList.size() > 1) {
            paramsList.remove(paramsList.size() - 1);
            String[] params = paramsList.toArray(String[]::new);
            logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                    role + path + "?" + String.join("&", params)));
            resp.sendRedirect(req.getContextPath() + role + path + "?" + String.join("&", params));
        } else {
            logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                    role + path));
            resp.sendRedirect(req.getContextPath() + role + path);
        }
    }

    /**
     * Getting page count
     * @param itemCount count of items
     * @param total total items on page
     * @return count of pages
     */
    public int getPageCount(int itemCount, int total) {
        return itemCount % total == 0 ? itemCount / total
                : itemCount / total + 1;
    }

    /**
     * Setting image name
     * @param part selected image
     * @return created image name
     */
    public String setImageName(Part part) {
        if (part == null)
            return null;
        String filename = part.getSubmittedFileName();
        if (filename == null || filename.isEmpty())
            return null;
        String[] splittedFilename = filename.split("\\.");
        String ext = splittedFilename[splittedFilename.length - 1];
        return new Date().getTime() + "." + ext;
    }

    /**
     * Saving user image
     * @param part selected image
     * @param filename created filename
     * @param realPath path of web project
     * @throws ServiceException if something went wrong while executing
     */
    public void saveUserImage(Part part, String filename, String realPath) throws ServiceException {
        if (filename == null || filename.isEmpty())
            return;
        String uploadPath = realPath + FilePaths.USER_IMG_UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists())
            uploadDir.mkdirs();
        try {
            part.write(uploadPath + filename);
            logger.info("User image is saved to this path: " + uploadPath + filename);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("Service: saveUserImage was failed", e);
        }
    }

    /**
     * Updating user image
     * @param part seleted image
     * @param filename created filename
     * @param oldImage name of old image
     * @param realPath path of web project
     * @throws ServiceException if something went wrong while executing
     */
    public void updateUserImage(Part part, String filename, String oldImage, String realPath) throws ServiceException {
        if (oldImage == null || oldImage.isEmpty()) {
            String uploadPath = realPath + FilePaths.USER_IMG_UPLOAD_DIRECTORY;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists())
                uploadDir.mkdirs();
            if (filename != null && !filename.isEmpty()) {
                try {
                    part.write(uploadPath + filename);
                    logger.info("User image is saved to this path: " + uploadPath + filename);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e);
                    throw new ServiceException("Service: updateUserImage was failed", e);
                }
            }
        } else {
            String oldImagePath = realPath + FilePaths.USER_IMG_UPLOAD_DIRECTORY + oldImage;
            File oldImageDir = new File(oldImagePath);
            logger.info("Removing user image from this path: " + oldImagePath);
            if (oldImageDir.exists())
                oldImageDir.delete();

            if (filename != null && !filename.isEmpty()) {
                String newImagePath = realPath + FilePaths.USER_IMG_UPLOAD_DIRECTORY + filename;
                try {
                    part.write(newImagePath);
                    logger.info("User image is saved to this path: " + newImagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e);
                    throw new ServiceException("Service: updateUserImage was failed", e);
                }
            }
        }
    }

    /**
     * Saving activity image
     * @param part selected image
     * @param filename created filename
     * @param realPath path of web project
     * @throws ServiceException if something went wrong while executing
     */
    public void saveActivityImage(Part part, String filename, String realPath) throws ServiceException {
        if (filename == null || filename.isEmpty())
            return;
        String uploadPath = realPath + FilePaths.ACTIVITY_IMG_UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists())
            uploadDir.mkdirs();
        try {
            part.write(uploadPath + filename);
            logger.info("Activity image is saved to this path: " + uploadPath + filename);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("Service: saveActivityImage was failed", e);
        }
    }

    /**
     * Updating activity image
     * @param part selected image
     * @param filename created filename
     * @param oldImage name of old image
     * @param realPath path to web project
     * @throws ServiceException if something went wrong while executing
     */
    public void updateActivityImage(Part part, String filename, String oldImage, String realPath) throws ServiceException {
        if (oldImage == null || oldImage.isEmpty()) {
            String uploadPath = realPath + FilePaths.ACTIVITY_IMG_UPLOAD_DIRECTORY;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists())
                uploadDir.mkdirs();
            if (filename != null && !filename.isEmpty()) {
                try {
                    part.write(uploadPath + filename);
                    logger.info("Activity image is saved to this path: " + uploadPath + filename);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e);
                    throw new ServiceException("Service: updateActivityImage was failed", e);
                }
            }
        } else {
            String oldImagePath = realPath + FilePaths.ACTIVITY_IMG_UPLOAD_DIRECTORY + oldImage;
            File oldImageDir = new File(oldImagePath);
            logger.info("Removing activity image from this path: " + oldImagePath);
            if (oldImageDir.exists())
                oldImageDir.delete();

            if (filename != null && !filename.isEmpty()) {
                String newImagePath = realPath + FilePaths.ACTIVITY_IMG_UPLOAD_DIRECTORY + filename;
                try {
                    part.write(newImagePath);
                    logger.info("Activity image is saved to this path: " + newImagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e);
                    throw new ServiceException("Service: updateActivityImage was failed", e);
                }
            }
        }
    }

    /**
     * Deleting activity image
     * @param image image name
     * @param realPath path of activity uploaded images
     */
    public void deleteActivityImage(String image, String realPath) {
        if (image == null || image.isEmpty())
            return;
        String oldImagePath = realPath + FilePaths.ACTIVITY_IMG_UPLOAD_DIRECTORY + image;
        File oldImageDir = new File(oldImagePath);
        oldImageDir.delete();
    }
}
