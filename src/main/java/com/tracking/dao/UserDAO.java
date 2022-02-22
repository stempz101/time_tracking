package com.tracking.dao;

import com.tracking.models.UserActivity;
import com.tracking.models.User;

import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

    User auth(HttpSession session, String email, String password) throws SQLException;

    boolean checkEmail(String email) throws SQLException;

    boolean checkPassword(User user, String password);

    User create(HttpSession session, String lastName, String firstName, String email, String password) throws SQLException;

    boolean ifExists(HttpSession session, String email) throws SQLException;

    List<User> getAll(int start, int total) throws SQLException;

    List<UserActivity> getAllWhereInActivity(int activityId, int start, int total) throws SQLException;

    List<User> getAllWhereNotInActivity(int activityId) throws SQLException;

    UserActivity getWhereInActivity(User user, int activityId) throws SQLException;

    List<User> getAllOrder(String sort, String order, int start, int total) throws SQLException;

    List<User> getAllWhereName(String lastName, String firstName, int start, int total) throws SQLException;

    List<User> getAllWhereNameOrder(String lastName, String firstName, String sort, String order,
                                    int start, int total) throws SQLException;

    User getById(int userId) throws SQLException;

    User getByEmail(String email) throws SQLException;

    User getUser(PreparedStatement prst) throws SQLException;

    int getCount() throws SQLException;

    int getCountWhereName(String lastName, String firstName) throws SQLException;

    void setAdmin(int userId, boolean isAdmin) throws SQLException;

    void setBlock(int userId, boolean isBlocked) throws SQLException;

    void startTime(int activityId, int userId) throws SQLException;

    void stopTime(int activityId, int userId) throws SQLException;

    boolean update(User user);

    boolean delete(User user);

    static boolean validateLogin(HttpSession session, String email, String password) {
        int errorCount = 0;
        if (email == null || email.isEmpty()) {
            session.setAttribute("emailError", "Email cannot be empty");
            errorCount++;
        } else if (!email.matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            session.setAttribute("emailError", "Email input is invalid");
            errorCount++;
        } else {
            session.removeAttribute("emailError");
        }
        if (password == null || password.isEmpty()) {
            session.setAttribute("passwordError", "Password cannot be empty");
            errorCount++;
        } else {
            session.removeAttribute("passwordError");
        }

        return errorCount == 0;
    }

    static boolean validateRegistration(HttpSession session, String lastName, String firstName, String email, String password) {
        int errorCount = 0;
        if (lastName == null || lastName.isEmpty()) {
            session.setAttribute("lastNameError", "Last name cannot be empty");
            errorCount++;
        } else if (!lastName.matches("[A-Za-z'\\s]+|[а-яА-ЯЇїІіЄєҐґ'\\s]+")) {
            session.setAttribute("lastNameError", "Last name input is invalid");
            errorCount++;
        }

        if (firstName == null || firstName.isEmpty()) {
            session.setAttribute("firstNameError", "First name cannot be empty");
            errorCount++;
        } else if (!firstName.matches("[A-Za-z'\\s]+|[а-яА-ЯЇїІіЄєҐґ'\\s]+")) {
            session.setAttribute("firstNameError", "First name input is invalid");
            errorCount++;
        }

        if (email == null || email.isEmpty()) {
            session.setAttribute("emailError", "Email cannot be empty");
            errorCount++;
        } else if (!email.matches("^[\\w\\-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            session.setAttribute("emailError", "Email input is invalid");
            errorCount++;
        }

        if (password == null || password.isEmpty()) {
            session.setAttribute("passwordError", "Password cannot be empty");
            errorCount++;
        } else if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            session.setAttribute("passwordError", "Password input is invalid");
            errorCount++;
        }

        return errorCount == 0;
    }
}
