package com.tracking.dao.mysql;

import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.dao.mapper.EntityMapper;
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
        if (!UserDAO.validateLogin(session, email, password))
            return null;
        if (!checkEmail(email)) {
            session.setAttribute("emailError", "User with this email does not exist");
            return null;
        }

        User dbUser = getByEmail(email);
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
    public User create(HttpSession session, String lastName, String firstName, String email, String password, String confirmPassword, String image) throws SQLException {

        User user;

        if (!UserDAO.validateRegistration(session, lastName, firstName, email, password, confirmPassword))
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
            prst.setString(++c, image);
            prst.execute();
            user = new User(lastName, firstName, email, hashedPassword, image);
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
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USERS)) {
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
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public List<UserActivity> getAllWhereInActivity(int activityId, int start, int total) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USERS_ACTIVITY)) {
            List<UserActivity> userActivityList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, activityId);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            UserActivityMapper mapper = new UserActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    UserActivity userActivity = mapper.mapRow(rs);
                    setUserActivityStatus(userActivity, rs.getString(COL_STATUS));
                    userActivityList.add(userActivity);
                }
            }
            return userActivityList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public List<UserActivity> getAllInActivityOrder(int activityId, String sort, String order, int start, int total) throws SQLException {
        String orderBy = sort + " " + order;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USERS_ACTIVITY_ORDER.replace(ORDER_BY, orderBy))) {
            List<UserActivity> userActivityList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, activityId);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            UserActivityMapper mapper = new UserActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    UserActivity userActivity = mapper.mapRow(rs);
                    setUserActivityStatus(userActivity, rs.getString(COL_STATUS));
                    userActivityList.add(userActivity);
                }
            }
            return userActivityList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public List<UserActivity> getAllInActivityWhereName(int activityId, String lastName, String firstName, int start, int total) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USERS_ACTIVITY_WHERE_NAME)) {
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
                    setUserActivityStatus(userActivity, rs.getString(COL_STATUS));
                    userActivityList.add(userActivity);
                }
            }
            return userActivityList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public List<UserActivity> getAllInActivityWhereNameOrder(int activityId, String lastName, String firstName, String sort, String order, int start, int total) throws SQLException {
        String orderBy = sort + " " + order;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USERS_ACTIVITY_WHERE_NAME_ORDER.replace(ORDER_BY, orderBy))) {
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
                    setUserActivityStatus(userActivity, rs.getString(COL_STATUS));
                    userActivityList.add(userActivity);
                }
            }
            return userActivityList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public List<User> getAllWhereNotInActivity(int activityId) throws SQLException {
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
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public UserActivity getWhereInActivity(User user, int activityId) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USER_ACTIVITY)) {
            int c = 0;
            prst.setInt(++c, user.getId());
            prst.setInt(++c, activityId);
            UserActivityMapper mapper = new UserActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                if (rs.next()) {
                    UserActivity userActivity = mapper.mapRow(rs);
                    setUserActivityStatus(userActivity, rs.getString(COL_STATUS));
                    return userActivity;
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public List<User> getAllOrder(String sort, String order, int start, int total) throws SQLException {
        String orderBy = sort + " " + order;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USERS_ORDER.replace(ORDER_BY, orderBy))) {
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
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public List<User> getAllWhereName(String lastName, String firstName, int start, int total) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USERS_WHERE_NAME)) {
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
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public List<User> getAllWhereNameOrder(String lastName, String firstName, String sort, String order, int start, int total) throws SQLException {
        String orderBy = sort + " " + order;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_USERS_WHERE_NAME_ORDER.replace(ORDER_BY, orderBy))) {
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
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
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
                user.setImage(rs.getString(COL_IMAGE));
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
    public int getCountWhereName(String lastName, String firstName) throws SQLException {
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
                return count;
            }
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

    private static class UserMapper implements EntityMapper<User> {
        @Override
        public User mapRow(ResultSet rs) {
            try {
                User user = new User();
                user.setId(rs.getInt(COL_ID));
                user.setLastName(rs.getString(COL_LAST_NAME));
                user.setFirstName(rs.getString(COL_FIRST_NAME));
                user.setImage(rs.getString(COL_IMAGE));
                user.setActivityCount(rs.getInt(COL_ACTIVITY_COUNT));
                user.setSpentTime(rs.getLong(COL_SPENT_TIME));
                user.setAdmin(rs.getBoolean(COL_IS_ADMIN));
                user.setBlocked(rs.getBoolean(COL_IS_BLOCKED));
                return user;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private static class UserActivityMapper implements EntityMapper<UserActivity> {
        @Override
        public UserActivity mapRow(ResultSet rs) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
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
                return userActivity;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
