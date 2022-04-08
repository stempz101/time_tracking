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
 * Filter, that allows unauthorized users to go to the login and registration page
 */
@WebFilter(urlPatterns = {"/login", "/register"})
public class LoginRegisterFilter implements Filter {

    private static final Logger logger = Logger.getLogger(LoginRegisterFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("authUser");
        if (user != null) {
            if (user.isAdmin()) {
                logger.info("Redirecting to " + Service.getFullURL(request.getRequestURL().toString(), request.getRequestURI(),
                        "/a/activities"));
                response.sendRedirect(request.getContextPath() + "/a/activities");
                return;
            }
            logger.info("Redirecting to " + Service.getFullURL(request.getRequestURL().toString(), request.getRequestURI(),
                    "/u/activities"));
            response.sendRedirect(request.getContextPath() + "/u/activities");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
