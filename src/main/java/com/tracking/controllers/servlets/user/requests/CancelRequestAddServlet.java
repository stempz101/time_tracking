package com.tracking.controllers.servlets.user.requests;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.servlets.user.activities.StopTimeServlet;
import com.tracking.models.Request;
import com.tracking.controllers.services.requests.RequestService;
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
 * Servlet, that responsible for canceling add request (user)
 */
@WebServlet("/u/canc-req-add")
public class CancelRequestAddServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(CancelRequestAddServlet.class);

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
            requestService.cancelAdd(req, request);
            requestService.deleteActivityImage(request.getActivity().getImage(), getServletContext().getRealPath(""));
            ResourceBundle bundle = ResourceBundle.getBundle("content", Service.getLocale(req));
            req.getSession().setAttribute("successMessage", bundle.getString("message.req_cancelled"));
            logger.info("Redirecting to " + Service.getFullURL(req, "/u/requests"));
            resp.sendRedirect(req.getContextPath() + "/u/requests");
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
