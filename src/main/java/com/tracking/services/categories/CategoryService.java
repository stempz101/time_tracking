package com.tracking.services.categories;

import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.models.Category;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class CategoryService {
    public boolean add(HttpSession session, String nameEN, String nameUA) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        try {
            return categoryDAO.create(session, nameEN, nameUA);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public Category get(int categoryId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        Category category = null;
        try {
            category = categoryDAO.getById(categoryId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return category;
    }

    public boolean update(HttpSession session, String nameEN, String nameUA) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        try {
            return categoryDAO.update(session, nameEN, nameUA);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void delete(HttpSession session, int categoryId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        try {
            categoryDAO.delete(session, categoryId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }
}
