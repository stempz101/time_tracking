package com.tracking.dao;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.models.Activity;
import com.tracking.models.Request;
import com.tracking.models.User;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.List;

/**
 * Request DAO interface. Contains all essential methods for work with requests from DB
 */
public interface RequestDAO {

    /**
     * Creates request in DB
     * @param req for getting session to get/set attributes, getting parameters
     * @param activity activity for request
     * @param forDelete true if request for delete
     * @return true if request is created, false if activity didn't go through validation
     * @throws DBException if something went wrong while executing
     */
    boolean create(HttpServletRequest req, Activity activity, boolean forDelete) throws DBException;

    /**
     * Getting all requests from DB
     * @param sort sort type
     * @param order order type
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @param authUser authorized user
     * @return received requests
     * @throws DBException if something went wrong while executing
     */
    List<Request> getAll(String sort, String order, int start, int total, User authUser) throws DBException;

    /**
     * Getting request from DB
     * @param requestId id of request
     * @return received request
     * @throws DBException if something went wrong while executing
     */
    Request get(int requestId) throws DBException;

    /**
     * Getting count of requests from DB
     * @param authUser authorized user
     * @return count of requests
     * @throws DBException if something went wrong while executing
     */
    int getCount(User authUser) throws DBException;

    /**
     * Creating activity for add request in DB
     * @param con connection of DB
     * @param activity entered activity
     * @throws DBException if something went wrong while executing
     */
    void createActivity(Connection con, Activity activity) throws DBException;

    /**
     * Setting categories for created activity, which is for add request, in DB
     * @param con connection of DB
     * @param categoryIds selected category ids
     * @param activityId entered activity
     * @throws DBException if something went wrong while executing
     */
    void setActivityCategories(Connection con, List<Integer> categoryIds, int activityId) throws DBException;

    /**
     * Getting category ids for activity in request from DB
     * @param con connection of DB
     * @param activityId id of activity
     * @return received category ids
     * @throws DBException if something went wrong while executing
     */
    List<Integer> withCategories(Connection con, int activityId) throws DBException;

    /**
     * Confirmation of add request in DB
     * @param requestId id of request
     * @param activityId id of activity
     * @param userId id of creator
     * @throws DBException if something went wrong while executing
     */
    void confirmAdd(int requestId, int activityId, int userId) throws DBException;

    /**
     * Deleting request from DB
     * @param requestId id of request
     * @param authUser authorized user
     * @throws DBException if something went wrong while executing
     */
    void delete(int requestId, User authUser) throws DBException;

}
