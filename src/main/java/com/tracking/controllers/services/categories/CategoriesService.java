package com.tracking.controllers.services.categories;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.activities.ActivityService;
import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.lang.Language;
import com.tracking.models.Category;
import com.tracking.models.User;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * Service, that contains all actions with categories
 */
public class CategoriesService extends Service {

    private static final Logger logger = Logger.getLogger(CategoriesService.class);

    /**
     * Getting all categories
     * @param locale for setting order by name in given language
     * @return list of categories
     * @throws ServiceException if something went wrong while executing
     */
    public List<Category> getAllCategories(Locale locale) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        List<Category> categoryList = null;
        try {
            categoryList = categoryDAO.getAllForActivities(locale);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("CategoriesService: getAllCategories was failed", e);
        }
        return categoryList;
    }

    /**
     * Getting all essential for Categories page
     * @param req for getting session to get/set attributes, getting parameters
     * @param resp for sending redirect
     * @return true if process of categories was successful, false if request parameter "page" equal or less than 0 and
     * greater than assigned page count
     * @throws IOException if something went wrong while redirecting
     * @throws ServiceException if something went wrong while executing
     */
    public boolean processCategories(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        List<Category> categoryList;
        try {
            User authUser = (User) req.getSession().getAttribute("authUser");

            int start = 1;
            int page = 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page <= 0) {
                    redirectWithoutPage(req, resp, "categories", authUser);
                    return false;
                }
                start = start + TOTAL_CATEGORIES * (page - 1);
            }

            int categoryCount;
            String sort = req.getParameter("sort");
            String order = req.getParameter("order");
            Locale locale = Service.getLocale(req);
            if (order == null || order.isEmpty())
                order = "asc";
            if (req.getParameter("name") != null) {
                String name = req.getParameter("name");
                req.setAttribute("name", name);
                categoryCount = categoryDAO.getCountWhereName(name, locale);
                categoryList = categoryDAO.getAllWhereName(name, locale, sort, order, start, TOTAL_CATEGORIES); // add localize
            } else {
                categoryCount = categoryDAO.getCount();
                categoryList = categoryDAO.getAll(locale, sort, order, start, TOTAL_CATEGORIES); // add localize
            }

            int pageCount = getPageCount(categoryCount, TOTAL_CATEGORIES);
            if (pageCount > 0 && page > pageCount) {
                redirectWithoutPage(req, resp, "categories", authUser);
                return false;
            }

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
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("CategoriesService: processCategories was failed", e);
        }
    }
}
