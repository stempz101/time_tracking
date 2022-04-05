package com.tracking.controllers.services.requests;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.activities.ActivityService;
import com.tracking.controllers.services.profile.ProfileService;
import com.tracking.dao.ActivityDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.RequestDAO;
import com.tracking.lang.Language;
import com.tracking.models.Activity;
import com.tracking.models.Category;
import com.tracking.models.Request;
import com.tracking.models.User;
import com.tracking.controllers.services.Service;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

/**
 * Service, that contains all actions with request
 */
public class RequestService extends Service {

    private static final Logger logger = Logger.getLogger(RequestService.class);

    /**
     * Getting request
     * @param req for getting session to get/set attributes, getting parameters
     * @param requestId id of request
     * @return received request
     * @throws ServiceException if something went wrong while executing
     */
    public Request get(HttpServletRequest req, int requestId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        RequestDAO requestDAO = factory.getRequestDao();
        try {
            Request request = requestDAO.get(requestId);
            List<Category> categories = new ActivityService().getActivityCategories(request.getActivity().getCategories(), Service.getLocale(req)); // localize
            req.setAttribute("request", request);
            req.setAttribute("categories", categories);
            return request;
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("RequestService: get was failed", e);
        }
    }

    /**
     * Creating request for add activity
     * @param req for getting session to get/set attributes, getting parameters
     * @param activity created activity
     * @return true if request is created
     * @throws ServiceException if something went wrong while executing
     */
    public boolean addForAdd(HttpServletRequest req, Activity activity) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        RequestDAO requestDAO = factory.getRequestDao();
        boolean result = false;
        try {
            result = requestDAO.create(req, activity, false);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("RequestService: addForAdd was failed", e);
        }
        return result;
    }

    /**
     * Creating request for remove activity
     * @param req for getting session to get/set attributes, getting parameters
     * @param activity selected activity
     * @return true if request is created
     * @throws ServiceException if something went wrong while executing
     */
    public boolean addForDelete(HttpServletRequest req, Activity activity) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        RequestDAO requestDAO = factory.getRequestDao();
        boolean result = false;
        try {
            result = requestDAO.create(req, activity, true);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("RequestService: addForDelete was failed", e);
        }
        return result;
    }

    /**
     * Confirmation of adding request
     * @param requestId id of request
     * @param activityId id of activity
     * @param creatorId id of creator
     * @throws ServiceException if something went wrong while executing
     */
    public void confirmAdd(int requestId, int activityId, int creatorId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        RequestDAO requestDAO = factory.getRequestDao();
        try {
            requestDAO.confirmAdd(requestId, activityId, creatorId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("RequestService: confirmAdd was failed", e);
        }
    }

    /**
     * Confirmation of removing request
     * @param request selected request
     * @throws ServiceException if something went wrong while executing
     */
    public void confirmRemove(Request request) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            activityDAO.delete(request.getActivityId());
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("RequestService: confirmRemove was failed", e);
        }
    }

    /**
     * Canceling of adding request
     * @param req for getting session to get/set attributes, getting parameters
     * @param request selected request
     * @throws ServiceException if something went wrong while executing
     */
    public void cancelAdd(HttpServletRequest req, Request request) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            User authUser = (User) req.getSession().getAttribute("authUser");
            if (authUser.isAdmin())
                activityDAO.delete(request.getActivityId());
            else
                activityDAO.deleteByCreator(request.getActivityId(), authUser.getId());
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("RequestService: cancelAdd was failed", e);
        }
    }

    /**
     * Canceling of removing request
     * @param req for getting session to get/set attributes, getting parameters
     * @param requestId id of request
     * @throws ServiceException if something went wrong while executing
     */
    public void cancelRemove(HttpServletRequest req, int requestId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        RequestDAO requestDAO = factory.getRequestDao();
        try {
            User authUser = (User) req.getSession().getAttribute("authUser");
            requestDAO.delete(requestId, authUser);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("RequestService: cancelRemove was failed", e);
        }
    }

    /**
     * Deleting request
     * @param request selected request
     * @throws ServiceException if something went wrong while executing
     */
    public void delete(Request request) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            activityDAO.delete(request.getActivityId());
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("RequestService: delete was failed", e);
        }
    }
}
