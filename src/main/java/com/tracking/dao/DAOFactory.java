package com.tracking.dao;

import com.tracking.dao.mysql.MysqlDAOFactory;

import java.sql.Connection;
import java.sql.SQLException;

public interface DAOFactory {

    static DAOFactory getDAOFactory(FactoryType factoryType) {
        if (factoryType.equals(FactoryType.MYSQL))
            return MysqlDAOFactory.getInstance();
        System.out.println("Invalid factory type");
        throw new IllegalArgumentException("Factory type: " + factoryType + " is invalid");
    }

    Connection getConnection() throws SQLException;

    void rollback(Connection con);

    void closeResource(AutoCloseable closeable);

    ActivityDAO getActivityDao();

    CategoryDAO getCategoryDao();

    UserDAO getUserDao();

    enum FactoryType {
        MYSQL
    }
}
