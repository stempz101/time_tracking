package com.tracking.services.categories;

import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.lang.Language;
import com.tracking.models.Category;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CategoriesService {
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

    public void processCategories(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        try {
            int categoryCount = getCount();
            int start = 1;
            int total = 10;
            int page = 1;
            int pageCount = categoryCount % total == 0 ? categoryCount / total
                    : categoryCount / total + 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page <= 0 || page > pageCount) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                start = start + total * (page - 1);
            }
            int previousPage = 0;
            if (page > 1)
                previousPage = page - 1;
            int nextPage = 0;
            if (categoryCount > total && page < pageCount)
                nextPage = page + 1;

            List<Category> categoryList = getAllCategories(Language.EN, start, total); // localize

            req.setAttribute("pageCount", pageCount);
            req.setAttribute("previousPage", previousPage);
            req.setAttribute("nextPage", nextPage);
            req.setAttribute("categoryList", categoryList);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }
}
