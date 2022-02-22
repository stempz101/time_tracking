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
            categoryList = categoryDAO.getAll(Language.EN, start, total);
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

    public boolean processCategories(HttpServletRequest req) throws IOException, SQLException {
        try {
            int categoryCount = getCount();
            int start = 1;
            int page = 1;
            int pageCount = getPageCount(categoryCount, TOTAL_CATEGORIES);
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page <= 0 || page > pageCount)
                    return false;
                start = start + TOTAL_CATEGORIES * (page - 1);
            }
            int previousPage = 0;
            if (page > 1)
                previousPage = page - 1;
            int nextPage = 0;
            if (categoryCount > TOTAL_CATEGORIES && page < pageCount)
                nextPage = page + 1;

            List<Category> categoryList = getAllCategories(Language.EN, start, TOTAL_CATEGORIES); // localize

            req.setAttribute("pageCount", pageCount);
            req.setAttribute("previousPage", previousPage);
            req.setAttribute("nextPage", nextPage);
            req.setAttribute("categoryList", categoryList);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return true;
    }
}
