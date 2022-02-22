package com.tracking.controllers.filters;

import com.tracking.models.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/login", "/register"})
public class LoginRegisterFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("authUser");
        if (user != null) {
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/a/activities");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/u/activities");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
