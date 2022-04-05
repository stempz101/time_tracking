package com.tracking.dao;

import com.tracking.dao.mysql.MysqlDAOFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DAO Factory interface. With it, you can get the factory of the selected DB.
 */
public interface DAOFactory {

    /**
     * Getting factory of chosen DB
     * @param factoryType type of factory (DB)
     * @return selected factory
     */
    static DAOFactory getDAOFactory(FactoryType factoryType) {
        if (factoryType.equals(FactoryType.MYSQL))
            return MysqlDAOFactory.getInstance();
        System.out.println("Invalid factory type");
        throw new IllegalArgumentException("Factory type: " + factoryType + " is invalid");
    }

    /**
     * Getting connection of DB
     * @return connection of DB
     * @throws SQLException if something went wrong with connection
     */
    Connection getConnection() throws SQLException;

    /**
     * Transaction rollback
     * @param con connection of DB
     */
    void rollback(Connection con);

    /**
     * Closing opened resources
     * @param closeable opened resources
     */
    void closeResource(AutoCloseable closeable);

    /**
     * Getting Activity DAO
     * @return activity DAO
     */
    ActivityDAO getActivityDao();

    /**
     * Getting Category DAO
     * @return category DAO
     */
    CategoryDAO getCategoryDao();

    /**
     * Getting User DAO
     * @return user DAO
     */
    UserDAO getUserDao();

    /**
     * Getting Request DAO
     * @return request DAO
     */
    RequestDAO getRequestDao();

    /**
     * Enumeration of factory (DB) types
     */
    enum FactoryType {
        MYSQL
    }
}
