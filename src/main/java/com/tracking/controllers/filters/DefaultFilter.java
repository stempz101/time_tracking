package com.tracking.controllers.filters;

import com.tracking.controllers.services.Service;
import com.tracking.models.User;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter, that processes the welcome page for specific users
 */
@WebFilter("/")
public class DefaultFilter implements Filter {

    private static final Logger logger = Logger.getLogger(DefaultFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("authUser");
        if (user == null) {
            logger.info("Redirecting to " + request.getRequestURL().toString() + "login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        } else if (user.isAdmin()) {
            logger.info("Redirecting to " + Service.getFullURL(request, "/a/activities"));
            response.sendRedirect(request.getContextPath() + "/a/activities");
            return;
        } else if (!user.isAdmin()) {
            logger.info("Redirecting to " + Service.getFullURL(request, "/u/activities"));
            response.sendRedirect(request.getContextPath() + "/u/activities");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
