package com.tracking.dao.mysql;

import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.models.UserActivity;
import com.tracking.models.User;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.tracking.dao.mysql.MysqlConstants.*;

public class MysqlUserDAO implements UserDAO {

    private DAOFactory factory = MysqlDAOFactory.getInstance();

    private static UserDAO instance;

    protected static UserDAO getInstance() {
        if (instance == null)
            instance = new MysqlUserDAO();
        return instance;
    }

    private MysqlUserDAO() {

    }

    @Override
    public User auth(HttpSession session, String email, String password) throws SQLException {
        User dbUser = getByEmail(email);
        if (!UserDAO.validateLogin(session, email, password))
            return null;
        if (!checkEmail(email)) {
            session.setAttribute("emailError", "User with this email does not exist");
            return null;
        }
        if (!checkPassword(dbUser, password)) {
            session.setAttribute("passwordError", "Wrong password entered");
            return null;
        }
        return dbUser;
    }

    @Override
    public boolean checkEmail(String email) throws SQLException {
        User dbUser = getByEmail(email);
        return dbUser != null;
    }

    @Override
    public boolean checkPassword(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }

    @Override
    public User create(HttpSession session, String lastName, String firstName, String email, String password) throws SQLException {

        User user;

        if (!UserDAO.validateRegistration(session, lastName, firstName, email, password))
            return null;

        if (ifExists(session, email)) {
            session.setAttribute("regError", "User with this email already exists");
            return null;
        }

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
            int c = 0;
            prst.setString(++c, lastName);
            prst.setString(++c, firstName);
            prst.setString(++c, email);
            prst.setString(++c, hashedPassword);
            prst.execute();
            user = new User(lastName, firstName, email, hashedPassword);
            try (ResultSet rs = prst.getGeneratedKeys()) {
                if (rs.next())
                    user.setId(rs.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public boolean ifExists(HttpSession session, String email) throws SQLException {
        return checkEmail(email);
    }

    @Override
    public List<User> getAll(int start, int total) throws SQLException {
        List<User> userList;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ALL_USERS)) {
            userList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt(COL_ID));
                    user.setLastName(rs.getString(COL_LAST_NAME));
                    user.setFirstName(rs.getString(COL_FIRST_NAME));
                    user.setImage(rs.getString(COL_IMAGE));
                    user.setActivityCount(rs.getInt(COL_ACTIVITY_COUNT));
                    user.setSpentTime(rs.getLong(COL_SPENT_TIME));
                    user.setAdmin(rs.getBoolean(COL_IS_ADMIN));
                    user.setBlocked(rs.getBoolean(COL_IS_BLOCKED));
                    userList.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return userList;
    }

    @Override
    public List<UserActivity> getAllWhereInActivity(int activityId, int start, int total) throws SQLException {
        List<UserActivity> userActivityList;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USERS_WHERE_ACTIVITY)) {
            userActivityList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
            int c = 0;
            prst.setInt(++c, activityId);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    UserActivity userActivity = new UserActivity();
                    userActivity.setUserId(rs.getInt(COL_USER_ID));
                    userActivity.setUserLastName(rs.getString(COL_LAST_NAME));
                    userActivity.setUserFirstName(rs.getString(COL_FIRST_NAME));
                    userActivity.setUserImage(rs.getString(COL_IMAGE));
                    userActivity.setAdmin(rs.getBoolean(COL_IS_ADMIN));
                    if (rs.getTimestamp(COL_START_TIME) != null)
                        userActivity.setStartTime(sdf.format(rs.getTimestamp(COL_START_TIME)));
                    if (rs.getTimestamp(COL_STOP_TIME) != null)
                        userActivity.setStopTime(sdf.format(rs.getTimestamp(COL_STOP_TIME)));
                    userActivity.setSpentTime(rs.getLong(COL_SPENT_TIME));
                    setUserActivityStatus(userActivity, rs.getString(COL_STATUS));
                    userActivityList.add(userActivity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return userActivityList;
    }

    @Override
    public List<User> getAllWhereNotInActivity(int activityId) throws SQLException {
        List<User> userList;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USERS_WHERE_NOT_IN_ACTIVITY)) {
            userList = new ArrayList<>();
            prst.setInt(1, activityId);
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt(COL_ID));
                    user.setLastName(rs.getString(COL_LAST_NAME));
                    user.setFirstName(rs.getString(COL_FIRST_NAME));
                    user.setAdmin(rs.getBoolean(COL_IS_ADMIN));
                    userList.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return userList;
    }

    @Override
    public UserActivity getWhereInActivity(User user, int activityId) throws SQLException {
        UserActivity userActivity = null;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USER_WHERE_ACTIVITY)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
            int c = 0;
            prst.setInt(++c, user.getId());
            prst.setInt(++c, activityId);
            try (ResultSet rs = prst.executeQuery()) {
                if (rs.next()) {
                    userActivity = new UserActivity();
                    userActivity.setUserId(rs.getInt(COL_USER_ID));
                    userActivity.setUserLastName(rs.getString(COL_LAST_NAME));
                    userActivity.setUserFirstName(rs.getString(COL_FIRST_NAME));
                    userActivity.setUserImage(rs.getString(COL_IMAGE));
                    if (rs.getTimestamp(COL_START_TIME) != null)
                        userActivity.setStartTime(sdf.format(rs.getTimestamp(COL_START_TIME)));
                    if (rs.getTimestamp(COL_STOP_TIME) != null)
                        userActivity.setStopTime(sdf.format(rs.getTimestamp(COL_STOP_TIME)));
                    userActivity.setSpentTime(rs.getLong(COL_SPENT_TIME));
                    setUserActivityStatus(userActivity, rs.getString(COL_STATUS));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return userActivity;
    }

    @Override
    public User getById(int userId) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USER)) {
            prst.setInt(1, userId);
            return getUser(prst);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public User getByEmail(String email) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USER_BY_EMAIL)) {
            prst.setString(1, email);
            return getUser(prst);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public User getUser(PreparedStatement prst) throws SQLException {
        try (ResultSet rs = prst.executeQuery()) {
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(COL_ID));
                user.setLastName(rs.getString(COL_LAST_NAME));
                user.setFirstName(rs.getString(COL_FIRST_NAME));
                user.setEmail(rs.getString(COL_EMAIL));
                user.setPassword(rs.getString(COL_PASSWORD));
                if (rs.getBoolean(COL_IS_ADMIN))
                    user.setAdmin(true);
                if (rs.getBoolean(COL_IS_BLOCKED))
                    user.setBlocked(true);
                return user;
            }
        }
        return null;
    }

    @Override
    public int getCount() throws SQLException {
        try (Connection con = factory.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(GET_USER_COUNT)) {
            int count = 0;
            if (rs.next())
                count = rs.getInt(COL_COUNT);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public void setAdmin(int userId, boolean isAdmin) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SET_USER_ADMIN)) {
            int c = 0;
            if (isAdmin)
                prst.setBoolean(++c, true);
            else
                prst.setNull(++c, Types.NULL);
            prst.setInt(++c, userId);
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public void setBlock(int userId, boolean isBlocked) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SET_USER_BLOCK)) {
            int c = 0;
            if (isBlocked)
                prst.setBoolean(++c, true);
            else
                prst.setNull(++c, Types.NULL);
            prst.setInt(++c, userId);
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public void startTime(int activityId, int userId) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(START_TIME)) {
            int c = 0;
            prst.setTimestamp(++c, new Timestamp(System.currentTimeMillis()));
            prst.setInt(++c, activityId);
            prst.setInt(++c, userId);
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public void stopTime(int activityId, int userId) throws SQLException {
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

            prst = con.prepareStatement(UPDATE_USER_SPENT_TIME);
            c = 0;
            prst.setTimestamp(++c, currentTime);
            prst.setInt(++c, activityId);
            prst.setInt(++c, userId);
            prst.setInt(++c, userId);
            prst.executeUpdate();

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            factory.rollback(con);
            throw new SQLException();
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean delete(User user) {
        return false;
    }

    private void setUserActivityStatus(UserActivity user, String status) {
        if (status.equals(UserActivity.Status.STARTED.toString()))
            user.setStatus(UserActivity.Status.STARTED);
        else if (status.equals(UserActivity.Status.STOPPED.toString()))
            user.setStatus(UserActivity.Status.STOPPED);
        else if (status.equals(UserActivity.Status.NOT_STARTED.toString()))
            user.setStatus(UserActivity.Status.NOT_STARTED);
    }
}
