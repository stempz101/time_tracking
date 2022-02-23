package com.tracking.services.categories;

import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.lang.Language;
import com.tracking.models.Category;
import com.tracking.services.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CategoriesService extends Service {
    public List<Category> getAllCategories(Language language) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        List<Category> categoryList = null;
        try {
            categoryList = categoryDAO.getAllForActivities(language);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return categoryList;
    }

    public List<Category> getAllCategories(Language language, int start, int total) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        List<Category> categoryList = null;
        try {
            categoryList = categoryDAO.getAll(language, start, total);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return categoryList;
    }

    public int getCount() throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        try {
            return categoryDAO.getCount();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public int getCountWhereName(String name, Language language) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        try {
            return categoryDAO.getCountWhereName(name, language);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public boolean processCategories(HttpServletRequest req) throws IOException, SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        List<Category> categoryList;
        try {
            int start = 1;
            int page = 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page <= 0)
                    return false;
                start = start + TOTAL_CATEGORIES * (page - 1);
            }

            int categoryCount = getCount();
            if (req.getParameter("name") != null) {
                String name = req.getParameter("name");
                Language lang = null;
                if (req.getParameter("lang").equalsIgnoreCase(Language.EN.name()))
                    lang = Language.EN;
                else if (req.getParameter("lang").equalsIgnoreCase(Language.UA.name()))
                    lang = Language.UA;
                req.setAttribute("name", name);
                categoryCount = getCountWhereName(name, lang);
                if (req.getParameter("sort") != null && !req.getParameter("sort").isEmpty()) {
                    String sort = req.getParameter("sort");
                    String order = req.getParameter("order");
                    if (order == null || order.isEmpty())
                        order = "asc";
                    categoryList = categoryDAO.getAllWhereNameOrder(name, lang, sort, order, start, TOTAL_CATEGORIES);
                } else {
                    categoryList = categoryDAO.getAllWhereName(name, lang, start, TOTAL_CATEGORIES); // add localize
                }
            } else if (req.getParameter("sort") != null && !req.getParameter("sort").isEmpty()) {
                String sort = req.getParameter("sort");
                String order = req.getParameter("order");
                if (order == null || order.isEmpty())
                    order = "asc";
                categoryList = categoryDAO.getAllOrder(Language.EN, sort, order, start, TOTAL_CATEGORIES);
            } else {
                categoryList = getAllCategories(Language.EN, start, TOTAL_CATEGORIES);
            }

            int pageCount = getPageCount(categoryCount, TOTAL_CATEGORIES);
            int previousPage = 0;
            if (page > 1)
                previousPage = page - 1;
            int nextPage = 0;
            if (categoryCount > TOTAL_CATEGORIES && page < pageCount)
                nextPage = page + 1;

            req.setAttribute("pageCount", pageCount);
            req.setAttribute("previousPage", previousPage);
            req.setAttribute("nextPage", nextPage);
            req.setAttribute("categoryList", categoryList);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }
}
