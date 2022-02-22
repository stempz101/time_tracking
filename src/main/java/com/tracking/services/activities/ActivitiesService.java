package com.tracking.services.activities;

import com.tracking.dao.ActivityDAO;
import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.lang.Language;
import com.tracking.models.Activity;
import com.tracking.models.Category;
import com.tracking.services.Service;
import com.tracking.services.categories.CategoriesService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class ActivitiesService extends Service {
    public int getMaxPeopleCount() throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            return activityDAO.getMaxPeopleCount();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public List<Activity> getAll(int peopleFrom, int peopleTo, int start, int total) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        List<Activity> activityList = null;
        try {
            activityList = activityDAO.getAll(peopleFrom, peopleTo, start, total);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    public List<Activity> getAllWhereCategory(List<Integer> categoryIds, int peopleFrom, int peopleTo, int start, int total)
            throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        List<Activity> activityList = null;
        try {
            activityList = activityDAO.getAllWhereCategory(categoryIds, peopleFrom, peopleTo, start, total);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    public List<Activity> getAllWhereCategoryIsOther(int peopleFrom, int peopleTo, int start, int total) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        List<Activity> activityList = null;
        try {
            activityList = activityDAO.getAllWhereCategoryIsNull(peopleFrom, peopleTo, start, total);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    public List<Activity> getAllBySearch(String searchQuery, int peopleFrom, int peopleTo, int start, int total) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        List<Activity> activityList = null;
        try {
            activityList = activityDAO.getAllLike(searchQuery, peopleFrom, peopleTo, start, total);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    public List<Activity> getAllBySearchWhereCategory(String searchQuery, List<Integer> categoryIds, int peopleFrom,
                                                      int peopleTo, int start, int total) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        List<Activity> activityList = null;
        try {
            activityList = activityDAO.getAllLikeAndWhereCategory(searchQuery, categoryIds,
                    peopleFrom, peopleTo, start, total);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    public List<Activity> getAllBySearchWhereCategoryIsOther(String searchQuery, int peopleFrom, int peopleTo,
                                                            int start, int total) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        List<Activity> activityList = null;
        try {
            activityList = activityDAO.getAllLikeAndWhereCategoryIsNull(searchQuery, peopleFrom, peopleTo, start, total);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activityList;
    }

    public int getCount(int peopleFrom, int peopleTo) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            return activityDAO.getCount(peopleFrom, peopleTo);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public int getCountWhereCategory(List<Integer> categoryIds, int peopleFrom, int peopleTo) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            return activityDAO.getCountWhereCategory(categoryIds, peopleFrom, peopleTo);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public int getCountWhereCategoryIsOther(int peopleFrom, int peopleTo) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            return activityDAO.getCountWhereCategoryIsNull(peopleFrom, peopleTo);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public int getCountBySearch(String searchQuery, int peopleFrom, int peopleTo) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            return activityDAO.getCountWhereLike(searchQuery, peopleFrom, peopleTo);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public int getCountBySearchWhereCategory(String searchQuery, List<Integer> categoryIds, int peopleFrom, int peopleTo)
            throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            return activityDAO.getCountWhereLikeAndCategory(searchQuery, categoryIds, peopleFrom, peopleTo);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public int getCountBySearchWhereCategoryIsOther(String searchQuery, int peopleFrom, int peopleTo) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            return activityDAO.getCountWhereLikeAndCategoryIsNull(searchQuery, peopleFrom, peopleTo);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void processActivities(HttpServletRequest req) throws SQLException {
        CategoriesService categoriesService = new CategoriesService();

        List<Integer> filteredList;
        List<Category> categoryList;
        List<Activity> activityList;
        try {
            int start = 1;
            int total = 3;

            int page = 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page > 1)
                    start = start + total * (page - 1);
            }

            int peopleMaxCount = getMaxPeopleCount();
            int peopleFrom = 0;
            int peopleTo = peopleMaxCount;
            if (req.getParameter("from") != null && !req.getParameter("from").isEmpty())
                peopleFrom = Integer.parseInt(req.getParameter("from"));
            if (req.getParameter("to") != null && !req.getParameter("to").isEmpty()) {
                peopleTo = Integer.parseInt(req.getParameter("to"));
            }

            int activityCount;
            categoryList = categoriesService.getAllCategories(Language.EN);
            categoryList.add(new Category(0, "Other", "Інше"));
            if (req.getParameter("search") != null) {
                String searchQuery = req.getParameter("search");
                req.setAttribute("searchQuery", searchQuery);
                if (req.getParameterValues("filter") != null) {
                    System.out.println("req.getParameterValues(\"filter\") != null");
                    filteredList = Arrays.stream(req.getParameterValues("filter"))
                            .map(Integer::parseInt)
                            .toList();
                    req.setAttribute("filterCategories", filteredList);
                    if (filteredList.size() == 1 && filteredList.contains(0)) {
                        activityCount = getCountBySearchWhereCategoryIsOther(searchQuery, peopleFrom, peopleTo);
                        activityList = getAllBySearchWhereCategoryIsOther(searchQuery, peopleFrom, peopleTo, start, total);
                    } else {
                        activityCount = getCountBySearchWhereCategory(searchQuery, filteredList, peopleFrom, peopleTo);
                        activityList = getAllBySearchWhereCategory(searchQuery, filteredList, peopleFrom, peopleTo, start, total);
                    }
                } else {
                    activityCount = getCountBySearch(searchQuery, peopleFrom, peopleTo);
                    activityList = getAllBySearch(searchQuery, peopleFrom, peopleTo, start, total);
                }
            } else if (req.getParameterValues("filter") != null) {
                filteredList = Arrays.stream(req.getParameterValues("filter"))
                        .map(Integer::parseInt)
                        .toList();
                req.setAttribute("filterCategories", filteredList);
                if (filteredList.size() == 1 && filteredList.contains(0)) {
                    activityCount = getCountWhereCategoryIsOther(peopleFrom, peopleTo);
                    activityList = getAllWhereCategoryIsOther(peopleFrom, peopleTo,start, total);
                } else {
                    activityCount = getCountWhereCategory(filteredList, peopleFrom, peopleTo);
                    activityList = getAllWhereCategory(filteredList, peopleFrom, peopleTo, start, total);
                }
            } else {
                activityCount = getCount(peopleFrom, peopleTo);
                activityList = getAll(peopleFrom, peopleTo, start, total);
            }
            int pageCount = activityCount % total == 0 ? activityCount / total
                    : activityCount / total + 1;
            int previousPage = 0;
            if (page > 1)
                previousPage = page - 1;
            int nextPage = 0;
            if (activityCount > total && page < pageCount)
                nextPage = page + 1;
            req.setAttribute("pageCount", pageCount);
            req.setAttribute("previousPage", previousPage);
            req.setAttribute("nextPage", nextPage);
            req.setAttribute("peopleMaxCount", peopleMaxCount);
            req.setAttribute("categories", categoryList);
            req.setAttribute("activities", activityList);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }
}
