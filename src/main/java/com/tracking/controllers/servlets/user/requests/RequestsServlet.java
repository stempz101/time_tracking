package com.tracking.controllers.servlets.user.requests;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.requests.RequestsService;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet, that responsible for showing requests (user)
 */
@WebServlet("/u/requests")
public class RequestsServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RequestsServlet.class);

    private RequestsService requestsService = null;

    @Override
    public void init() throws ServletException {
        requestsService = new RequestsService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Service.setLang(req);
            if (!requestsService.processRequests(req, resp))
                return;
            requestsService.setQueryStringForPagination(req);
            logger.info("Opening Requests page (user)");
            req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/user/requests/requests.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
