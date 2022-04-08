package com.tracking.dao;

import com.tracking.controllers.services.Service;
import com.tracking.controllers.exceptions.DBException;
import com.tracking.models.UserActivity;
import com.tracking.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User DAO interface. Contains all essential methods for work with users from DB
 */
public interface UserDAO {

    boolean IS_ADMIN = true;
    boolean IS_USER = false;

    /**
     * Checking email for existing in DB
     * @param email entered email
     * @return true if email exists
     * @throws DBException if something went wrong while executing
     */
    boolean checkEmail(String email) throws DBException;

    /**
     * Checking password for correctness in DB
     * @param user user for checking password
     * @param password entered password
     * @return true if password is correct
     */
    boolean checkPassword(User user, String password);

    /**
     * Registration (creating) user in DB
     * @param lastName entered last name
     * @param firstName entered first name
     * @param email entered email
     * @param password entered password
     * @param confirmPassword entered confirmation of password
     * @param image filename of selected image
     * @param isAdmin true if it's registration of admin
     * @return registered user
     * @throws DBException if something went wrong while executing
     */
    User create(String lastName, String firstName, String email, String password, String confirmPassword, String image, boolean isAdmin) throws DBException;

    /**
     * Checking entered user for existing by email in DB
     * @param email entered email
     * @return true if user with this email is exists
     * @throws DBException if something went wrong while executing
     */
    boolean ifExists(String email) throws DBException;

    /**
     * Getting all users from DB
     * @param sort sort type
     * @param order order type
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @return received users
     * @throws DBException if something went wrong while executing
     */
    List<User> getAll(String sort, String order, int start, int total) throws DBException;

    /**
     * Getting all users who are in activity from DB
     * @param activityId id of activity
     * @param sort sort type
     * @param order order type
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @return received users with their information in activity
     * @throws DBException if something went wrong while executing
     */
    List<UserActivity> getAllWhereInActivity(int activityId, String sort, String order, int start, int total) throws DBException;

    /**
     * Getting all users by name in activity from DB
     * @param activityId id of activity
     * @param lastName entered last name
     * @param firstName entered first name
     * @param sort sort type
     * @param order order type
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @return received users with their information in activity
     * @throws DBException if something went wrong while executing
     */
    List<UserActivity> getAllInActivityWhereName(int activityId, String lastName, String firstName,
                                                 String sort, String order, int start, int total) throws DBException;

    /**
     * Getting all users who are not in activity from DB
     * @param activityId id of activity
     * @return received users
     * @throws DBException if something went wrong while executing
     */
    List<User> getAllWhereNotInActivity(int activityId) throws DBException;

    /**
     * Getting user information in activity from DB
     * @param user participating user
     * @param activityId id of activity
     * @return user information in activity
     * @throws DBException if something went wrong while executing
     */
    UserActivity getWhereInActivity(User user, int activityId) throws DBException;

    /**
     * Getting all users by name from DB
     * @param lastName entered last name
     * @param firstName entered first name
     * @param sort sort type
     * @param order order type
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @return received users
     * @throws DBException if something went wrong while executing
     */
    List<User> getAllWhereName(String lastName, String firstName, String sort, String order, int start, int total) throws DBException;

    /**
     * Getting user by id from DB
     * @param userId id of user
     * @return received user
     * @throws DBException if something went wrong while executing
     */
    User getById(int userId) throws DBException;

    /**
     * Getting user by email from DB
     * @param email user email
     * @return received user
     * @throws DBException if something went wrong while executing
     */
    User getByEmail(String email) throws DBException;

    /**
     * Getting count of users in DB
     * @return count of users
     * @throws DBException if something went wrong while executing
     */
    int getCount() throws DBException;

    /**
     * Getting count of users by name from DB
     * @param lastName entered last name
     * @param firstName entered first name
     * @return count of users
     * @throws DBException if something went wrong while executing
     */
    int getCountWhereName(String lastName, String firstName) throws DBException;

    /**
     * Setting block status for user in DB
     * @param userId id of user
     * @param isBlocked true if for blocking, false if for unblocking
     * @throws DBException if something went wrong while executing
     */
    void setBlock(int userId, boolean isBlocked) throws DBException;

    /**
     * Saving start time of the beginning of activity in DB
     * @param activityId id of activity
     * @param userId id of user
     * @throws DBException if something went wrong while executing
     */
    void startTime(int activityId, int userId) throws DBException;

    /**
     * Saving stopping time of the active activity in DB.
     * Calculating difference between stop and start time for receiving spent time,
     * which is added to the accrued time earlier in DB
     * @param activityId id of activity
     * @param userId id of user
     * @throws DBException if something went wrong while executing
     */
    void stopTime(int activityId, int userId) throws DBException;

    /**
     * Updating user information (last name, first name, email) in DB
     * @param userId id of user
     * @param lastName entered last name
     * @param firstName entered first name
     * @param email entered email
     * @return true if user is updated, false if user didn't go through validation
     * @throws DBException if something went wrong while executing
     */
    boolean updateProfile(int userId, String lastName, String firstName, String email) throws DBException;

    /**
     * Updating user photo in DB
     * @param userId id of user
     * @param imageName filename of new photo
     * @return true if user photo is updated
     * @throws DBException if something went wrong while executing
     */
    boolean updatePhoto(int userId, String imageName) throws DBException;

    /**
     * Updating user password in DB
     * @param userId id of user
     * @param currentPassword entered current password
     * @param newPassword entered new password
     * @param confirmPassword entered confirmation of new password
     * @return true if password is updated, false if current password is incorrect
     * and if password didn't go through validation
     * @throws DBException if something went wrong while executing
     */
    boolean updatePassword(int userId, String currentPassword, String newPassword, String confirmPassword) throws DBException;

    /**
     * Deleting user from DB
     * @param user selected user
     * @return true if user is deleted
     * @throws DBException if something went wrong while executing
     */
    boolean delete(User user) throws DBException;

    /**
     * Password validation
     * @param password entered password
     * @return true if validation is successful
     */
    static boolean validatePassword(String password) {
        return password != null && !password.isEmpty() && password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

    /**
     * Checking confirmation of entered password
     * @param password entered password
     * @param confirmPassword entered confirmation of password
     * @return true if password confirmed
     */
    static boolean confirmPassword(String password, String confirmPassword) {
        return confirmPassword != null && !confirmPassword.isEmpty() && confirmPassword.equals(password);
    }

    /**
     * Name validation
     * @param name entered name
     * @return true if validation was successful
     */
    static boolean validateName(String name) {
        return name != null && !name.isEmpty() && name.matches("[A-Za-z'\\s]+|[а-яА-ЯЇїІіЄєҐґ'\\s]+");
    }

    /**
     * Email validation
     * @param email entered email
     * @return true if validation is successful
     */
    static boolean validateEmail(String email) {
        return email != null && !email.isEmpty() && email.matches("^[\\w\\-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
