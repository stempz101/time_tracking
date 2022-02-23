package com.tracking.dao.mysql;

import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.mapper.EntityMapper;
import com.tracking.models.Category;
import com.tracking.lang.Language;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.tracking.dao.mysql.MysqlConstants.*;
import static com.tracking.dao.mysql.MysqlConstants.SELECT_ALL_CATEGORIES_UA_ORDER_LIMIT;

public class MysqlCategoryDAO implements CategoryDAO {

    private DAOFactory factory = MysqlDAOFactory.getInstance();

    private static CategoryDAO instance;

    protected static CategoryDAO getInstance() {
        if (instance == null)
            instance = new MysqlCategoryDAO();
        return instance;
    }

    private MysqlCategoryDAO() {

    }

    @Override
    public boolean create(HttpSession session, String nameEN, String nameUA) throws SQLException {

        if (checkErrors(session, nameEN, nameUA)) return false;

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(INSERT_CATEGORY)) {
            int c = 0;
            prst.setString(++c, nameEN);
            prst.setString(++c, nameUA);
            prst.execute();
            session.removeAttribute("categoryEN");
            session.removeAttribute("categoryUA");
            session.setAttribute("successMessage", "Category \"" + nameEN + "/" + nameUA + "\" successfully created");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public boolean ifExists(HttpSession session, String name, Language language) {
        if (session.getAttribute("category") != null) {
            Category category = (Category) session.getAttribute("category");
            Category otherCategory = getByNameOther(category.getId(), name, language);
            return otherCategory != null;
        }
        Category category = getByName(name, language);
        return category != null;
    }

    @Override
    public List<Category> getAll(Language language, int start, int total) throws SQLException {
        Connection con = null;
        PreparedStatement prst = null;
        try {
            List<Category> categoryList = new ArrayList<>();
            con = factory.getConnection();
            if (language.equals(Language.EN))
                prst = con.prepareStatement(SELECT_ALL_CATEGORIES_EN_LIMIT);
            else if (language.equals(Language.UA))
                prst = con.prepareStatement(SELECT_ALL_CATEGORIES_UA_LIMIT);
            int c = 0;
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            CategoryMapper mapper = new CategoryMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Category category = mapper.mapRow(rs);
                    category.setId(rs.getInt(COL_ID));
                    category.setNameEN(rs.getString(COL_NAME_EN));
                    category.setNameUA(rs.getString(COL_NAME_UA));
                    categoryList.add(category);
                }
            }
            return categoryList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public List<Category> getAllOrder(Language language, String sort, String order, int start, int total) throws SQLException {
        Connection con = null;
        PreparedStatement prst = null;
        try {
            List<Category> categoryList = new ArrayList<>();
            String orderBy = sort + " " + order;
            con = factory.getConnection();
            if (language.equals(Language.EN))
                prst = con.prepareStatement(SELECT_ALL_CATEGORIES_EN_ORDER_LIMIT.replace(ORDER_BY, orderBy));
            else if (language.equals(Language.UA))
                prst = con.prepareStatement(SELECT_ALL_CATEGORIES_UA_ORDER_LIMIT.replace(ORDER_BY, orderBy));
            int c = 0;
            prst.setInt(++c, start - 1);
            prst.setInt(++c, total);
            CategoryMapper mapper = new CategoryMapper();
            try (ResultSet rs = prst.executeQuery()) {
                while (rs.next()) {
                    Category category = mapper.mapRow(rs);
                    categoryList.add(category);
                }
            }
            return categoryList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public List<Category> getAllWhereName(String name, Language language, int start, int total) throws SQLException {
        Connection con = null;
        PreparedStatement prst = null;
        try {
            List<Category> categoryList = new ArrayList<>();
            con = factory.getConnection();
            if (language.equals(Language.EN))
                prst = con.prepareStatement(SELECT_CATEGORIES_LIKE_EN_LIMIT);
            else if (language.equals(Language.UA))
                prst = con.prepareStatement(SELECT_CATEGORIES_LIKE_UA_LIMIT);
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
            return categoryList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public List<Category> getAllWhereNameOrder(String name, Language language, String sort, String order, int start, int total) throws SQLException {
        Connection con = null;
        PreparedStatement prst = null;
        try {
            List<Category> categoryList = new ArrayList<>();
            String orderBy = sort + " " + order;
            con = factory.getConnection();
            if (language.equals(Language.EN))
                prst = con.prepareStatement(SELECT_CATEGORIES_LIKE_EN_ORDER_LIMIT.replace(ORDER_BY, orderBy));
            else if (language.equals(Language.UA))
                prst = con.prepareStatement(SELECT_CATEGORIES_LIKE_UA_ORDER_LIMIT.replace(ORDER_BY, orderBy));
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
            return categoryList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public List<Category> getAllForActivities(Language language) throws SQLException {
        try (Connection con = factory.getConnection();
             Statement stmt = con.createStatement()) {
            List<Category> categoryList = new ArrayList<>();
            ResultSet rs = null;
            if (language.equals(Language.EN))
                rs = stmt.executeQuery(SELECT_ALL_CATEGORIES_EN);
            else if (language.equals(Language.UA))
                rs = stmt.executeQuery(SELECT_ALL_CATEGORIES_UA);
            CategoryMapper mapper = new CategoryMapper();
            while (rs.next()) {
                Category category = mapper.mapRow(rs);
                categoryList.add(category);
            }
            return categoryList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public List<Category> getAllById(List<Integer> categoryIds, Language language) throws SQLException {
        try (Connection con = factory.getConnection()) {
            PreparedStatement prst = null;
            List<Category> categoryList = null;
            if (categoryIds != null && !categoryIds.isEmpty()) {
                categoryList = new ArrayList<>();
                if (language.equals(Language.EN))
                    prst = con.prepareStatement(buildWhereInQuery(categoryIds, SELECT_ALL_CATEGORIES_BY_ID_EN));
                else if (language.equals(Language.UA))
                    prst = con.prepareStatement(buildWhereInQuery(categoryIds, SELECT_ALL_CATEGORIES_BY_ID_UA));
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
            }
            return categoryList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public Category getById(int id) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(SELECT_CATEGORY)) {
            prst.setInt(1, id);
            CategoryMapper mapper = new CategoryMapper();
            Category category = null;
            try (ResultSet rs = prst.executeQuery()) {
                if (rs.next())
                    category = mapper.mapRow(rs);
                return category;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public Category getByName(String name, Language language) {
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
                return category;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
        return null;
    }

    public Category getByNameOther(int id, String name, Language language) {
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
                return category;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
        return null;
    }

    @Override
    public int getCount() throws SQLException {
        try (Connection con = factory.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(GET_CATEGORIES_COUNT)) {
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
    public int getCountWhereName(String name, Language language) throws SQLException {
        Connection con = null;
        PreparedStatement prst = null;
        try {
            con = factory.getConnection();
            if (language.equals(Language.EN))
                prst = con.prepareStatement(GET_CATEGORIES_LIKE_EN_COUNT);
            else if (language.equals(Language.UA))
                prst = con.prepareStatement(GET_CATEGORIES_LIKE_UA_COUNT);
            prst.setString(1, name + "%");
            try (ResultSet rs = prst.executeQuery()) {
                int count = 0;
                if (rs.next())
                    count = rs.getInt(COL_COUNT);
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            factory.closeResource(prst);
            factory.closeResource(con);
        }
    }

    @Override
    public boolean update(HttpSession session, String nameEN, String nameUA) throws SQLException {
        if (checkErrors(session, nameEN, nameUA)) return false;

        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(UPDATE_CATEGORY)) {
            Category category = (Category) session.getAttribute("category");
            int c = 0;
            prst.setString(++c, nameEN);
            prst.setString(++c, nameUA);
            prst.setInt(++c, category.getId());
            prst.executeUpdate();
            session.removeAttribute("categoryEN");
            session.removeAttribute("categoryUA");
            session.removeAttribute("category");
            session.setAttribute("successMessage", "Category \"" + nameEN + "/" + nameUA + "\" successfully updated");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public void delete(HttpSession session, int id) throws SQLException {
        try (Connection con = factory.getConnection();
             PreparedStatement prst = con.prepareStatement(DELETE_CATEGORY)) {
            prst.setInt(1, id);
            prst.execute();
            session.setAttribute("successMessage", "Category with id = " + id + " successfully deleted");
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

    private boolean checkErrors(HttpSession session, String nameEN, String nameUA) {
        if (!CategoryDAO.validate(session, nameEN, nameUA))
            return true;

        if (ifExists(session, nameEN, Language.EN)) {
            session.setAttribute("categoryEnError", "Category \"" + nameEN + "\" already exists");
            return true;
        }
        if (ifExists(session, nameUA, Language.UA)) {
            session.setAttribute("categoryUaError", "Category \"" + nameUA + "\" already exists");
            return true;
        }
        return false;
    }

    private static class CategoryMapper implements EntityMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs) {
            try {
                Category category = new Category();
                category.setId(rs.getInt(COL_ID));
                category.setNameEN(rs.getString(COL_NAME_EN));
                category.setNameUA(rs.getString(COL_NAME_UA));
                return category;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
