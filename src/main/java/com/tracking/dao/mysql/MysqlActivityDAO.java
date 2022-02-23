package com.tracking.dao.mysql;

import com.tracking.dao.ActivityDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.mapper.EntityMapper;
import com.tracking.models.Activity;
import com.tracking.models.User;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.tracking.dao.mysql.MysqlConstants.*;

public class MysqlActivityDAO implements ActivityDAO {

    private DAOFactory factory = MysqlDAOFactory.getInstance();

    private static ActivityDAO instance;

    protected static ActivityDAO getInstance() {
        if (instance == null)
            instance = new MysqlActivityDAO();
        return instance;
    }

    private MysqlActivityDAO() {

    }

    @Override
    public boolean create(HttpSession session, Activity activity) throws SQLException {

        if (checkErrors(session, activity)) return false;

        Connection con = null;
        PreparedStatement prst = null;
        try {
            con = factory.getConnection();
            con.setAutoCommit(false);
            prst = con.prepareStatement(INSERT_ACTIVITY, Statement.RETURN_GENERATED_KEYS);
            int c = 0;
            prst.setString(++c, activity.getName());
            prst.setString(++c, activity.getDescription());
            setImageToStatement(activity.getImage(), prst, ++c);
            prst.setInt(++c, activity.getCreatorId());
            prst.setString(++c, activity.getStatus().toString());
            prst.execute();
            int activityId = getInsertedActivityId(prst);
            setActivityCategories(con, activity.getCategories(), activityId);
            session.removeAttribute("activity");
            session.setAttribute("successMessage", "Activity successfully created");
            con.commit();
            return true;
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
    public List<Activity> getAll(int peopleFrom, int peopleTo, int start, int total) throws SQLException {
        List<Activity> activityList = null;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ALL_ACTIVITIES)) {
            activityList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    activity.setCategories(withCategories(con, activity.getId()));
                    activityList.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllOrder(String sort, String order, int peopleFrom, int peopleTo, int start, int total)
            throws SQLException {
        List<Activity> activityList = null;
        String orderBy = sort + " " + order;;
        if (sort.equals("create_time") || sort.equals("people_count"))
            orderBy = sort + " " + order + ", name";
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ALL_ACTIVITIES_ORDER.replace(ORDER_BY, orderBy))) {
            activityList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    activity.setCategories(withCategories(con, activity.getId()));
                    activityList.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllLike(String searchQuery, int peopleFrom, int peopleTo, int start, int total)
            throws SQLException {
        List<Activity> activityList = null;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ACTIVITIES_LIKE)) {
            activityList = new ArrayList<>();
            int c = 0;
            prst.setString(++c, searchQuery + "%");
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    activity.setCategories(withCategories(con, activity.getId()));
                    activityList.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllLikeOrder(String searchQuery, String sort, String order, int peopleFrom, int peopleTo,
                                          int start, int total) throws SQLException {
        List<Activity> activityList = null;
        String orderBy = sort + " " + order;;
        if (sort.equals("create_time") || sort.equals("people_count"))
            orderBy = sort + " " + order + ", name";
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ACTIVITIES_LIKE_ORDER.replace(ORDER_BY, orderBy))) {
            activityList = new ArrayList<>();
            int c = 0;
            prst.setString(++c, searchQuery + "%");
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    activity.setCategories(withCategories(con, activity.getId()));
                    activityList.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllWhereCategory(List<Integer> categoryList, int peopleFrom, int peopleTo,
                                              int start, int total) throws SQLException {
        List<Activity> activityList;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(buildWhereInQuery(categoryList,
                     SELECT_ACTIVITIES_WHERE_CATEGORY))) {
            activityList = new ArrayList<>();
            int c = 0;
            for (int categoryId : categoryList)
                prst.setInt(++c, categoryId);
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, categoryList.size());
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    activity.setCategories(withCategories(con, activity.getId()));
                    activityList.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllWhereCategoryOrder(List<Integer> categoryList, String sort, String order, int peopleFrom,
                                                   int peopleTo, int start, int total) throws SQLException {
        List<Activity> activityList;
        String orderBy = sort + " " + order;;
        if (sort.equals("create_time") || sort.equals("people_count"))
            orderBy = sort + " " + order + ", name";
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(buildWhereInQuery(categoryList,
                     SELECT_ACTIVITIES_WHERE_CATEGORY_ORDER.replace(ORDER_BY, orderBy)))) {
            activityList = new ArrayList<>();
            int c = 0;
            for (int categoryId : categoryList)
                prst.setInt(++c, categoryId);
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, categoryList.size());
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    activity.setCategories(withCategories(con, activity.getId()));
                    activityList.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllWhereCategoryIsNull(int peopleFrom, int peopleTo, int start, int total) throws SQLException {
        List<Activity> activityList;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ACTIVITIES_WHERE_CATEGORY_IS_NULL)) {
            activityList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    activity.setCategories(withCategories(con, activity.getId()));
                    activityList.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllWhereCategoryIsNullOrder(String sort, String order, int peopleFrom, int peopleTo,
                                                         int start, int total) throws SQLException {
        List<Activity> activityList;
        String orderBy = sort + " " + order;;
        if (sort.equals("create_time") || sort.equals("people_count"))
            orderBy = sort + " " + order + ", name";
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ACTIVITIES_WHERE_CATEGORY_IS_NULL_ORDER
                     .replace(ORDER_BY, orderBy))) {
            activityList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    activity.setCategories(withCategories(con, activity.getId()));
                    activityList.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllLikeAndWhereCategory(String searchQuery, List<Integer> categoryList, int peopleFrom, int peopleTo,
                                                     int start, int total) throws SQLException {
        List<Activity> activityList;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(buildWhereInQuery(categoryList, SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY))) {
            activityList = new ArrayList<>();
            int c = 0;
            prst.setString(++c, searchQuery + "%");
            for (int categoryId : categoryList)
                prst.setInt(++c, categoryId);
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, categoryList.size());
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    activity.setCategories(withCategories(con, activity.getId()));
                    activityList.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllLikeAndWhereCategoryOrder(String searchQuery, List<Integer> categoryList, String sort, String order,
                                                          int peopleFrom, int peopleTo, int start, int total) throws SQLException {
        List<Activity> activityList;
        String orderBy = sort + " " + order;;
        if (sort.equals("create_time") || sort.equals("people_count"))
            orderBy = sort + " " + order + ", name";
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(buildWhereInQuery(categoryList,
                     SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_ORDER.replace(ORDER_BY, orderBy)))) {
            activityList = new ArrayList<>();
            int c = 0;
            prst.setString(++c, searchQuery + "%");
            for (int categoryId : categoryList)
                prst.setInt(++c, categoryId);
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, categoryList.size());
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    activity.setCategories(withCategories(con, activity.getId()));
                    activityList.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllLikeAndWhereCategoryIsNull(String searchQuery, int peopleFrom, int peopleTo,
                                                           int start, int total) throws SQLException {
        List<Activity> activityList;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL)) {
            activityList = new ArrayList<>();
            int c = 0;
            prst.setString(++c, searchQuery + "%");
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    activity.setCategories(withCategories(con, activity.getId()));
                    activityList.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllLikeAndWhereCategoryIsNullOrder(String searchQuery, String sort, String order,
                                                                int peopleFrom, int peopleTo, int start, int total) throws SQLException {
        List<Activity> activityList;
        String orderBy = sort + " " + order;
        if (sort.equals("create_time") || sort.equals("people_count"))
            orderBy = sort + " " + order + ", name";
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL_ORDER
                     .replace(ORDER_BY, orderBy))) {
            activityList = new ArrayList<>();
            int c = 0;
            prst.setString(++c, searchQuery + "%");
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    activity.setCategories(withCategories(con, activity.getId()));
                    activityList.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    @Override
    public int getCount(int peopleFrom, int peopleTo) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(GET_ACTIVITY_COUNT)) {
            int c = 0;
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
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
    public int getCountWhereLike(String searchQuery, int peopleFrom, int peopleTo) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(GET_ACTIVITIES_LIKE_COUNT)) {
            int c = 0;
            prst.setString(++c, searchQuery + "%");
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
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
    public int getCountWhereCategory(List<Integer> categoryList, int peopleFrom, int peopleTo) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(buildWhereInQuery(categoryList, GET_ACTIVITIES_WHERE_CATEGORY_COUNT))) {
            int c = 0;
            for (int categoryId : categoryList)
                prst.setInt(++c, categoryId);
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, categoryList.size());
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
    public int getCountWhereCategoryIsNull(int peopleFrom, int peopleTo) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(GET_ACTIVITIES_WHERE_CATEGORY_IS_NULL_COUNT)) {
            int c = 0;
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
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
    public int getCountWhereLikeAndCategory(String searchQuery, List<Integer> categoryList, int peopleFrom, int peopleTo)
            throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(buildWhereInQuery(categoryList, GET_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_COUNT))) {
            int c = 0;
            prst.setString(++c, searchQuery + "%");
            for (int categoryId : categoryList)
                prst.setInt(++c, categoryId);
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, categoryList.size());
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
    public int getCountWhereLikeAndCategoryIsNull(String searchQuery, int peopleFrom, int peopleTo) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(GET_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL_COUNT)) {
            int c = 0;
            prst.setString(++c, searchQuery + "%");
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
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
    public int getPeopleCount(int activityId) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(GET_ACTIVITY_PEOPLE_COUNT)) {
            prst.setInt(1, activityId);
            try (ResultSet rs = prst.executeQuery()) {
                int count = 0;
                if (rs.next()) {
                    count = rs.getInt(COL_PEOPLE_COUNT);
                }
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public int getMaxPeopleCount() throws SQLException {
        try (Connection con = factory.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ACTIVITIES_MAX_PEOPLE_COUNT)) {
            int count = 0;
            if (rs.next())
                count = rs.getInt(COL_PEOPLE_COUNT);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public User getCreator(int activityId) throws SQLException {
        User creator = null;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(GET_ACTIVITY_CREATOR)) {
            prst.setInt(1, activityId);
            try (ResultSet rs = prst.executeQuery()) {
                if (rs.next()) {
                    creator = new User();
                    creator.setId(rs.getInt(COL_ID));
                    creator.setLastName(rs.getString(COL_LAST_NAME));
                    creator.setFirstName(rs.getString(COL_FIRST_NAME));
                    creator.setImage(rs.getString(COL_IMAGE));
                    creator.setAdmin(rs.getBoolean(COL_IS_ADMIN));
                    creator.setBlocked(rs.getBoolean(COL_IS_BLOCKED));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return creator;
    }

    @Override
    public Activity getById(int id) throws SQLException {
        Activity activity = null;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ACTIVITY)) {
            prst.setInt(1, id);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                if (rs.next()) {
                    activity = mapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activity;
    }

    @Override
    public void setActivityCategories(Connection con, List<Integer> categoryIds, int activityId) throws SQLException {
        if (categoryIds == null || categoryIds.isEmpty())
            return;
        try (PreparedStatement prst = con.prepareStatement(INSERT_ACTIVITY_CATEGORIES)) {
            for (int categoryId : categoryIds) {
                int c = 0;
                prst.setInt(++c, activityId);
                prst.setInt(++c, categoryId);
                prst.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public void updateActivityCategories(Connection con, List<Integer> categoryIds, int activityId) throws SQLException {
        try (PreparedStatement prst = con.prepareStatement(DELETE_ACTIVITY_CATEGORIES)) {
            prst.setInt(1, activityId);
            prst.execute();
            setActivityCategories(con, categoryIds, activityId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public void addUser(int activityId, int userId) throws SQLException {
        Connection con = null;
        PreparedStatement prst = null;
        try {
            con = factory.getConnection();
            con.setAutoCommit(false);
            prst = con.prepareStatement(INSERT_USER_ACTIVITY);
            int c = 0;
            prst.setInt(++c, userId);
            prst.setInt(++c, activityId);
            prst.execute();
            prst = con.prepareStatement(UPDATE_ACTIVITY_PEOPLE_COUNT);
            c = 0;
            prst.setInt(++c, activityId);
            prst.setInt(++c, activityId);
            prst.executeUpdate();
            prst = con.prepareStatement(UPDATE_USER_ACTIVITY_COUNT);
            c = 0;
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
    public void deleteUser(int activityId, int userId) throws SQLException {
        Connection con = null;
        PreparedStatement prst = null;
        try {
            con = factory.getConnection();
            con.setAutoCommit(false);
            prst = con.prepareStatement(DELETE_USER_ACTIVITY);
            int c = 0;
            prst.setInt(++c, userId);
            prst.setInt(++c, activityId);
            prst.execute();
            prst = con.prepareStatement(UPDATE_ACTIVITY_PEOPLE_COUNT);
            c = 0;
            prst.setInt(++c, activityId);
            prst.setInt(++c, activityId);
            prst.executeUpdate();
            prst = con.prepareStatement(UPDATE_USER_ACTIVITY_COUNT);
            c = 0;
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
    public boolean update(HttpSession session, Activity activity) throws SQLException {
        if (checkErrors(session, activity)) return false;

        Connection con = null;
        PreparedStatement prst = null;
        try {
            con = factory.getConnection();
            con.setAutoCommit(false);
            prst = con.prepareStatement(UPDATE_ACTIVITY);
            int c = 0;
            prst.setString(++c, activity.getName());
            prst.setString(++c, activity.getDescription());
            setImageToStatement(activity.getImage(), prst, ++c);
            prst.setInt(++c, activity.getId());
            prst.executeUpdate();
            updateActivityCategories(con, activity.getCategories(), activity.getId());
            session.removeAttribute("activity");
            session.setAttribute("successMessage", "Activity (id = " + activity.getId() + ") successfully updated");
            con.commit();
            return true;
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
    public void delete(HttpSession session, int activityId) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(DELETE_ACTIVITY)) {
            prst.setInt(1, activityId);
            prst.execute();
            session.setAttribute("successMessage", "Activity (id = " + activityId + ") successfully deleted");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    private String buildWhereInQuery(List<Integer> categoryList, String mysqlQuery) {
        StringBuilder params = new StringBuilder();
        for (int ignored : categoryList) {
            params.append("?");
            params.append(", ");
        }
        params.delete(params.length() - 2, params.length());
        return mysqlQuery.replace(IN, params.toString());
    }

    private List<Integer> withCategories(Connection con, int activityId) throws SQLException {
        List<Integer> categoryList;
        try (PreparedStatement prst = con.prepareStatement(SELECT_ACTIVITY_CATEGORIES)) {
            categoryList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, activityId);
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next())
                    categoryList.add(rs.getInt(COL_CATEGORY_ID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return categoryList;
    }

    private void setImageToStatement(String image, PreparedStatement prst, int c) throws SQLException {
        if (image == null || image.isEmpty())
            prst.setNull(c, Types.NULL);
        else
            prst.setString(c, image);
    }

    private int getInsertedActivityId(PreparedStatement prst) throws SQLException {
        int activityId = 0;
        try (ResultSet rs = prst.getGeneratedKeys()) {
            if (rs.next())
                activityId = rs.getInt(1);
        }
        return activityId;
    }

    private boolean checkErrors(HttpSession session, Activity activity) {
        return !ActivityDAO.validate(session, activity);
    }

    private static class ActivityMapper implements EntityMapper<Activity> {
        @Override
        public Activity mapRow(ResultSet rs) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd, HH:mm");
                Activity activity = new Activity();
                activity.setId(rs.getInt(COL_ID));
                activity.setName(rs.getString(COL_NAME));
                activity.setDescription(rs.getString(COL_DESCRIPTION));
                activity.setImage(rs.getString(COL_IMAGE));
                activity.setPeopleCount(rs.getInt(COL_PEOPLE_COUNT));
                activity.setCreatorId(rs.getInt(COL_CREATOR_ID));
                activity.setCreateTime(sdf.format(rs.getTimestamp(COL_CREATE_TIME)));
                return activity;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
