package com.tracking.dao.mysql;

import com.tracking.controllers.services.Service;
import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.controllers.exceptions.DBException;
import com.tracking.dao.mapper.EntityMapper;
import com.tracking.models.Category;
import com.tracking.lang.Language;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.tracking.dao.mysql.MysqlConstants.*;

/**
 * MySQL Category DAO. Here is realized all methods of Category DAO for MySQL DB
 */
public class MysqlCategoryDAO implements CategoryDAO {

    private static final Logger logger = Logger.getLogger(MysqlCategoryDAO.class);

    private final DAOFactory factory = MysqlDAOFactory.getInstance();

    private static CategoryDAO instance;

    protected static synchronized CategoryDAO getInstance() {
        if (instance == null)
            instance = new MysqlCategoryDAO();
        return instance;
    }

    private MysqlCategoryDAO() {

    }

    @Override
    public synchronized Category create(String nameEN, String nameUA) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            int c = 0;
            prst.setString(++c, nameEN);
            prst.setString(++c, nameUA);
            prst.execute();
            Category category = new Category(nameEN, nameUA);
            try (ResultSet rs = prst.getGeneratedKeys()) {
                if (rs.next())
                    category.setId(rs.getInt(1));
            }
            logger.info("Category creating was successful");
            return category;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlCategoryDAO: create was failed.", e);
        }
    }

    @Override
    public List<Category> getAll(Locale locale, String sort, String order, int start, int total) throws DBException {
        String orderBy, query = SELECT_CATEGORIES_EN_LIMIT;
        if (sort != null && !sort.isEmpty()) {
            orderBy = sort + " " + order;
            if (locale.getLanguage().equals(Language.UA.getValue()))
                query = SELECT_CATEGORIES_UA_ORDER_LIMIT.replace(ORDER_BY, orderBy);
            else
                query = SELECT_CATEGORIES_EN_ORDER_LIMIT.replace(ORDER_BY, orderBy);
        } else if (order.equals("desc")){
            if (locale.getLanguage().equals(Language.UA.getValue()))
                query = SELECT_CATEGORIES_UA_REVERSE_LIMIT;
            else
                query = SELECT_CATEGORIES_EN_REVERSE_LIMIT;
        } else {
            if (locale.getLanguage().equals(Language.UA.getValue()))
                query = SELECT_CATEGORIES_UA_LIMIT;
        }

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            List<Category> categoryList = new ArrayList<>();
            int c = 0;
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            CategoryMapper mapper = new CategoryMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Category category = mapper.mapRow(rs);
//                    category.setId(rs.getInt(COL_ID));
//                    category.setNameEN(rs.getString(COL_NAME_EN));
//                    category.setNameUA(rs.getString(COL_NAME_UA));
                    categoryList.add(category);
                }
            }
            logger.info("Selection of categories was successful");
            logger.info("Count of categories: " + categoryList.size());
            return categoryList;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlCategoryDAO: getAll was failed.", e);
        }
    }

    @Override
    public List<Category> getAllWhereName(String searchLang, String name, String sort,
                                          String order, int start, int total) throws DBException {
        String orderBy, query;
        if (searchLang.equals(Language.UA.getValue()))
            query = SELECT_CATEGORIES_LIKE_UA_LIMIT;
        else
            query = SELECT_CATEGORIES_LIKE_EN_LIMIT;
        if (sort != null && !sort.isEmpty()) {
            orderBy = sort + " " + order;
            if (searchLang.equals(Language.UA.getValue()))
                query = SELECT_CATEGORIES_LIKE_UA_ORDER_LIMIT.replace(ORDER_BY, orderBy);
            else
                query = SELECT_CATEGORIES_LIKE_EN_ORDER_LIMIT.replace(ORDER_BY, orderBy);
        } else if (order.equals("desc")){
            if (searchLang.equals(Language.UA.getValue()))
                query = SELECT_CATEGORIES_LIKE_UA_REVERSE_LIMIT;
            else
                query = SELECT_CATEGORIES_LIKE_EN_REVERSE_LIMIT;
        } else {
            if (searchLang.equals(Language.UA.getValue()))
                query = SELECT_CATEGORIES_LIKE_UA_LIMIT;
        }

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(query)) {
            List<Category> categoryList = new ArrayList<>();
            int c = 0;
            prst.setString(++c, name + "%");
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            CategoryMapper mapper = new CategoryMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Category category = mapper.mapRow(rs);
                    categoryList.add(category);
                }
            }
            logger.info("Selection of queried categories was successful");
            logger.info("Count of queried categories: " + categoryList.size());
            return categoryList;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlCategoryDAO: getAllWhereName was failed.", e);
        }
    }

    @Override
    public List<Category> getAllForActivities(Locale locale) throws DBException {
        try (Connection con = factory.getConnection();
             Statement stmt = con.createStatement()) {
            List<Category> categoryList = new ArrayList<>();
            ResultSet rs = null;
            if (locale.getLanguage().equals(Language.UA.getValue()))
                rs = stmt.executeQuery(SELECT_CATEGORIES_UA);
            else
                rs = stmt.executeQuery(SELECT_CATEGORIES_EN);
            CategoryMapper mapper = new CategoryMapper();
            while (rs.next()) {
                Category category = mapper.mapRow(rs);
                categoryList.add(category);
            }
            logger.info("Selection of categories for activities was successful");
            logger.info("Count of categories: " + categoryList.size());
            return categoryList;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlCategoryDAO: getAllForActivities was failed.", e);
        }
    }

    @Override
    public List<Category> getAllById(List<Integer> categoryIds, Locale locale) throws DBException {
        try (Connection con = factory.getConnection()) {
            PreparedStatement prst = null;
            List<Category> categoryList = null;
            if (categoryIds != null && !categoryIds.isEmpty()) {
                categoryList = new ArrayList<>();
                if (locale.getLanguage().equals(Language.UA.getValue()))
                    prst = con.prepareStatement(buildWhereInQuery(categoryIds, SELECT_CATEGORIES_BY_ID_UA));
                else
                    prst = con.prepareStatement(buildWhereInQuery(categoryIds, SELECT_CATEGORIES_BY_ID_EN));
                int c = 0;
                for (int categoryId : categoryIds)
                    prst.setInt(++c, categoryId);
                CategoryMapper mapper = new CategoryMapper();
                try (ResultSet rs = prst.executeQuery()) {
                    while (rs.next()) {
                        Category category = mapper.mapRow(rs);
                        categoryList.add(category);
                    }
                }
                logger.info("Selection of categories by their ids was successful");
                logger.info("Count of categories: " + categoryList.size());
            }
            return categoryList;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlCategoryDAO: getAllById was failed.", e);
        }
    }

    @Override
    public Category getById(int id) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_CATEGORY)) {
            prst.setInt(1, id);
            CategoryMapper mapper = new CategoryMapper();
            Category category = null;
            try (ResultSet rs = prst.executeQuery()) {
                if (rs.next())
                    category = mapper.mapRow(rs);
                logger.info("Selection of category by id was successful");
                return category;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlCategoryDAO: getById was failed.", e);
        }
    }

    @Override
    public Category getByName(String name, Language language) throws DBException {
        Connection con = null;
        PreparedStatement prst = null;
        try {
            con = factory.getConnection();
            if (language.equals(Language.EN))
                prst = con.prepareStatement(SELECT_CATEGORY_BY_NAME_EN);
            else if (language.equals(Language.UA))
                prst = con.prepareStatement(SELECT_CATEGORY_BY_NAME_UA);
            prst.setString(1, name);
            CategoryMapper mapper = new CategoryMapper();
            Category category = null;
            try (ResultSet rs = prst.executeQuery()) {
                if (rs.next())
                    category = mapper.mapRow(rs);
                logger.info("Selection of category by name was successful");
                return category;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlCategoryDAO: getByName was failed.", e);
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public Category getByNameOther(int id, String name, Language language) throws DBException {
        Connection con = null;
        PreparedStatement prst = null;
        try {
            con = factory.getConnection();
            if (language.equals(Language.EN))
                prst = con.prepareStatement(SELECT_CATEGORY_BY_NAME_EN_OTHER);
            else if (language.equals(Language.UA))
                prst = con.prepareStatement(SELECT_CATEGORY_BY_NAME_UA_OTHER);
            int c = 0;
            prst.setString(++c, name);
            prst.setInt(++c, id);
            CategoryMapper mapper = new CategoryMapper();
            Category category = null;
            try (ResultSet rs = prst.executeQuery()) {
                if (rs.next())
                    category = mapper.mapRow(rs);
                logger.info("Selection of other category with the same name was successfull");
                return category;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlCategoryDAO: getByNameOther was failed.", e);
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public int getCount() throws DBException {
        try (Connection con = factory.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(GET_CATEGORIES_COUNT)) {
            int count = 0;
            if (rs.next())
                count = rs.getInt(COL_COUNT);
            logger.info("Count selection of categories was successful");
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlCategoryDAO: getCount was failed.", e);
        }
    }

    @Override
    public int getCountWhereName(String name, String lang) throws DBException {
        Connection con = null;
        PreparedStatement prst = null;
        try {
            con = factory.getConnection();
            if (lang.equals(Language.UA.getValue()))
                prst = con.prepareStatement(GET_CATEGORIES_LIKE_UA_COUNT);
            else
                prst = con.prepareStatement(GET_CATEGORIES_LIKE_EN_COUNT);
            prst.setString(1, name + "%");
            try (ResultSet rs = prst.executeQuery()) {
                int count = 0;
                if (rs.next())
                    count = rs.getInt(COL_COUNT);
                logger.info("Count selection of queried categories was successful");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlCategoryDAO: getCountWhereName was failed.", e);
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public synchronized boolean update(int categoryId, String nameEN, String nameUA) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(UPDATE_CATEGORY)) {
            int c = 0;
            prst.setString(++c, nameEN);
            prst.setString(++c, nameUA);
            prst.setInt(++c, categoryId);
            prst.executeUpdate();
            logger.info("Category (id=" + categoryId + ") was successfully updated");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlCategoryDAO: update was failed.", e);
        }
    }

    @Override
    public synchronized boolean delete(int id) throws DBException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(DELETE_CATEGORY)) {
            prst.setInt(1, id);
            prst.execute();
            logger.info("Category (id=" + id + ") was successfully deleted");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new DBException("MysqlCategoryDAO: delete was failed.", e);
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
     * Category Mapper. Setting received category
     */
    private static class CategoryMapper implements EntityMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs) {
            try {
                Category category = new Category();
                category.setId(rs.getInt(COL_CATEGORIES_ID));
                category.setNameEN(rs.getString(COL_CATEGORIES_NAME_EN));
                category.setNameUA(rs.getString(COL_CATEGORIES_NAME_UA));
                return category;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
