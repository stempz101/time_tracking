package com.tracking.controllers.servlets.admin.requests;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.requests.RequestService;
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
 * Servlet, that responsible for showing request (admin)
 */
@WebServlet("/a/request")
public class RequestServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RequestServlet.class);

    private RequestService requestService = null;

    @Override
    public void init() throws ServletException {
        requestService = new RequestService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("lang") != null)
            req.getSession().setAttribute("lang", req.getParameter("lang"));
        int requestId = Integer.parseInt(req.getParameter("id"));

        try {
            requestService.get(req, requestId);
            logger.info("Opening Request (id=" + requestId + ") page (admin)");
            req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/requests/request.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
