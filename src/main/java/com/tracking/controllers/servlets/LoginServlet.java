package com.tracking.controllers.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tracking.models.User;
import com.tracking.services.LoginService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private LoginService loginService = null;

    @Override
    public void init() throws ServletException {
        loginService = new LoginService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        RequestDispatcher requestDispatcher = context.getRequestDispatcher("/jsp/login.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        HttpSession session = req.getSession();
        session.setAttribute("email", email);

        User user = loginService.authUser(session, email, password);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        deleteAttributes(session);
        session.setAttribute("authUser", user);
        if (user.isAdmin())
            resp.sendRedirect(req.getContextPath() + "/a/activities");
        else
            resp.sendRedirect(req.getContextPath() + "/u/activities");

    }

    private void deleteAttributes(HttpSession session) {
        session.removeAttribute("email");
        session.removeAttribute("emailError");
        session.removeAttribute("passwordError");
    }
}
