package com.tracking.dao;

import com.tracking.models.Activity;
import com.tracking.models.User;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ActivityDAO {

    boolean create(HttpSession session, Activity activity) throws SQLException;

    List<Activity> getAll(int peopleFrom, int peopleTo, int start, int total) throws SQLException;

    List<Activity> getAllOrder(String sort, String order, int peopleFrom, int peopleTo, int start, int total) throws SQLException;

    List<Activity> getAllLike(String searchQuery, int peopleFrom, int peopleTo, int start, int total) throws SQLException;

    List<Activity> getAllLikeOrder(String searchQuery, String sort, String order, int peopleFrom, int peopleTo, int start, int total) throws SQLException;

    List<Activity> getAllWhereCategory(List<Integer> categoryList, int peopleFrom, int peopleTo, int start, int total) throws SQLException;

    List<Activity> getAllWhereCategoryOrder(List<Integer> categoryList, String sort, String order, int peopleFrom, int peopleTo, int start, int total) throws SQLException;

    List<Activity> getAllWhereCategoryIsNull(int peopleFrom, int peopleTo,int start, int total) throws SQLException;

    List<Activity> getAllWhereCategoryIsNullOrder(String sort, String order, int peopleFrom, int peopleTo, int start, int total) throws SQLException;

    List<Activity> getAllLikeAndWhereCategory(String searchQuery, List<Integer> categoryList, int peopleFrom, int peopleTo, int start, int total) throws SQLException;

    List<Activity> getAllLikeAndWhereCategoryOrder(String searchQuery, List<Integer> categoryList, String sort, String order, int peopleFrom, int peopleTo, int start, int total) throws SQLException;

    List<Activity> getAllLikeAndWhereCategoryIsNull(String searchQuery, int peopleFrom, int peopleTo, int start, int total) throws SQLException;

    List<Activity> getAllLikeAndWhereCategoryIsNullOrder(String searchQuery, String sort, String order, int peopleFrom, int peopleTo, int start, int total) throws SQLException;

    Activity getById(int id) throws SQLException;

    int getCount(int peopleFrom, int peopleTo) throws SQLException;

    int getCountWhereLike(String searchQuery, int peopleFrom, int peopleTo) throws SQLException;

    int getCountWhereCategory(List<Integer> categoryList, int peopleFrom, int peopleTo) throws SQLException;

    int getCountWhereCategoryIsNull(int peopleFrom, int peopleTo) throws SQLException;

    int getCountWhereLikeAndCategory(String searchQuery, List<Integer> categoryList, int peopleFrom, int peopleTo) throws SQLException;

    int getCountWhereLikeAndCategoryIsNull(String searchQuery, int peopleFrom, int peopleTo) throws SQLException;

    int getCountInActivity(int activityId) throws SQLException;

    int getCountInActivityWhereName(int activityId, String lastName, String firstName) throws SQLException;

    int getMaxPeopleCount() throws SQLException;

    User getCreator(int activityId) throws SQLException;

    void setActivityCategories(Connection con, List<Integer> categoryIds, int activityId) throws SQLException;

    void updateActivityCategories(Connection con, List<Integer> categoryIds, int activityId) throws SQLException;

    void addUser(int activityId, int userId) throws SQLException;

    void deleteUser(int activityId, int userId) throws SQLException;

    boolean update(HttpSession session, Activity activity) throws SQLException;

    void delete(HttpSession session, int activityId) throws SQLException;

    static boolean validate(HttpSession session, Activity activity) {
        int errorCount = 0;
        String name = activity.getName();
        if (name == null || name.isEmpty()) {
            session.setAttribute("activityNameError", "Activity name cannot be empty");
            errorCount++;
        } else if (!name.matches("[а-щА-ЩЬьЮюЯяЇїІіЄєҐґ'\\w\\s]+")) {
            session.setAttribute("activityNameError", "Activity name input is invalid");
            errorCount++;
        }

        String description = activity.getDescription();
        if (description == null || description.isEmpty()) {
            session.setAttribute("activityDescriptionError", "Description cannot be empty");
            errorCount++;
        }

        return errorCount == 0;
    }
}
