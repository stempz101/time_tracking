package com.tracking.controllers.servlets;

import com.tracking.models.User;
import com.tracking.services.RegisterService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;

@WebServlet("/register")
@MultipartConfig
public class RegisterServlet extends HttpServlet {

    private RegisterService registerService = null;

    @Override
    public void init() throws ServletException {
        registerService = new RegisterService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        RequestDispatcher requestDispatcher = context.getRequestDispatcher("/jsp/register.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lastName = req.getParameter("last_name");
        String firstName = req.getParameter("first_name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        Part image = req.getPart("image");
        String imageName = registerService.setImageName(image);

        HttpSession session = req.getSession();
        saveFields(session, lastName, firstName, email, password);

        User user = registerService.registerUser(session, lastName, firstName, email, password, confirmPassword, imageName);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        registerService.saveUserImage(image, user.getImage(), getServletContext().getRealPath(""));
        deleteAttributes(session);
        session.setAttribute("authUser", user);
        resp.sendRedirect(req.getContextPath() + "/u/activities");
    }

    private void saveFields(HttpSession session, String lastName, String firstName, String email, String password) {
        session.setAttribute("lastName", lastName);
        session.setAttribute("firstName", firstName);
        session.setAttribute("email", email);
        session.setAttribute("password", password);
    }

    private void deleteAttributes(HttpSession session) {
        session.removeAttribute("lastName");
        session.removeAttribute("firstName");
        session.removeAttribute("email");
        session.removeAttribute("password");
        session.removeAttribute("regError");
    }
}
