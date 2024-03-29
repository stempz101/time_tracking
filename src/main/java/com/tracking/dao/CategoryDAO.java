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
     * @param nameEN entered name in English
     * @param nameUA entered name in Ukrainian
     * @return created category
     * @throws DBException if something went wrong while executing
     */
    Category create(String nameEN, String nameUA) throws DBException;

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
     * @param searchLang chosen language for search
     * @param name entered name
     * @param sort sort type
     * @param order order type
     * @param start index of item, from which selection will start
     * @param total index of item, to which selection will occur
     * @return received categories
     * @throws DBException if something went wrong while executing
     */
    List<Category> getAllWhereName(String searchLang, String name, String sort, String order, int start, int total) throws DBException;

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
     * Getting category by name in editing in order to check for existing
     * @param id id of editing category
     * @param name entered name
     * @param language language of name
     * @return received category if exists
     * @throws DBException if something went wrong while executing
     */
    Category getByNameOther(int id, String name, Language language) throws DBException;

    /**
     * Getting count of categories from DB
     * @return count of categories
     * @throws DBException if something went wrong while executing
     */
    int getCount() throws DBException;

    /**
     * Getting count of categories by name from DB
     * @param name entered name
     * @param lang chosen language for search
     * @return count of categories
     * @throws DBException if something went wrong while executing
     */
    int getCountWhereName(String name, String lang) throws DBException;

    /**
     * Updating category in DB
     * @param categoryId id of category
     * @param nameEN entered name in English
     * @param nameUA entered name in Ukrainian
     * @return true if category is updated, false if category didn't go through validation and if it exists
     * @throws DBException if something went wrong while executing
     */
    boolean update(int categoryId, String nameEN, String nameUA) throws DBException;

    /**
     * Deleting category from DB
     * @param id id of category
     * @return true if category is deleted
     * @throws DBException if something went wrong while executing
     */
    boolean delete(int id) throws DBException;

    /**
     * Category name (EN) validation
     * @param nameEN entered name in English
     * @return true if validation was successful
     */
    static boolean validateNameEn(String nameEN) {
        return nameEN != null && !nameEN.isEmpty() && nameEN.matches("[a-zA-Z'\\d\\s]+");
    }

    /**
     * Category name (UA) validation
     * @param nameUA entered name in Ukrainian
     * @return true if validation was successful
     */
    static boolean validateNameUa(String nameUA) {
        return nameUA != null && !nameUA.isEmpty() && nameUA.matches("[a-zA-Zа-щА-ЩЬьЮюЯяЇїІіЄєҐґ'\\d\\s]+");
    }
}
