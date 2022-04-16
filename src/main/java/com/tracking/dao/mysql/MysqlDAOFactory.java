package com.tracking.dao.mysql;

import com.tracking.dao.*;
import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * MySQL DAO Factory. Here there is a connection to the MySQL DB,
 * and also there is a connections between DAOs in this DB
 */
public class MysqlDAOFactory implements DAOFactory {

    private static final Logger logger = Logger.getLogger(MysqlDAOFactory.class);

    private static MysqlDAOFactory instance;
    private DataSource dataSource;

    public static synchronized MysqlDAOFactory getInstance() {
        if (instance == null)
            instance = new MysqlDAOFactory();
        return instance;
    }

    private MysqlDAOFactory() {

    }

    @Override
    public synchronized Connection getConnection() throws SQLException {
//        try {
//            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/time_tracking");
//            return dataSource.getConnection();
//        } catch (NamingException e) {
//            logger.error("jdbc/time_tracking is missing!");
//            logger.error(e);
//            throw new IllegalStateException("jdbc/time_tracking is missing!", e);
//        }
        return getConnectionForTest();
    }

    @Override
    public Connection getConnectionForTest() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.error(e);
        }
        return DriverManager
                .getConnection("jdbc:mysql://localhost:3306/time_tracking?autoReconnect=true", "root", "root");
    }

    @Override
    public void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error(e);
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
            logger.error(e);
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

    @Override
    public RequestDAO getRequestDao() {
        return MysqlRequestDAO.getInstance();
    }
}
