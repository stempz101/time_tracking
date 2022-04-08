package com.tracking.dao;

import com.tracking.controllers.services.Service;
import com.tracking.controllers.exceptions.DBException;
import com.tracking.models.Activity;
import com.tracking.models.User;
import com.tracking.models.UserActivity;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Activity DAO interface. Contains all essential methods for work with activities from DB
 */
public interface ActivityDAO {

    /**
     * Creates activity in DB
     * @param activity created activity
     * @return created activity
     * @throws DBException if something went wrong while executing
     */
    Activity create(Activity activity) throws DBException;

    /**
     * Getting all activities from DB
     * @param sort sort type
     * @param order order type
     * @param peopleFrom min count of people
     * @param peopleTo max count of people
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @param authUser authorized user
     * @return received activities
     * @throws DBException if something went wrong while executing
     */
    List<Activity> getAll(String sort, String order, int peopleFrom, int peopleTo, int start, int total, User authUser) throws DBException;

    /**
     * Getting all activities by search query from DB
     * @param searchQuery entered search query
     * @param sort sort type
     * @param order order type
     * @param peopleFrom min count of people
     * @param peopleTo max count of people
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @param authUser authorized user
     * @return received activities
     * @throws DBException if something went wrong while executing
     */
    List<Activity> getAllLike(String searchQuery, String sort, String order, int peopleFrom, int peopleTo, int start, int total, User authUser) throws DBException;

    /**
     * Getting all activities by selected categories from DB
     * @param categoryList selected categories
     * @param sort sort type
     * @param order order type
     * @param peopleFrom min count of people
     * @param peopleTo max count of people
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @param authUser authorized user
     * @return received activities
     * @throws DBException if something went wrong while executing
     */
    List<Activity> getAllWhereCategory(List<Integer> categoryList, String sort, String order, int peopleFrom, int peopleTo, int start, int total, User authUser) throws DBException;

    /**
     * Getting all activities from DB, that don't have categories
     * @param sort sort type
     * @param order order type
     * @param peopleFrom min count of people
     * @param peopleTo max count of people
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @param authUser authorized user
     * @return received activities
     * @throws DBException if something went wrong while executing
     */
    List<Activity> getAllWhereCategoryIsNull(String sort, String order, int peopleFrom, int peopleTo,int start, int total, User authUser) throws DBException;

    /**
     * Getting all activities by search query and selected categories from DB
     * @param searchQuery entered search query
     * @param categoryList selected categories
     * @param sort sort type
     * @param order order type
     * @param peopleFrom min count of people
     * @param peopleTo max count of people
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @param authUser authorized user
     * @return received activities
     * @throws DBException if something went wrong while executing
     */
    List<Activity> getAllLikeAndWhereCategory(String searchQuery, List<Integer> categoryList, String sort, String order, int peopleFrom, int peopleTo, int start, int total, User authUser) throws DBException;

    /**
     * Getting all activities by search query from DB, that don't have categories
     * @param searchQuery entered search query
     * @param sort sort type
     * @param order order type
     * @param peopleFrom min count of people
     * @param peopleTo max count of people
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @param authUser authorized user
     * @return received activities
     * @throws DBException if something went wrong while executing
     */
    List<Activity> getAllLikeAndWhereCategoryIsNull(String searchQuery, String sort, String order, int peopleFrom, int peopleTo, int start, int total, User authUser) throws DBException;

    /**
     * Getting all created activities by creator from DB
     * @param creatorId id of creator
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @return received activities
     * @throws DBException if something went wrong while executing
     */
    List<Activity> getAllCreated(int creatorId, int start, int total) throws DBException;

    /**
     * Getting all user activities with his information for profile from DB
     * @param userId id of user
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @return received activities
     * @throws DBException if something went wrong while executing
     */
    List<UserActivity> getAllForProfile(int userId, int start, int total) throws DBException;

    /**
     * Getting activity by id from DB
     * @param id id of activity
     * @return received activity
     * @throws DBException if something went wrong while executing
     */
    Activity getById(int id) throws DBException;

    /**
     * Getting count of activities from DB
     * @param peopleFrom min count of people
     * @param peopleTo max count of people
     * @param authUser authorized user
     * @return count of activities
     * @throws DBException if something went wrong while executing
     */
    int getCount(int peopleFrom, int peopleTo, User authUser) throws DBException;

    /**
     * Getting count of activities by search query from DB
     * @param searchQuery entered search query
     * @param peopleFrom min count of people
     * @param peopleTo max count of people
     * @param authUser authorized user
     * @return count of activities
     * @throws DBException if something went wrong while executing
     */
    int getCountWhereLike(String searchQuery, int peopleFrom, int peopleTo, User authUser) throws DBException;

    /**
     * Getting count of activities by selected categories from DB
     * @param categoryList selected categories
     * @param peopleFrom min count of people
     * @param peopleTo max count of people
     * @param authUser authorized user
     * @return count of activities
     * @throws DBException if something went wrong while executing
     */
    int getCountWhereCategory(List<Integer> categoryList, int peopleFrom, int peopleTo, User authUser) throws DBException;

    /**
     * Getting count of activities from DB, that don't have categories
     * @param peopleFrom min count of people
     * @param peopleTo max count of people
     * @param authUser authorized user
     * @return count of activities
     * @throws DBException if something went wrong while executing
     */
    int getCountWhereCategoryIsNull(int peopleFrom, int peopleTo, User authUser) throws DBException;

