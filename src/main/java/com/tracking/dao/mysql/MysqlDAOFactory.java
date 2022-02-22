package com.tracking.dao.mysql;

import com.tracking.dao.ActivityDAO;
import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MysqlDAOFactory implements DAOFactory {

    private static MysqlDAOFactory instance;
    private DataSource dataSource;

    public static synchronized MysqlDAOFactory getInstance() {
        if (instance == null)
            instance = new MysqlDAOFactory();
        return instance;
    }

    private MysqlDAOFactory() {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/time_tracking");
        } catch (NamingException e) {
            System.out.println("jdbc/time_tracking is missing!");
            throw new IllegalStateException("jdbc/time_tracking is missing!", e);
        }
    }

    @Override
    public synchronized Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void closeResource(AutoCloseable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ActivityDAO getActivityDao() {
        return MysqlActivityDAO.getInstance();
    }

    @Override
    public CategoryDAO getCategoryDao() {
        return MysqlCategoryDAO.getInstance();
    }

    @Override
    public UserDAO getUserDao() {
        return MysqlUserDAO.getInstance();
    }
}
