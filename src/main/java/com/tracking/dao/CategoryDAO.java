package com.tracking.dao;

import com.tracking.models.Category;
import com.tracking.lang.Language;

import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface CategoryDAO {

    boolean create(HttpSession session, String nameEN, String nameUA) throws SQLException;

    boolean ifExists(HttpSession session, String name, Language language);

    List<Category> getAll(Language language, int start, int total) throws SQLException;

    List<Category> getAllOrder(Language language, String sort, String order, int start, int total) throws SQLException;

    List<Category> getAllWhereName(String name, Language language, int start, int total) throws SQLException;

    List<Category> getAllWhereNameOrder(String name, Language language, String sort, String order, int start, int total) throws SQLException;

    List<Category> getAllForActivities(Language language) throws SQLException;

    List<Category> getAllById(List<Integer> categoryIds, Language language) throws SQLException;

    Category getById(int id) throws SQLException;

    Category getByName(String name, Language language);

    int getCount() throws SQLException;

    int getCountWhereName(String name, Language language) throws SQLException;

    boolean update(HttpSession session, String nameEN, String nameUA) throws SQLException;

    void delete(HttpSession session, int id) throws SQLException;

    static boolean validate(HttpSession session, String nameEN, String nameUA) {
        int errorCount = 0;
        if (nameEN == null || nameEN.isEmpty()) {
            session.setAttribute("categoryEnError", "Category (EN) cannot be empty");
            errorCount++;
        } else if (!nameEN.matches("[a-zA-Z'\\d\\s]+")) {
            session.setAttribute("categoryEnError", "Category (EN) input is invalid");
            errorCount++;
        }

        if (nameUA == null || nameUA.isEmpty()) {
            session.setAttribute("categoryUaError", "Category (UA) cannot be empty");
            errorCount++;
        } else if (!nameUA.matches("[a-zA-Zа-щА-ЩЬьЮюЯяЇїІіЄєҐґ'\\d\\s]+")) {
            session.setAttribute("categoryUaError", "Category (UA) input is invalid");
            errorCount++;
        }

        return errorCount == 0;
    }
}
