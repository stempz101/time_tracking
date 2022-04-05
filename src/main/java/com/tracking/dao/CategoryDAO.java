package com.tracking.dao;

import com.tracking.controllers.services.Service;
import com.tracking.controllers.exceptions.DBException;
import com.tracking.models.Category;
import com.tracking.lang.Language;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Category DAO interface. Contains all essential methods for work with categories from DB
 */
public interface CategoryDAO {

    /**
     * Creates category in DB
     * @param req for getting session to get/set attributes, getting parameters
     * @param nameEN entered name in English
     * @param nameUA entered name in Ukrainian
     * @return true if category is created, false if category didn't go through validation and if it exists
     * @throws DBException if something went wrong while executing
     */
    boolean create(HttpServletRequest req, String nameEN, String nameUA) throws DBException;

    /**
     * Checking if category exists in DB
     * @param req for getting session to get/set attributes, getting parameters
     * @param name category name
     * @param language language for checking name on this selected
     * @return true if exists
     * @throws DBException if something went wrong while executing
     */
    boolean ifExists(HttpServletRequest req, String name, Language language) throws DBException;

    /**
     * Getting all categories from DB
     * @param locale locale for selection by chosen language
     * @param sort sort type
     * @param order order type
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @return received categories
     * @throws DBException if something went wrong while executing
     */
    List<Category> getAll(Locale locale, String sort, String order, int start, int total) throws DBException;

    /**
     * Getting all categories by name from DB
     * @param name entered name
     * @param locale locale for selection by chosen language
     * @param sort sort type
     * @param order order type
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @return received categories
     * @throws DBException if something went wrong while executing
     */
    List<Category> getAllWhereName(String name, Locale locale, String sort, String order, int start, int total) throws DBException;

    /**
     * Getting all categories for Activity page from DB
     * @param locale locale for selection by chosen language
     * @return received categories
     * @throws DBException if something went wrong while executing
     */
    List<Category> getAllForActivities(Locale locale) throws DBException;

    /**
     * Getting all categories by ids from DB
     * @param categoryIds category ids
     * @param locale locale for selection by chosen language
     * @return received categories
     * @throws DBException if something went wrong while executing
     */
    List<Category> getAllById(List<Integer> categoryIds, Locale locale) throws DBException;

    /**
     * Getting category by id from DB
     * @param id id of category
     * @return received category
     * @throws DBException if something went wrong while executing
     */
    Category getById(int id) throws DBException;

    /**
     * Getting category by name from DB
     * @param name category name
     * @param language selected language for selection
     * @return received category
     * @throws DBException if something went wrong while executing
     */
    Category getByName(String name, Language language) throws DBException;

    /**
     * Getting count of categories from DB
     * @return count of categories
     * @throws DBException if something went wrong while executing
     */
    int getCount() throws DBException;

    /**
     * Getting count of categories by name from DB
     * @param name entered name
     * @param locale locale for selection by chosen language
     * @return count of categories
     * @throws DBException if something went wrong while executing
     */
    int getCountWhereName(String name, Locale locale) throws DBException;

    /**
     * Updating category in DB
     * @param req for getting session to get/set attributes, getting parameters
     * @param nameEN entered name in English
     * @param nameUA entered name in Ukrainian
     * @return true if category is updated, false if category didn't go through validation and if it exists
     * @throws DBException if something went wrong while executing
     */
    boolean update(HttpServletRequest req, String nameEN, String nameUA) throws DBException;

    /**
     * Deleting category from DB
     * @param req for getting session to get/set attributes, getting parameters
     * @param id id of category
     * @throws DBException if something went wrong while executing
     */
    void delete(HttpServletRequest req, int id) throws DBException;

    /**
     * Category validation
     * @param req for getting session to get/set attributes, getting parameters
     * @param nameEN entered name in English
     * @param nameUA entered name in Ukrainian
     * @return true if validation was successful
     */
    static boolean validateCategory(HttpServletRequest req, String nameEN, String nameUA) {
        return validateNameEn(req, nameEN) && validateNameUa(req, nameUA);
    }

    /**
     * Category name (EN) validation
     * @param req for getting session to get/set attributes, getting parameters
     * @param nameEN entered name in English
     * @return true if validation was successful
     */
    private static boolean validateNameEn(HttpServletRequest req, String nameEN) {
        int errorCount = 0;
        ResourceBundle bundle = ResourceBundle.getBundle("content", Service.getLocale(req));
        if (nameEN == null || nameEN.isEmpty()) {
            req.getSession().setAttribute("messageError", bundle.getString("message.category_en_empty"));
            errorCount++;
        } else if (!nameEN.matches("[a-zA-Z'\\d\\s]+")) {
            req.getSession().setAttribute("messageError", bundle.getString("message.category_en_invalid"));
            errorCount++;
        }
        return errorCount == 0;
    }

    /**
     * Category name (UA) validation
     * @param req for getting session to get/set attributes, getting parameters
     * @param nameUA entered name in Ukrainian
     * @return true if validation was successful
     */
    private static boolean validateNameUa(HttpServletRequest req, String nameUA) {
        int errorCount = 0;
        ResourceBundle bundle = ResourceBundle.getBundle("content", Service.getLocale(req));
        if (nameUA == null || nameUA.isEmpty()) {
            req.getSession().setAttribute("messageError", bundle.getString("message.category_ua_empty"));
            errorCount++;
        } else if (!nameUA.matches("[a-zA-Zа-щА-ЩЬьЮюЯяЇїІіЄєҐґ'\\d\\s]+")) {
            req.getSession().setAttribute("messageError", bundle.getString("message.category_ua_invalid"));
            errorCount++;
        }
        return errorCount == 0;
    }
}
