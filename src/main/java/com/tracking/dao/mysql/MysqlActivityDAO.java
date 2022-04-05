package com.tracking.dao.mysql;

import com.tracking.controllers.services.Service;
import com.tracking.dao.ActivityDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.controllers.exceptions.DBException;
import com.tracking.dao.mapper.EntityMapper;
import com.tracking.models.Activity;
import com.tracking.models.User;
import com.tracking.models.UserActivity;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.*;
import java.util.Date;

import static com.tracking.dao.mysql.MysqlConstants.*;

/**
 * MySQL Activity DAO. Here is realized all methods of Activity DAO for MySQL DB
 */
public class MysqlActivityDAO implements ActivityDAO {

    private static final Logger logger = Logger.getLogger(MysqlActivityDAO.class);

    private final DAOFactory factory = MysqlDAOFactory.getInstance();

    private static ActivityDAO instance;

    protected static synchronized ActivityDAO getInstance() {
        if (instance == null)
            instance = new MysqlActivityDAO();
        return instance;
    }

    private MysqlActivityDAO() {

    }

    @Override
    public boolean create(HttpServletRequest req, Activity activity) throws DBException {

        if (!ActivityDAO.validateActivity(req, activity)) {
            logger.error("Validation error occurred (name, description): " + req.getSession().getAttribute("messageError"));
            return false;
        }

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
            prst.setBoolean(++c, activity.isByAdmin());
            prst.execute();
            int activityId = getInsertedActivityId(prst);
            setActivityCategories(con, activity.getCategories(), activityId);
            logger.info("Activity creating was successfull");
            req.getSession().removeAttribute("activity");
            ResourceBundle bundle = ResourceBundle.getBundle("content", Service.getLocale(req));
            req.getSession().setAttribute("successMessage", bundle.getString("message.activity_created"));
            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            factory.rollback(con);
            throw new DBException("MysqlActivityDAO: create was failed.", e);
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public List<Activity> getAll(String sort, String order, int peopleFrom, int peopleTo, int start, int total, User authUser) throws DBException {
        List<Activity> activityList = null;

        String orderBy, query;
        if (sort != null && !sort.isEmpty()) {
            orderBy = sort + " " + order;
            if (sort.equals("create_time") || sort.equals("people_count")) {
                if (sort.equals("create_time"))
                    sort = "activities." + sort;
                orderBy = sort + " " + order + ", name";
            }
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_ORDER.replace(ORDER_BY, orderBy) :
                    SELECT_USER_ACTIVITIES_ORDER.replace(ORDER_BY, orderBy);
        } else if (order.equals("desc")){
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_REVERSE : SELECT_USER_ACTIVITIES_REVERSE;
        } else {
            query = authUser.isAdmin() ? SELECT_ACTIVITIES : SELECT_USER_ACTIVITIES;
        }
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            activityList = new ArrayList<>();
            int c = 0;
            if (!authUser.isAdmin())
                prst.setInt(++c, authUser.getId());
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    if (activity.isByAdmin() || isConfirmed(con, activity)) {
                        activity.setCategories(withCategories(con, activity.getId()));
                        if (!authUser.isAdmin() && isForDelete(con, activity))
                            activity.setForDelete(true);
                        activityList.add(activity);
                    }
                }
            }
            logger.info("Selection of activities was successful");
            logger.info("Count of activities: " + activityList.size());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getAll was failed.", e);
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllLike(String searchQuery, String sort, String order, int peopleFrom, int peopleTo,
                                          int start, int total, User authUser) throws DBException {
        List<Activity> activityList = null;

        String orderBy, query;
        if (sort != null && !sort.isEmpty()) {
            orderBy = sort + " " + order;
            if (sort.equals("create_time") || sort.equals("people_count"))
                orderBy = sort + " " + order + ", name";
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_LIKE_ORDER.replace(ORDER_BY, orderBy) :
                    SELECT_USER_ACTIVITIES_LIKE_ORDER.replace(ORDER_BY, orderBy);
        } else if (order.equals("desc")){
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_LIKE_REVERSE : SELECT_USER_ACTIVITIES_LIKE_REVERSE;
        } else {
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_LIKE : SELECT_USER_ACTIVITIES_LIKE;
        }

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            activityList = new ArrayList<>();
            int c = 0;
            if (!authUser.isAdmin())
                prst.setInt(++c, authUser.getId());
            prst.setString(++c, searchQuery + "%");
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    if (activity.isByAdmin() || isConfirmed(con, activity)) {
                        activity.setCategories(withCategories(con, activity.getId()));
                        activityList.add(activity);
                    }
                }
            }
            logger.info("Queried selection of activities was successful");
            logger.info("Count of activities: " + activityList.size());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getAllLike was failed.", e);
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllWhereCategory(List<Integer> categoryList, String sort, String order, int peopleFrom, int peopleTo,
                                              int start, int total, User authUser) throws DBException {
        List<Activity> activityList;

        String orderBy, query;
        if (sort != null && !sort.isEmpty()) {
            orderBy = sort + " " + order;
            if (sort.equals("create_time") || sort.equals("people_count"))
                orderBy = sort + " " + order + ", name";
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_WHERE_CATEGORY_ORDER.replace(ORDER_BY, orderBy) :
                    SELECT_USER_ACTIVITIES_WHERE_CATEGORY_ORDER.replace(ORDER_BY, orderBy);
        } else if (order.equals("desc")){
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_WHERE_CATEGORY_REVERSE :
                    SELECT_USER_ACTIVITIES_WHERE_CATEGORY_REVERSE;
        } else {
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_WHERE_CATEGORY : SELECT_USER_ACTIVITIES_WHERE_CATEGORY;
        }

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(buildWhereInQuery(categoryList, query))) {
            activityList = new ArrayList<>();
            int c = 0;
            if (!authUser.isAdmin())
                prst.setInt(++c, authUser.getId());
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
                    if (activity.isByAdmin() || isConfirmed(con, activity)) {
                        activity.setCategories(withCategories(con, activity.getId()));
                        activityList.add(activity);
                    }
                }
            }
            logger.info("Filtered selection of activities was successful");
            logger.info("Count of activities: " + activityList.size());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getAllWhereCategory was failed.", e);
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllWhereCategoryIsNull(String sort, String order, int peopleFrom, int peopleTo,
                                                    int start, int total, User authUser) throws DBException {
        List<Activity> activityList;

        String orderBy, query;
        if (sort != null && !sort.isEmpty()) {
            orderBy = sort + " " + order;
            if (sort.equals("create_time") || sort.equals("people_count"))
                orderBy = sort + " " + order + ", name";
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_WHERE_CATEGORY_IS_NULL_ORDER.replace(ORDER_BY, orderBy) :
                    SELECT_USER_ACTIVITIES_WHERE_CATEGORY_IS_NULL_ORDER.replace(ORDER_BY, orderBy);
        } else if (order.equals("desc")){
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_WHERE_CATEGORY_IS_NULL_REVERSE :
                    SELECT_USER_ACTIVITIES_WHERE_CATEGORY_IS_NULL_REVERSE;
        } else {
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_WHERE_CATEGORY_IS_NULL : SELECT_USER_ACTIVITIES_WHERE_CATEGORY_IS_NULL;
        }

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            activityList = new ArrayList<>();
            int c = 0;
            if (!authUser.isAdmin())
                prst.setInt(++c, authUser.getId());
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    if (activity.isByAdmin() || isConfirmed(con, activity)) {
                        activity.setCategories(withCategories(con, activity.getId()));
                        activityList.add(activity);
                    }
                }
            }
            logger.info("Filtered selection of activities was successful");
            logger.info("Count of activities: " + activityList.size());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getAllWhereCategoryIsNull was failed.", e);
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllLikeAndWhereCategory(String searchQuery, List<Integer> categoryList, String sort,
                                                     String order, int peopleFrom, int peopleTo,
                                                     int start, int total, User authUser) throws DBException {
        List<Activity> activityList;

        String orderBy, query;
        if (sort != null && !sort.isEmpty()) {
            orderBy = sort + " " + order;
            if (sort.equals("create_time") || sort.equals("people_count"))
                orderBy = sort + " " + order + ", name";
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_ORDER.replace(ORDER_BY, orderBy) :
                    SELECT_USER_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_ORDER.replace(ORDER_BY, orderBy);
        } else if (order.equals("desc")){
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_REVERSE :
                    SELECT_USER_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_REVERSE;
        } else {
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY : SELECT_USER_ACTIVITIES_LIKE_AND_WHERE_CATEGORY;
        }

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(buildWhereInQuery(categoryList, query))) {
            activityList = new ArrayList<>();
            int c = 0;
            if (!authUser.isAdmin())
                prst.setInt(++c, authUser.getId());
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
                    if (activity.isByAdmin() || isConfirmed(con, activity)) {
                        activity.setCategories(withCategories(con, activity.getId()));
                        activityList.add(activity);
                    }
                }
            }
            logger.info("Queried and filtered selection of activities was successful");
            logger.info("Count of activities: " + activityList.size());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getAllLikeAndWhereCategory was failed.", e);
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllLikeAndWhereCategoryIsNull(String searchQuery, String sort, String order,
                                                           int peopleFrom, int peopleTo, int start,
                                                           int total, User authUser) throws DBException {
        List<Activity> activityList;

        String orderBy, query;
        if (sort != null && !sort.isEmpty()) {
            orderBy = sort + " " + order;
            if (sort.equals("create_time") || sort.equals("people_count"))
                orderBy = sort + " " + order + ", name";
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL_ORDER.replace(ORDER_BY, orderBy) :
                    SELECT_USER_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL_ORDER.replace(ORDER_BY, orderBy);
        } else if (order.equals("desc")){
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL_REVERSE :
                    SELECT_USER_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL_REVERSE;
        } else {
            query = authUser.isAdmin() ? SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL : SELECT_USER_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL;
        }

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            activityList = new ArrayList<>();
            int c = 0;
            if (!authUser.isAdmin())
                prst.setInt(++c, authUser.getId());
            prst.setString(++c, searchQuery + "%");
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    if (activity.isByAdmin() || isConfirmed(con, activity)) {
                        activity.setCategories(withCategories(con, activity.getId()));
                        activityList.add(activity);
                    }
                }
            }
            logger.info("Queried and filtered selection of activities was successful");
            logger.info("Count of activities: " + activityList.size());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getAllLikeAndWhereCategoryIsNull was failed.", e);
        }
        return activityList;
    }

    @Override
    public List<Activity> getAllCreated(int creatorId, int start, int total) throws DBException {
        List<Activity> activityList;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ACTIVITIES_BY_ADMIN)) {
            activityList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, creatorId);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Activity activity = mapper.mapRow(rs);
                    activityList.add(activity);
                }
            }
            logger.info("Selection of created activities by admin (id=" + creatorId + ") was successful");
            logger.info("Count of activities: " + activityList.size());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getAllCreated was failed.", e);
        }
        return activityList;
    }

    @Override
    public List<UserActivity> getAllForProfile(int userId, int start, int total) throws DBException {
        List<UserActivity> activityList;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ACTIVITIES_FOR_PROFILE)){
            activityList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, userId);
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            UserActivityMapper mapper = new UserActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    UserActivity activity = mapper.mapRow(rs);
                    setUserActivityStatus(activity, rs.getString(COL_STATUS));
                    activityList.add(activity);
                }
            }
            logger.info("Selection of created activities by user (id=" + userId + ") was successful");
            logger.info("Count of activities: " + activityList.size());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getAllForProfile was failed.", e);
        }
        return activityList;
    }

    @Override
    public int getCount(int peopleFrom, int peopleTo, User authUser) throws DBException {
        String query = authUser.isAdmin() ? GET_ACTIVITY_COUNT : GET_USER_ACTIVITY_COUNT;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            int c = 0;
            if (!authUser.isAdmin())
                prst.setInt(++c, authUser.getId());
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            try (ResultSet rs = prst.executeQuery()) {
                int count = 0;
                if (rs.next())
                    count = rs.getInt(COL_COUNT);
                logger.info("Count selection of activities was successful");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getCount was failed.", e);
        }
    }

    @Override
    public int getCountWhereLike(String searchQuery, int peopleFrom, int peopleTo, User authUser) throws DBException {
        String query = authUser.isAdmin() ? GET_ACTIVITIES_LIKE_COUNT : GET_USER_ACTIVITIES_LIKE_COUNT;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            int c = 0;
            if (!authUser.isAdmin())
                prst.setInt(++c, authUser.getId());
            prst.setString(++c, searchQuery + "%");
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            try (ResultSet rs = prst.executeQuery()) {
                int count = 0;
                if (rs.next())
                    count = rs.getInt(COL_COUNT);
                logger.info("Count selection of queried activities was successful");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getCountWhereLike was failed.", e);
        }
    }

    @Override
    public int getCountWhereCategory(List<Integer> categoryList, int peopleFrom, int peopleTo, User authUser) throws DBException {
        String query = authUser.isAdmin() ? GET_ACTIVITIES_WHERE_CATEGORY_COUNT : GET_USER_ACTIVITIES_WHERE_CATEGORY_COUNT;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(buildWhereInQuery(categoryList, query))) {
            int c = 0;
            if (!authUser.isAdmin())
                prst.setInt(++c, authUser.getId());
            for (int categoryId : categoryList)
                prst.setInt(++c, categoryId);
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            prst.setInt(++c, categoryList.size());
            try (ResultSet rs = prst.executeQuery()) {
                int count = 0;
                if (rs.next())
                    count = rs.getInt(COL_COUNT);
                logger.info("Count selection of filtered activities was successful");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getCountWhereCategory was failed.", e);
        }
    }

    @Override
    public int getCountWhereCategoryIsNull(int peopleFrom, int peopleTo, User authUser) throws DBException {
        String query = authUser.isAdmin() ? GET_ACTIVITIES_WHERE_CATEGORY_IS_NULL_COUNT :
                GET_USER_ACTIVITIES_WHERE_CATEGORY_IS_NULL_COUNT;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            int c = 0;
            if (!authUser.isAdmin())
                prst.setInt(++c, authUser.getId());
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            try (ResultSet rs = prst.executeQuery()) {
                int count = 0;
                if (rs.next())
                    count = rs.getInt(COL_COUNT);
                logger.info("Count selection of filtered activities was successful");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getCountWhereCategoryIsNull was failed.", e);
        }
    }

    @Override
    public int getCountWhereLikeAndCategory(String searchQuery, List<Integer> categoryList, int peopleFrom, int peopleTo, User authUser) throws DBException {
        String query = authUser.isAdmin() ? GET_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_COUNT :
                GET_USER_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_COUNT;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(buildWhereInQuery(categoryList, query))) {
            int c = 0;
            if (!authUser.isAdmin())
                prst.setInt(++c, authUser.getId());
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
                logger.info("Count selection of queried and filtered activities was successful");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getCountWhereLikeAndCategory was failed.", e);
        }
    }

    @Override
    public int getCountWhereLikeAndCategoryIsNull(String searchQuery, int peopleFrom, int peopleTo, User authUser) throws DBException {
        String query = authUser.isAdmin() ? GET_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL_COUNT :
                GET_USER_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL_COUNT;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            int c = 0;
            if (!authUser.isAdmin())
                prst.setInt(++c, authUser.getId());
            prst.setString(++c, searchQuery + "%");
            prst.setInt(++c, peopleFrom);
            prst.setInt(++c, peopleTo);
            try (ResultSet rs = prst.executeQuery()) {
                int count = 0;
                if (rs.next())
                    count = rs.getInt(COL_COUNT);
                logger.info("Count selection of queried and filtered activities was successful");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getCountWhereLikeAndCategoryIsNull was failed.", e);
        }
    }

    @Override
    public int getCountInActivity(int activityId) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(GET_USERS_ACTIVITY_COUNT)) {
            prst.setInt(1, activityId);
            try (ResultSet rs = prst.executeQuery()) {
                int count = 0;
                if (rs.next())
                    count = rs.getInt(COL_COUNT);
                logger.info("Count selection of participating users in activity was successful");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getCountInActivity was failed.", e);
        }
    }

    @Override
    public int getCountInActivityWhereName(int activityId, String lastName, String firstName) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(GET_USERS_ACTIVITY_COUNT_WHERE_NAME)) {
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
            try (ResultSet rs = prst.executeQuery()) {
                int count = 0;
                if (rs.next())
                    count = rs.getInt(COL_COUNT);
                logger.info("Count selection of queried participating users in activity was successful");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getCountInActivityWhereName was failed.", e);
        }
    }

    @Override
    public int getCreatedCount(int creatorId) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(GET_ACTIVITIES_BY_ADMIN_COUNT)) {
            int c = 0;
            prst.setInt(++c, creatorId);
            try (ResultSet rs = prst.executeQuery()) {
                int count = 0;
                if (rs.next())
                    count = rs.getInt(COL_COUNT);
                logger.info("Count selection of created activities by admin was successful");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getCreatedCount was failed.", e);
        }
    }

    @Override
    public int getCountForProfile(int userId) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(GET_ACTIVITIES_FOR_PROFILE_COUNT)) {
            int c = 0;
            prst.setInt(++c, userId);
            try (ResultSet rs = prst.executeQuery()) {
                int count = 0;
                if (rs.next())
                    count = rs.getInt(COL_COUNT);
                logger.info("Count selection of user activities was successful");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getCountForProfile was failed.", e);
        }
    }

    @Override
    public int getMaxPeopleCount() throws DBException {
        try (Connection con = factory.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ACTIVITIES_MAX_PEOPLE_COUNT)) {
            int count = 0;
            if (rs.next())
                count = rs.getInt(COL_PEOPLE_COUNT);
            logger.info("Determination of max people count among the activities was successful");
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getMaxPeopleCount was failed.", e);
        }
    }

    @Override
    public User getCreator(int activityId) throws DBException {
        User creator = null;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ACTIVITY_CREATOR)) {
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
            logger.info("Selection of activity (id=" + activityId + ") creator was successful");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getCreator was failed.", e);
        }
        return creator;
    }

    @Override
    public Activity getById(int id) throws DBException {
        Activity activity = null;
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_ACTIVITY)) {
            prst.setInt(1, id);
            ActivityMapper mapper = new ActivityMapper();
            try (ResultSet rs = prst.executeQuery()) {
                if (rs.next()) {
                    activity = mapper.mapRow(rs);
                    activity.setCategories(withCategories(con, activity.getId()));
                    logger.info("Selection of activity by id was successful");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: getById was failed.", e);
        }
        return activity;
    }

    @Override
    public List<Integer> withCategories(Connection con, int activityId) throws DBException {
        List<Integer> categoryList;
        try (PreparedStatement prst = con.prepareStatement(SELECT_ACTIVITY_CATEGORIES)) {
            categoryList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, activityId);
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next())
                    categoryList.add(rs.getInt(COL_CATEGORY_ID));
            }
            logger.info("Id selection of activity (id=" + activityId + ") categories was successful");
            logger.info("Count of ids: " + categoryList.size());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: withCategories was failed.", e);
        }
        return categoryList;
    }

    @Override
    public void setActivityCategories(Connection con, List<Integer> categoryIds, int activityId) throws DBException {
        if (categoryIds == null || categoryIds.isEmpty())
            return;
        try (PreparedStatement prst = con.prepareStatement(INSERT_ACTIVITY_CATEGORIES)) {
            for (int categoryId : categoryIds) {
                int c = 0;
                prst.setInt(++c, activityId);
                prst.setInt(++c, categoryId);
                prst.execute();
                logger.info("Category (id=" + categoryId + ") was set for activity (id=" + activityId + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: setActivityCategories was failed.", e);
        }
    }

    @Override
    public void updateActivityCategories(Connection con, List<Integer> categoryIds, int activityId) throws DBException {
        try (PreparedStatement prst = con.prepareStatement(DELETE_ACTIVITY_CATEGORIES)) {
            prst.setInt(1, activityId);
            prst.execute();
            logger.info("Activity (id=" + activityId + ") categories was removed");
            setActivityCategories(con, categoryIds, activityId);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: updateActivityCategories was failed.", e);
        }
    }

    @Override
    public void addUser(int activityId, int userId) throws DBException {
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
            logger.info("User (id=" + userId + ") was added to activity (id=" + activityId + ") successfully");
            prst = con.prepareStatement(UPDATE_ACTIVITY_PEOPLE_COUNT);
            c = 0;
            prst.setInt(++c, activityId);
            prst.setInt(++c, activityId);
            prst.executeUpdate();
            logger.info("Activity (id=" + activityId + ") people count was updated");
            prst = con.prepareStatement(UPDATE_USER_ACTIVITY_COUNT);
            c = 0;
            prst.setInt(++c, userId);
            prst.setInt(++c, userId);
            prst.executeUpdate();
            logger.info("User (id=" + userId + ") activity count was updated");
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            factory.rollback(con);
            throw new DBException("MysqlActivityDAO: addUser was failed.", e);
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public void deleteUser(int activityId, int userId) throws DBException {
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
            logger.info("User (id=" + userId + ") was removed from activity (id=" + activityId + ") successfully");
            prst = con.prepareStatement(UPDATE_ACTIVITY_PEOPLE_COUNT);
            c = 0;
            prst.setInt(++c, activityId);
            prst.setInt(++c, activityId);
            prst.executeUpdate();
            logger.info("Activity (id=" + activityId + ") people count was updated");
            prst = con.prepareStatement(UPDATE_USER_ACTIVITY_COUNT);
            c = 0;
            prst.setInt(++c, userId);
            prst.setInt(++c, userId);
            prst.executeUpdate();
            logger.info("User (id=" + userId + ") activity count was updated");
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            factory.rollback(con);
            throw new DBException("MysqlActivityDAO: deleteUser was failed.", e);
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public boolean update(HttpServletRequest req, Activity activity) throws DBException {
        if (!ActivityDAO.validateActivity(req, activity)) {
            return false;
        }

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
            logger.info("Activity (id=" + activity.getId() + ") information was updated");
            updateActivityCategories(con, activity.getCategories(), activity.getId());
            req.getSession().removeAttribute("activity");
            ResourceBundle bundle = ResourceBundle.getBundle("content", Service.getLocale(req));
            req.getSession().setAttribute("successMessage", bundle.getString("message.activity_updated"));
            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            factory.rollback(con);
            throw new DBException("MysqlActivityDAO: update was failed.", e);
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public void delete(int activityId) throws DBException {
        Connection con = null;
        PreparedStatement prst = null;
        try {
            con = factory.getConnection();
            con.setAutoCommit(false);
            List<User> userList = new ArrayList<>();
            prst = con.prepareStatement(SELECT_ALL_USERS_ACTIVITY);
            int c = 0;
            prst.setInt(++c, activityId);
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt(COL_ID));
                    userList.add(user);
                }
            }
            logger.info("Selection of participating users in activity (id=" + activityId + ") was successful");
            prst = con.prepareStatement(DELETE_ACTIVITY);
            c = 0;
            prst.setInt(++c, activityId);
            prst.execute();
            logger.info("Activity (id=" + activityId + ") was successfully removed");
            prst = con.prepareStatement(UPDATE_USER_ACTIVITY_COUNT);
            for (User user : userList) {
                c = 0;
                prst.setInt(++c, user.getId());
                prst.setInt(++c, user.getId());
                prst.executeUpdate();
            }
            logger.info("Activity count of earlier participating users was updated");
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            factory.rollback(con);
            throw new DBException("MysqlActivityDAO: delete was failed.", e);
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public void deleteByCreator(int activityId, int creatorId) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(DELETE_ACTIVITY_BY_USER)) {
            int c = 0;
            prst.setInt(++c, activityId);
            prst.setInt(++c, creatorId);
            prst.execute();
            logger.info("Activity (id=" + activityId + ") was removed after cancelled request for add by user (id=" + creatorId + ")");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: deleteByCreator was failed.", e);
        }
    }

    @Override
    public boolean isConfirmed(Connection con, Activity activity) throws DBException {
        try (PreparedStatement prst = con.prepareStatement(GET_REQUEST_ADD_CONFIRMED)) {
            int c = 0;
            prst.setInt(++c, activity.getId());
            try (ResultSet rs = prst.executeQuery()) {
                logger.info("Checking if activity (id=" + activity.getId() + ") is confirmed");
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: isConfirmed was failed.", e);
        }
    }

    @Override
    public boolean isForDelete(Connection con, Activity activity) throws DBException {
        try (PreparedStatement prst = con.prepareStatement(GET_REQUEST_REMOVE)) {
            int c = 0;
            prst.setInt(++c, activity.getId());
            try (ResultSet rs = prst.executeQuery()) {
                logger.info("Checking if activity (id=" + activity.getId() + ") is requested for remove");
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlActivityDAO: isForDelete was failed.", e);
        }
    }

    /**
     * Building SQL query for IN selection
     * @param categoryList list of categories
     * @param mysqlQuery SQL query
     * @return built SQL query
     */
    private String buildWhereInQuery(List<Integer> categoryList, String mysqlQuery) {
        StringBuilder params = new StringBuilder();
        for (int ignored : categoryList) {
            params.append("?");
            params.append(", ");
        }
        params.delete(params.length() - 2, params.length());
        return mysqlQuery.replace(IN, params.toString());
    }

    /**
     * Setting image to prepared statement
     * @param image name of image
     * @param prst prepared statement
     * @param c count of entries to statement
     * @throws SQLException if something went wrong while setting element
     */
    private void setImageToStatement(String image, PreparedStatement prst, int c) throws SQLException {
        if (image == null || image.isEmpty())
            prst.setNull(c, Types.NULL);
        else
            prst.setString(c, image);
    }

    /**
     * Getting id of inserted activity
     * @param prst prepared statement
     * @return id of inserted activity
     * @throws SQLException if something went wrong while getting element
     */
    private int getInsertedActivityId(PreparedStatement prst) throws SQLException {
        int activityId = 0;
        try (ResultSet rs = prst.getGeneratedKeys()) {
            if (rs.next())
                activityId = rs.getInt(1);
        }
        return activityId;
    }

    /**
     * Activity Mapper. Setting received activity
     */
    private static class ActivityMapper implements EntityMapper<Activity> {
        @Override
        public Activity mapRow(ResultSet rs) {
            try {
                Activity activity = new Activity();
                activity.setId(rs.getInt(COL_ID));
                activity.setName(rs.getString(COL_NAME));
                activity.setDescription(rs.getString(COL_DESCRIPTION));
                activity.setImage(rs.getString(COL_IMAGE));
                activity.setPeopleCount(rs.getInt(COL_PEOPLE_COUNT));
                activity.setCreatorId(rs.getInt(COL_CREATOR_ID));
                activity.setByAdmin(rs.getBoolean(COL_BY_ADMIN));
                activity.setCreateTime(new Date(rs.getTimestamp(COL_CREATE_TIME).getTime()));
                return activity;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * Setting status of user in activity
     * @param user user information in activity
     * @param status received status
     */
    private void setUserActivityStatus(UserActivity user, String status) {
        if (status.equals(UserActivity.Status.STARTED.toString()))
            user.setStatus(UserActivity.Status.STARTED);
        else if (status.equals(UserActivity.Status.STOPPED.toString()))
            user.setStatus(UserActivity.Status.STOPPED);
        else if (status.equals(UserActivity.Status.NOT_STARTED.toString()))
            user.setStatus(UserActivity.Status.NOT_STARTED);
    }

    /**
     * UserActivity Mapper. Setting received user information to activity
     */
    private static class UserActivityMapper implements EntityMapper<UserActivity> {
        @Override
        public UserActivity mapRow(ResultSet rs) {
            try {
                UserActivity userActivity = new UserActivity();
                userActivity.setActivityId(rs.getInt(COL_ACTIVITY_ID));
                userActivity.setActivityName(rs.getString(COL_NAME));
                userActivity.setSpentTime(rs.getLong(COL_SPENT_TIME));
                return userActivity;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
