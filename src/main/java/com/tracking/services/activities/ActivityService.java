package com.tracking.services.activities;

import com.tracking.controllers.constants.FilePaths;
import com.tracking.dao.ActivityDAO;
import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.lang.Language;
import com.tracking.models.Activity;
import com.tracking.models.Category;
import com.tracking.models.User;
import com.tracking.models.UserActivity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ActivityService {

    public boolean add(HttpSession session, Activity activity) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        boolean result = false;
        try {
            result = activityDAO.create(session, activity);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return result;
    }

    public boolean update(HttpSession session, Activity activity) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        boolean result = false;
        try {
            result = activityDAO.update(session, activity);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return result;
    }

    public void delete(HttpSession session, int activityId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            Activity activity = activityDAO.getById(activityId);
            activityDAO.delete(session, activityId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public Activity get(int activityId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        Activity activity = null;
        try {
            activity = activityDAO.getById(activityId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activity;
    }

    public List<Integer> getAllCategoryIds(HttpServletRequest req) {
        String[] ids = req.getParameterValues("activityCategory");
        if (ids != null && ids.length > 0)
            return Arrays.stream(req.getParameterValues("activityCategory"))
                    .map(Integer::parseInt)
                    .toList();
        return null;
    }

    public List<Category> getActivityCategories(List<Integer> categoryIds, Language language) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        List<Category> categories = null;
        try {
            categories = categoryDAO.getAllById(categoryIds, language); // localize
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return categories;
    }

    public List<UserActivity> getActivityUsers(int activityId, int start, int total) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        List<UserActivity> userList = null;
        try {
            userList = userDAO.getAllWhereInActivity(activityId, start, total);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return userList;
    }

    public List<User> getAvailableUsers(int activityId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        List<User> userList = null;
        try {
            userList = userDAO.getAllWhereNotInActivity(activityId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return userList;
    }

    public User getCreator(int activityId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        User creator = null;
        try {
            creator = activityDAO.getCreator(activityId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return creator;
    }

    public UserActivity getCreatorInfo(User creator, int activityId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        UserActivity creatorInfo = null;
        try {
            creatorInfo = userDAO.getWhereInActivity(creator, activityId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return creatorInfo;
    }

    public String setImageName(Part part) {
        String filename = part.getSubmittedFileName();
        if (filename == null || filename.isEmpty())
            return null;
        String[] splittedFilename = filename.split("\\.");
        String ext = splittedFilename[splittedFilename.length - 1];
        return new Date().getTime() + "." + ext;
    }

    public void saveImage(Part part, String filename, String realPath) throws IOException {
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

    public void updateImage(Part part, String filename, String oldImage, String realPath) throws IOException {
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

    public void deleteImage(String image, String realPath) {
        if (image == null || image.isEmpty())
            return;
        String oldImagePath = realPath + FilePaths.ACTIVITY_IMG_UPLOAD_DIRECTORY + image;
        File oldImageDir = new File(oldImagePath);
        oldImageDir.delete();
    }

    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        try {
            int activityId = Integer.parseInt(req.getParameter("id"));
            Activity activity = get(activityId);

            int userActivityCount = activity.getPeopleCount();
            int start = 1;
            int total = 6;
            int page = getPageCount(userActivityCount, total);
            int pageCount = userActivityCount % total == 0 ? userActivityCount / total
                    : userActivityCount / total + 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page <= 0 || page > pageCount) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                start = start + total * (page - 1);
            }
            int previousPage = 0;
            if (page > 1)
                previousPage = page - 1;
            int nextPage = 0;
            if (userActivityCount > total && page < pageCount)
                nextPage = page + 1;

            HttpSession session = req.getSession();
            List<Category> categories = getActivityCategories(activity.getCategories(), Language.EN); // localize
            List<UserActivity> users = getActivityUsers(activityId, start, total);
            UserActivity userActivity = getCreatorInfo((User) session.getAttribute("authUser"), activityId);
            List<User> usersNotInActivity = getAvailableUsers(activityId);
            User creator = getCreator(activityId);

            req.setAttribute("pageCount", pageCount);
            req.setAttribute("previousPage", previousPage);
            req.setAttribute("nextPage", nextPage);
            req.setAttribute("activity", activity);
            req.setAttribute("categories", categories);
            req.setAttribute("users", users);
            req.setAttribute("userActivity", userActivity);
            req.setAttribute("usersNotInActivity", usersNotInActivity);
            req.setAttribute("creator", creator);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    private int getPageCount(int itemCount, int total) {
        int pageCount = itemCount % total == 0 ? itemCount / total
                : itemCount / total + 1;
        return pageCount;
    }

    public void addUser(int activityId, int userId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            activityDAO.addUser(activityId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void removeUser(int activityId, int userId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            activityDAO.deleteUser(activityId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void startTime(int activityId, int userId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        try {
            userDAO.startTime(activityId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void stopTime(int activityId, int userId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        try {
            userDAO.stopTime(activityId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }
}
