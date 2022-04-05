package com.tracking.controllers.services.categories;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.models.Category;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * Service, that contains all actions with category
 */
public class CategoryService extends Service {

    private static final Logger logger = Logger.getLogger(CategoryService.class);

    /**
     * Creating category
     * @param req for getting session to get/set attributes, getting parameters
     * @param nameEN name of category on English
     * @param nameUA name of category on Ukrainian
     * @return true if category is created
     * @throws ServiceException if something went wrong while executing
     */
    public boolean add(HttpServletRequest req, String nameEN, String nameUA) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        try {
            return categoryDAO.create(req, nameEN, nameUA);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("CategoryService: add was failed", e);
        }
    }

    /**
     * Getting category
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
     * @param req for getting session to get/set attributes, getting parameters
     * @param nameEN name of category on English
     * @param nameUA name of category on Ukrainian
     * @return true if category is updated
     * @throws ServiceException if something went wrong while executing
     */
    public boolean update(HttpServletRequest req, String nameEN, String nameUA) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        try {
            return categoryDAO.update(req, nameEN, nameUA);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("CategoryService: update was failed", e);
        }
    }

    /**
     * Deleting category
     * @param req for getting session to get/set attributes, getting parameters
     * @param categoryId id of category
     * @throws ServiceException if something went wrong while executing
     */
    public void delete(HttpServletRequest req, int categoryId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        try {
            categoryDAO.delete(req, categoryId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("CategoryService: delete was failed", e);
        }
    }
}
