package com.tracking.controllers.services.categories;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.lang.Language;
import com.tracking.models.Category;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Service, that contains all actions with category
 */
public class CategoryService extends Service {

    private static final Logger logger = Logger.getLogger(CategoryService.class);

    /**
     * Creating category
     *
     * @param req    for getting session to get/set attributes, getting parameters
     * @param nameEN name of category on English
     * @param nameUA name of category on Ukrainian
     * @return true if category is created
     * @throws ServiceException if something went wrong while executing
     */
    public boolean add(HttpServletRequest req, String nameEN, String nameUA) throws ServiceException {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("content",
                    Service.getLocale((String) req.getSession().getAttribute("lang")));
            if (!CategoryDAO.validateNameEn(nameEN)) {
                req.getSession().setAttribute("messageError", bundle.getString("message.category_en_error"));
                logger.error("Validation error occurred (nameEN): " + req.getSession().getAttribute("messageError"));
                return false;
            }
            if (!CategoryDAO.validateNameUa(nameUA)) {
                req.getSession().setAttribute("messageError", bundle.getString("message.category_ua_error"));
                logger.error("Validation error occurred (nameUA): " + req.getSession().getAttribute("messageError"));
                return false;
            }

            DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
            CategoryDAO categoryDAO = factory.getCategoryDao();

            if (categoryDAO.getByName(nameEN, Language.EN) != null) {
                req.getSession().setAttribute("messageError", bundle.getString("message.category_en_exists"));
                logger.error(req.getSession().getAttribute("messageError"));
                return false;
            }
            if (categoryDAO.getByName(nameUA, Language.UA) != null) {
                req.getSession().setAttribute("messageError", bundle.getString("message.category_ua_exists"));
                logger.error(req.getSession().getAttribute("messageError"));
                return false;
            }

            if (categoryDAO.create(nameEN, nameUA) != null) {
                req.getSession().removeAttribute("categoryEN");
                req.getSession().removeAttribute("categoryUA");
                req.getSession().setAttribute("successMessage", bundle.getString("message.category_created"));
                return true;
            }
            return false;
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("CategoryService: add was failed", e);
        }
    }

    /**
     * Getting category
     *
     * @param categoryId id of category
     * @return received category
     * @throws ServiceException if something went wrong while executing
     */
    public Category get(int categoryId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        Category category = null;
        try {
            category = categoryDAO.getById(categoryId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("CategoryService: get was failed", e);
        }
        return category;
    }

    /**
     * Updating category
     *
     * @param req    for getting session to get/set attributes, getting parameters
     * @param nameEN name of category on English
     * @param nameUA name of category on Ukrainian
     * @return true if category is updated
     * @throws ServiceException if something went wrong while executing
     */
    public boolean update(HttpServletRequest req, String nameEN, String nameUA) throws ServiceException {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("content",
                    Service.getLocale((String) req.getSession().getAttribute("lang")));
            if (!CategoryDAO.validateNameEn(nameEN)) {
                req.getSession().setAttribute("messageError", bundle.getString("message.category_en_error"));
                logger.error(req.getSession().getAttribute("messageError"));
                return false;
            }
            if (!CategoryDAO.validateNameUa(nameUA)) {
                req.getSession().setAttribute("messageError", bundle.getString("message.category_ua_error"));
                logger.error(req.getSession().getAttribute("messageError"));
                return false;
            }

            DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
            CategoryDAO categoryDAO = factory.getCategoryDao();
            Category category = (Category) req.getSession().getAttribute("category");

            if (categoryDAO.getByNameOther(category.getId(), nameEN, Language.EN) == null) {
                if (categoryDAO.getByNameOther(category.getId(), nameUA, Language.UA) == null) {
                    if (categoryDAO.update(category.getId(), nameEN, nameUA)) {
                        req.getSession().removeAttribute("categoryEN");
                        req.getSession().removeAttribute("categoryUA");
                        req.getSession().removeAttribute("category");
                        req.getSession().setAttribute("successMessage", bundle.getString("message.category_updated"));
                        return true;
                    }
                }
                req.getSession().setAttribute("messageError", bundle.getString("message.category_ua_exists"));
                return false;
            }
            req.getSession().setAttribute("messageError", bundle.getString("message.category_en_exists"));
            return false;
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("CategoryService: update was failed", e);
        }
    }

    /**
     * Deleting category
     *
     * @param req        for getting session to get/set attributes, getting parameters
     * @param categoryId id of category
     * @throws ServiceException if something went wrong while executing
     */
    public void delete(HttpServletRequest req, int categoryId) throws ServiceException {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
            CategoryDAO categoryDAO = factory.getCategoryDao();

            categoryDAO.delete(categoryId);
            ResourceBundle bundle = ResourceBundle.getBundle("content",
                    Service.getLocale((String) req.getSession().getAttribute("lang")));
            req.getSession().setAttribute("successMessage", bundle.getString("message.category_deleted"));
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("CategoryService: delete was failed", e);
        }
    }
}
