package com.tracking.controllers.servlets.admin.requests;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.requests.RequestService;
import com.tracking.models.Request;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Servlet, that responsible for declining remove request (admin)
 */
@WebServlet("/a/decl-req-rem")
public class DeclineRequestRemoveServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DeclineRequestRemoveServlet.class);

    private RequestService requestService = null;

    @Override
    public void init() throws ServletException {
        requestService = new RequestService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int requestId = Integer.parseInt(req.getParameter("id"));

        try {
            Request request = requestService.get(req, requestId);
            requestService.declineRemove(request);
            ResourceBundle bundle = ResourceBundle.getBundle("content",
                    Service.getLocale((String) req.getSession().getAttribute("lang")));
            req.setAttribute("successMessage", bundle.getString("message.req_declined"));
            logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                    "/a/requests"));
            resp.sendRedirect(req.getContextPath() + "/a/requests");
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