    /**
     * Getting count of activities by search query and selected categories from DB
     * @param searchQuery entered search query
     * @param categoryList selected categories
     * @param peopleFrom min count of people
     * @param peopleTo max count of people
     * @param authUser authorized user
     * @return count of activities
     * @throws DBException if something went wrong while executing
     */
    int getCountWhereLikeAndCategory(String searchQuery, List<Integer> categoryList, int peopleFrom, int peopleTo, User authUser) throws DBException;

    /**
     * Getting count of activities by search query from DB, that don't have categories
     * @param searchQuery entered search query
     * @param peopleFrom min count of people
     * @param peopleTo max count of people
     * @param authUser authorized user
     * @return count of activities
     * @throws DBException if something went wrong while executing
     */
    int getCountWhereLikeAndCategoryIsNull(String searchQuery, int peopleFrom, int peopleTo, User authUser) throws DBException;

    /**
     * Getting count of users in activity from DB
     * @param activityId id of activity
     * @return count of users in activity
     * @throws DBException if something went wrong while executing
     */
    int getCountInActivity(int activityId) throws DBException;

    /**
     * Getting count of users in activity by entered last name and first name  from DB
     * @param activityId id of activity
     * @param lastName entered last name
     * @param firstName entered first name
     * @return count of users in activity
     * @throws DBException if something went wrong while executing
     */
    int getCountInActivityWhereName(int activityId, String lastName, String firstName) throws DBException;

    /**
     * Getting count of created activities by admin from DB
     * @param creatorId id of creator
     * @return count of activities
     * @throws DBException if something went wrong while executing
     */
    int getCreatedCount(int creatorId) throws DBException;

    /**
     * Getting count of activities for profile from DB, in which user participates
     * @param userId id of user
     * @return count of activities
     * @throws DBException if something went wrong while executing
     */
    int getCountForProfile(int userId) throws DBException;

    /**
     * Getting max people count among activities from DB
     * @return max count of people
     * @throws DBException if something went wrong while executing
     */
    int getMaxPeopleCount() throws DBException;

    /**
     * Getting creator of activity from DB
     * @param activityId id of activity
     * @return creator of activity
     * @throws DBException if something went wrong while executing
     */
    User getCreator(int activityId) throws DBException;

    /**
     * Setting categories for activity to DB
     * @param con connection of DB
     * @param categoryIds ids of categories
     * @param activityId id of activity
     * @throws DBException if something went wrong while executing
     */
    void setActivityCategories(Connection con, List<Integer> categoryIds, int activityId) throws DBException;

    /**
     * Getting category ids for activity from DB
     * @param con connection of DB
     * @param activityId id of activity
     * @return received category ids
     * @throws DBException if something went wrong while executing
     */
    List<Integer> withCategories(Connection con, int activityId) throws DBException;

    /**
     * Updating activity categories in DB
     * @param con connection of DB
     * @param categoryIds ids of categories
     * @param activityId id of activity
     * @throws DBException if something went wrong while executing
     */
    void updateActivityCategories(Connection con, List<Integer> categoryIds, int activityId) throws DBException;

    /**
     * Adding user to activity in DB
     * @param activityId id of activity
     * @param userId id of user
     * @throws DBException if something went wrong while executing
     */
    void addUser(int activityId, int userId) throws DBException;

    /**
     * Delete user from activity in DB
     * @param activityId id of activity
     * @param userId id of user
     * @throws DBException if something went wrong while executing
     */
    void deleteUser(int activityId, int userId) throws DBException;

    /**
     * Updating activity in DB
     * @param activity updated activity
     * @return true if update was successful, false if activity didn't go through validation
     * @throws DBException if something went wrong while executing
     */
    boolean update(Activity activity) throws DBException;

    /**
     * Deleting activity from DB
     * @param activityId id of activity
     * @return true if activity is deleted
     * @throws DBException if something went wrong while executing
     */
    boolean delete(int activityId) throws DBException;

    /**
     * Deleting activity by creator from DB
     * @param activityId id of activity
     * @param creatorId id of creator
     * @throws DBException if something went wrong while executing
     */
    void deleteByCreator(int activityId, int creatorId) throws DBException;

    /**
     * Checking if activity is confirmed in requests in DB
     * @param con connection of DB
     * @param activity selected activity
     * @return true if activity confirmed
     * @throws DBException something went wrong while executing
     */
    boolean isConfirmed(Connection con, Activity activity) throws DBException;

    /**
     * Checking if activity is for delete in requests in DB
     * @param con connection of DB
     * @param activity selected activity
     * @return true if activity is for delete
     * @throws DBException something went wrong while executing
     */
    boolean isForDelete(Connection con, Activity activity) throws DBException;

    /**
     * Activity name validation
     * @param name entered name
     * @return true if validation was successful
     */
    static boolean validateName(String name) {
        return name != null && !name.isEmpty() && name.matches("[а-щА-ЩЬьЮюЯяЇїІіЄєҐґ'\\w\\s?!,.\\-()]+");
    }

    /**
     * Activity description validation
     * @param description entered description
     * @return true if validation was successful
     */
    static boolean validateDescription(String description) {
        return description != null && !description.isEmpty();
    }
}
