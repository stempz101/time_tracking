package com.tracking.controllers.filters;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.controllers.services.requests.RequestsService;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.models.User;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Filter that handles permissions to perform admin actions
 */
@WebFilter("/a/*")
public class AdminFilter implements Filter {

    private static final Logger logger = Logger.getLogger(AdminFilter.class);

    FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();

        try {
            User authUser = (User) session.getAttribute("authUser");
            if (authUser == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            authUser = userDAO.getById(authUser.getId());
            session.setAttribute("authUser", authUser);
            if (authUser.isBlocked()) {
                ServletContext context = config.getServletContext();
                RequestDispatcher requestDispatcher = context.getRequestDispatcher("/WEB-INF/jsp/blocked.jsp");
                requestDispatcher.forward(request, response);
                return;
            }
            if (!authUser.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/u/activities");
                return;
            }
            filterChain.doFilter(request, response);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
