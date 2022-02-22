package com.tracking.controllers.servlets.img;

import com.tracking.controllers.constants.FilePaths;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/img/*")
public class DisplayImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/jpeg");
        ServletOutputStream fout = resp.getOutputStream();
        FileInputStream fin = new FileInputStream(req.getSession().getServletContext().getRealPath("")
                + "img" + req.getPathInfo());

        BufferedInputStream bin = new BufferedInputStream(fin);
        BufferedOutputStream bout = new BufferedOutputStream(fout);
        int ch;
        while ((ch = bin.read()) != -1) {
            bout.write(ch);
        }

        bin.close();
        fin.close();
        bout.close();
        fout.close();
    }
}
