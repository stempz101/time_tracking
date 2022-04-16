package com.tracking.dao.mysql;

import com.tracking.controllers.services.Service;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.controllers.exceptions.DBException;
import com.tracking.dao.mapper.EntityMapper;
import com.tracking.models.Activity;
import com.tracking.models.UserActivity;
import com.tracking.models.User;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static com.tracking.dao.mysql.MysqlConstants.*;

/**
 * MySQL User DAO. Here is realized all methods of User DAO for MySQL DB
 */
public class MysqlUserDAO implements UserDAO {

    private static final Logger logger = Logger.getLogger(MysqlUserDAO.class);

    private final DAOFactory factory = MysqlDAOFactory.getInstance();

    private static UserDAO instance;

    protected static synchronized UserDAO getInstance() {
        if (instance == null)
            instance = new MysqlUserDAO();
        return instance;
    }

    private MysqlUserDAO() {

    }

    @Override
    public boolean checkEmail(String email) throws DBException {
        try {
            User dbUser = getByEmail(email);
            return dbUser != null;
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: checkEmail was failed.", e);
        }
    }

    @Override
    public boolean checkPassword(User user, String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return BCrypt.checkpw(password, user.getPassword());
    }

    @Override
    public synchronized User create(String lastName, String firstName, String email,
                       String password, String confirmPassword, String image, boolean isAdmin) throws DBException {
        User user;
        String query = isAdmin ? INSERT_ADMIN : INSERT_USER;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
            int c = 0;
            prst.setString(++c, lastName);
            prst.setString(++c, firstName);
            prst.setString(++c, email);
            prst.setString(++c, hashedPassword);
            prst.setString(++c, image);
            if (isAdmin)
                prst.setBoolean(++c, true);
            prst.execute();
            user = new User(lastName, firstName, email, hashedPassword, image);
            try (ResultSet rs = prst.getGeneratedKeys()) {
                if (rs.next())
                    user.setId(rs.getInt(1));
            }
            if (user.isAdmin())
                logger.info("Admin (id=" + user.getId() + ") registration was successful");
            else
                logger.info("User (id=" + user.getId() + ") registration was successful");
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: create was failed.", e);
        }
    }

    @Override
    public boolean ifExists(String email) throws DBException {
        try {
            return checkEmail(email);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: ifExists was failed.", e);
        }
    }

    @Override
    public List<User> getAll(String sort, String order, int start, int total) throws DBException {
        String orderBy, query;
        if (sort != null && !sort.isEmpty()) {
            orderBy = sort + " " + order;
            if (!sort.equals("last_name"))
                orderBy += ", last_name";
            query = SELECT_USERS_ORDER.replace(ORDER_BY, orderBy);
        } else if (order.equals("desc")) {
            query = SELECT_USERS_REVERSE;
        } else {
            query = SELECT_USERS;
        }

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            List<User> userList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            UserMapper mapper = new UserMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    User user = mapper.mapRow(rs);
                    userList.add(user);
                }
            }
            logger.info("Selection of users was successful");
            logger.info("Count of users: " + userList.size());
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: getAll was failed.", e);
        }
    }

    @Override
    public List<UserActivity> getAllWhereInActivity(int activityId, String sort, String order, int start, int total) throws DBException {
        String orderBy, query;
        if (sort != null && !sort.isEmpty()) {
            if (sort.equals("spent_time"))
                sort = "users_activities." + sort;
            orderBy = sort + " " + order;
            if (!sort.equals("last_name"))
                orderBy += ", last_name";
            query = SELECT_USERS_ACTIVITY_ORDER.replace(ORDER_BY, orderBy);
        } else if (order.equals("desc")) {
            query = SELECT_USERS_ACTIVITY_REVERSE;
        } else {
            query = SELECT_USERS_ACTIVITY;
        }

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            List<UserActivity> userActivityList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, activityId);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            UserActivityMapper mapper = new UserActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    UserActivity userActivity = mapper.mapRow(rs);
                    userActivityList.add(userActivity);
                }
            }
            logger.info("Selection of participating users in activity (id=" + activityId + ") was successful");
            logger.info("Count of users: " + userActivityList.size());
            return userActivityList;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: getAllWhereInActivity was failed.", e);
        }
    }

    @Override
    public List<UserActivity> getAllInActivityWhereName(int activityId, String lastName, String firstName,
                                                        String sort, String order, int start, int total) throws DBException {
        String orderBy, query;
        if (sort != null && !sort.isEmpty()) {
            if (sort.equals("spent_time"))
                sort = "users_activities." + sort;
            orderBy = sort + " " + order;
            if (!sort.equals("last_name"))
                orderBy += ", last_name";
            query = SELECT_USERS_ACTIVITY_WHERE_NAME_ORDER.replace(ORDER_BY, orderBy);
        } else if (order.equals("desc")) {
            query = SELECT_USERS_ACTIVITY_WHERE_NAME_REVERSE;
        } else {
            query = SELECT_USERS_ACTIVITY_WHERE_NAME;
        }

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            List<UserActivity> userActivityList = new ArrayList<>();
            int c = 0;
            if (lastName != null)
                prst.setString(++c, lastName + "%");
            else
                prst.setString(++c, "%");
            if (firstName != null)
                prst.setString(++c, firstName + "%");
            else
                prst.setString(++c, "%");
            prst.setInt(++c, activityId);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            UserActivityMapper mapper = new UserActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    UserActivity userActivity = mapper.mapRow(rs);
                    userActivityList.add(userActivity);
                }
            }
            logger.info("Queried selection of participating users in activity (id=" + activityId + ") was successful");
            logger.info("Count of users: " + userActivityList.size());
            return userActivityList;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: getAllInActivityWhereName was failed.", e);
        }
    }

    @Override
    public List<User> getAllWhereNotInActivity(int activityId) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USERS_ACTIVITY_WHERE_NOT_IN)) {
            List<User> userList = new ArrayList<>();
            prst.setInt(1, activityId);
            UserMapper mapper = new UserMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    User user = mapper.mapRow(rs);
                    userList.add(user);
                }
            }
            logger.info("Selection of available users was successful");
            logger.info("Count of users: " + userList.size());
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: getAllWhereNotInActivity was failed.", e);
        }
    }

    @Override
    public UserActivity getWhereInActivity(User user, int activityId) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USER_ACTIVITY)) {
            int c = 0;
            prst.setInt(++c, user.getId());
            prst.setInt(++c, activityId);
            UserActivityMapper mapper = new UserActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                if (rs.next()) {
                    UserActivity userActivity = mapper.mapRow(rs);
                    logger.info("Selection of user (id=" + user.getId() + ") information in activity (id=" + activityId + ") was successful");
                    return userActivity;
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: getWhereInActivity was failed.", e);
        }
    }

    @Override
    public List<User> getAllWhereName(String lastName, String firstName, String sort, String order, int start, int total) throws DBException {
        String orderBy, query;
        if (sort != null && !sort.isEmpty()) {
            orderBy = sort + " " + order;
            if (!sort.equals("last_name"))
                orderBy += ", last_name";
            query = SELECT_USERS_WHERE_NAME_ORDER.replace(ORDER_BY, orderBy);
        } else if (order.equals("desc")) {
            query = SELECT_USERS_WHERE_NAME_REVERSE;
        } else {
            query = SELECT_USERS_WHERE_NAME;
        }

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            List<User> userList = new ArrayList<>();
            int c = 0;
            if (lastName != null)
                prst.setString(++c, lastName + "%");
            else
                prst.setString(++c, "%");
            if (firstName != null)
                prst.setString(++c, firstName + "%");
            else
                prst.setString(++c, "%");
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            UserMapper mapper = new UserMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    User user = mapper.mapRow(rs);
                    userList.add(user);
                }
            }
            logger.info("Queried selection of users was successful");
            logger.info("Count of users: " + userList.size());
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: getAllWhereName was failed.", e);
        }
    }

    @Override
    public User getById(int userId) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USER)) {
            prst.setInt(1, userId);
            UserMapper mapper = new UserMapper();
            try (ResultSet rs = prst.executeQuery()) {
                if (rs.next()) {
                    User user = mapper.mapRow(rs);
                    logger.info("User selection by id " + user.getId() + " was successful");
                    return user;
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: getById was failed.", e);
        }
    }

    @Override
    public User getByEmail(String email) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USER_BY_EMAIL)) {
            prst.setString(1, email);
            UserMapper mapper = new UserMapper();
            try (ResultSet rs = prst.executeQuery()) {
                if (rs.next()) {
                    User user = mapper.mapRow(rs);
                    logger.info("User selection by email " + user.getEmail() + " was successful");
                    return user;
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: getByEmail was failed.", e);
        }
    }

    @Override
    public int getCount() throws DBException {
        try (Connection con = factory.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(GET_USER_COUNT)) {
            int count = 0;
            if (rs.next())
                count = rs.getInt(COL_COUNT);
            logger.info("Count selection of users was successful");
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: getCount was failed.", e);
        }
    }

    @Override
    public int getCountWhereName(String lastName, String firstName) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(GET_USER_COUNT_WHERE_NAME)) {
            int c = 0;
            if (lastName != null)
                prst.setString(++c, lastName + "%");
            else
                prst.setString(++c, "%");
            if (firstName != null)
                prst.setString(++c, firstName + "%");
            else
                prst.setString(++c, "%");
            try (ResultSet rs = prst.executeQuery()) {
                int count = 0;
                if (rs.next())
                    count = rs.getInt(COL_COUNT);
                logger.info("Count selection of queried users was successful");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: getCountWhereName was failed.", e);
        }
    }

    @Override
    public synchronized void setBlock(int userId, boolean isBlocked) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SET_USER_BLOCK)) {
            int c = 0;
            if (isBlocked)
                prst.setBoolean(++c, true);
            else
                prst.setNull(++c, Types.NULL);
            prst.setInt(++c, userId);
            prst.executeUpdate();
            logger.info("Setting of block status (value=" + isBlocked + ") for user (id=" + userId + ") was successful");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: setBlock was failed.", e);
        }
    }

    @Override
    public synchronized void startTime(int activityId, int userId) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(START_TIME)) {
            int c = 0;
            prst.setTimestamp(++c, new Timestamp(System.currentTimeMillis()));
            prst.setInt(++c, activityId);
            prst.setInt(++c, userId);
            prst.executeUpdate();
            logger.info("Start time for user (id=" + userId + ") in activity (id=" + activityId + ") was set");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: startTime was failed.", e);
        }
    }

    @Override
    public synchronized void stopTime(int activityId, int userId) throws DBException {
        Connection con = null;
        PreparedStatement prst = null;
        try {
            con = factory.getConnection();
            con.setAutoCommit(false);
            prst = con.prepareStatement(STOP_TIME);
            int c = 0;
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            prst.setTimestamp(++c, currentTime);
            prst.setTimestamp(++c, currentTime);
            prst.setInt(++c, activityId);
            prst.setInt(++c, userId);
            prst.executeUpdate();
            logger.info("Stop time for user (id=" + userId + ") in activity (id=" + activityId + ") was set. User spent time was counted and added to total in activity.");

            prst = con.prepareStatement(UPDATE_USER_SPENT_TIME);
            c = 0;
            prst.setTimestamp(++c, currentTime);
            prst.setInt(++c, activityId);
            prst.setInt(++c, userId);
            prst.setInt(++c, userId);
            prst.executeUpdate();
            logger.info("Total spent time of user (id=" + userId + ") was updated");

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            factory.rollback(con);
            throw new DBException("MysqlUserDAO: stopTime was failed.", e);
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public synchronized boolean updateProfile(int userId, String lastName, String firstName, String email) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(UPDATE_USER_PROFILE)) {
            int c = 0;
            prst.setString(++c, lastName);
            prst.setString(++c, firstName);
            prst.setString(++c, email);
            prst.setInt(++c, userId);
            prst.executeUpdate();
            logger.info("User (id=" + userId + ") information (last name, first name, email) was updated");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: updateProfile was failed.", e);
        }
    }

    @Override
    public synchronized boolean updatePhoto(int userId, String imageName) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(UPDATE_USER_PHOTO)) {
            int c = 0;
            prst.setString(++c, imageName);
            prst.setInt(++c, userId);
            prst.executeUpdate();
            logger.info("User (id=" + userId + ") photo was updated");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info(e);
            throw new DBException("MysqlUserDAO: updatePhoto was failed.", e);
        }
    }

    @Override
    public synchronized boolean updatePassword(int userId, String currentPassword, String newPassword, String confirmPassword) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(UPDATE_USER_PASSWORD)) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));
            int c = 0;
            prst.setString(++c, hashedPassword);
            prst.setInt(++c, userId);
            prst.executeUpdate();
            logger.info("User (id=" + userId + ") password was updated");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlUserDAO: updatePassword was failed.", e);
        }
    }

    @Override
    public synchronized boolean delete(User user) throws DBException {
        Connection con = null;
        PreparedStatement prst = null;
        try {
            con = factory.getConnection();
            con.setAutoCommit(false);
            prst = con.prepareStatement(SELECT_ALL_USER_ACTIVITIES);
            prst.setInt(1, user.getId());
            List<Activity> activityList = new ArrayList<>();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    activityList.add(new Activity(rs.getInt(COL_ACTIVITIES_ID), rs.getString(COL_ACTIVITIES_NAME)));
                }
            }
            prst = con.prepareStatement(DELETE_USER);
            prst.setInt(1, user.getId());
            prst.execute();
            prst = con.prepareStatement(UPDATE_ACTIVITY_PEOPLE_COUNT);
            for (Activity activity : activityList) {
                prst.setInt(1, activity.getId());
                prst.setInt(2, activity.getId());
                prst.executeUpdate();
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            factory.rollback(con);
            throw new DBException("MysqlUserDAO: delete was failed.", e);
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    private void setUserActivityStatus(UserActivity user, String status) {
        if (status.equals(UserActivity.Status.STARTED.toString()))
            user.setStatus(UserActivity.Status.STARTED);
        else if (status.equals(UserActivity.Status.STOPPED.toString()))
            user.setStatus(UserActivity.Status.STOPPED);
        else if (status.equals(UserActivity.Status.NOT_STARTED.toString()))
            user.setStatus(UserActivity.Status.NOT_STARTED);
    }

    /**
     * User Mapper. Setting received user
     */
    private static class UserMapper implements EntityMapper<User> {
        @Override
        public User mapRow(ResultSet rs) {
            try {
                User user = new User();
                user.setId(rs.getInt(COL_USERS_ID));
                user.setLastName(rs.getString(COL_USERS_LAST_NAME));
                user.setFirstName(rs.getString(COL_USERS_FIRST_NAME));
                user.setImage(rs.getString(COL_USERS_IMAGE));
                user.setEmail(rs.getString(COL_USERS_EMAIL));
                user.setPassword(rs.getString(COL_USERS_PASSWORD));
                user.setActivityCount(rs.getInt(COL_USERS_ACTIVITY_COUNT));
                user.setSpentTime(rs.getLong(COL_USERS_SPENT_TIME));
                user.setAdmin(rs.getBoolean(COL_USERS_IS_ADMIN));
                user.setBlocked(rs.getBoolean(COL_USERS_IS_BLOCKED));
                return user;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * UserActivity Mapper. Setting received user information in activity
     */
    private static class UserActivityMapper implements EntityMapper<UserActivity> {
        @Override
        public UserActivity mapRow(ResultSet rs) {
            try {
                UserActivity userActivity = new UserActivity();
                userActivity.setUserId(rs.getInt(COL_USERS_ID));
                userActivity.setUserLastName(rs.getString(COL_USERS_LAST_NAME));
                userActivity.setUserFirstName(rs.getString(COL_USERS_FIRST_NAME));
                userActivity.setUserImage(rs.getString(COL_USERS_IMAGE));
                userActivity.setAdmin(rs.getBoolean(COL_USERS_IS_ADMIN));
                if (rs.getTimestamp(COL_USE_ACT_START_TIME) != null)
                    userActivity.setStartTime(new Date(rs.getTimestamp(COL_USE_ACT_START_TIME).getTime()));
                if (rs.getTimestamp(COL_USE_ACT_STOP_TIME) != null)
                    userActivity.setStopTime(new Date(rs.getTimestamp(COL_USE_ACT_STOP_TIME).getTime()));
                userActivity.setSpentTime(rs.getLong(COL_USE_ACT_SPENT_TIME));
                userActivity.setStatus(UserActivity.Status.get(rs.getString(COL_USE_ACT_STATUS)));
                return userActivity;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
