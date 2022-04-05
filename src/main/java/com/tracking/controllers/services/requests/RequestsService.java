package com.tracking.controllers.services.requests;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.RequestDAO;
import com.tracking.models.Request;
import com.tracking.models.User;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Service, that contains all actions with requests
 */
public class RequestsService extends Service {

    private static final Logger logger = Logger.getLogger(RequestsService.class);

    /**
     * Getting all essential for Requests page
     * @param req for getting session to get/set attributes, getting parameters
     * @param resp for sending redirect
     * @return true if process of categories was successful, false if request parameter "page" equal or less than 0 and
     * greater than assigned page count
     * @throws IOException if something went wrong while redirecting
     * @throws ServiceException if something went wrong while executing
     */
    public boolean processRequests(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        RequestDAO requestDAO = factory.getRequestDao();
        List<Request> requestList;

        try {
            User authUser = (User) req.getSession().getAttribute("authUser");

            int start = 1;
            int page = 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page <= 0) {
                    redirectWithoutPage(req, resp, "requests", authUser);
                    return false;
                }
                start = start + TOTAL_REQUESTS * (page - 1);
            }

            int requestCount;
            String sort = req.getParameter("sortBy");
            String order = req.getParameter("order");
            if (order == null || order.isEmpty())
                order = "asc";

            requestCount = requestDAO.getCount(authUser);
            requestList = requestDAO.getAll(sort, order, start, TOTAL_REQUESTS, authUser);

            int pageCount = getPageCount(requestCount, TOTAL_REQUESTS);
            if (pageCount > 0 && page > pageCount) {
                redirectWithoutPage(req, resp, "requests", authUser);
                return false;
            }

            int previousPage = 0;
            if (page > 1)
                previousPage = page - 1;
            int nextPage = 0;
            if (requestCount > TOTAL_USERS && page < pageCount)
                nextPage = page + 1;

            req.setAttribute("pageCount", pageCount);
            req.setAttribute("previousPage", previousPage);
            req.setAttribute("nextPage", nextPage);
            req.setAttribute("requestList", requestList);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("RequestsService: processRequests was failed", e);
        }
        return true;
    }
}
