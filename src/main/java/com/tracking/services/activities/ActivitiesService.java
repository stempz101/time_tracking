package com.tracking.services.activities;

import com.tracking.dao.ActivityDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.lang.Language;
import com.tracking.models.Activity;
import com.tracking.models.Category;
import com.tracking.services.Service;
import com.tracking.services.categories.CategoriesService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Arrays;
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

    public void processActivities(HttpServletRequest req) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        CategoriesService categoriesService = new CategoriesService();

        List<Integer> filteredList;
        List<Category> categoryList;
        List<Activity> activityList;
        try {
            int start = 1;
            int page = 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page > 1)
                    start = start + TOTAL_ACTIVITIES * (page - 1);
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
                        activityCount = activityDAO.getCountWhereLikeAndCategoryIsNull(searchQuery, peopleFrom, peopleTo);
                        if (req.getParameter("sort") != null && !req.getParameter("sort").isEmpty()) {
                            String sort = req.getParameter("sort");
                            String order = req.getParameter("order");
                            if (order == null || order.isEmpty())
                                order = "asc";
                            activityList = activityDAO.getAllLikeAndWhereCategoryIsNullOrder(searchQuery, sort, order,
                                    peopleFrom, peopleTo, start, TOTAL_ACTIVITIES);
                        }
                        activityList = activityDAO.getAllLikeAndWhereCategoryIsNull(searchQuery, peopleFrom, peopleTo,
                                start, TOTAL_ACTIVITIES);
                    } else {
                        activityCount = activityDAO.getCountWhereLikeAndCategory(searchQuery, filteredList, peopleFrom, peopleTo);
                        if (req.getParameter("sort") != null && !req.getParameter("sort").isEmpty()) {
                            String sort = req.getParameter("sort");
                            String order = req.getParameter("order");
                            if (order == null || order.isEmpty())
                                order = "asc";
                            activityList = activityDAO.getAllLikeAndWhereCategoryOrder(searchQuery, filteredList, sort,
                                    order, peopleFrom, peopleTo, start, TOTAL_ACTIVITIES);
                        }
                        activityList = activityDAO.getAllLikeAndWhereCategory(searchQuery, filteredList, peopleFrom,
                                peopleTo, start, TOTAL_ACTIVITIES);
                    }
                } else {
                    activityCount = activityDAO.getCountWhereLike(searchQuery, peopleFrom, peopleTo);
                    if (req.getParameter("sort") != null && !req.getParameter("sort").isEmpty()) {
                        String sort = req.getParameter("sort");
                        String order = req.getParameter("order");
                        if (order == null || order.isEmpty())
                            order = "asc";
                        activityList = activityDAO.getAllLikeOrder(searchQuery, sort, order, peopleFrom,
                                peopleTo, start, TOTAL_ACTIVITIES);
                    }
                    activityList = activityDAO.getAllLike(searchQuery, peopleFrom, peopleTo, start, TOTAL_ACTIVITIES);
                }
            } else if (req.getParameterValues("filter") != null) {
                filteredList = Arrays.stream(req.getParameterValues("filter"))
                        .map(Integer::parseInt)
                        .toList();
                req.setAttribute("filterCategories", filteredList);
                if (filteredList.size() == 1 && filteredList.contains(0)) {
                    activityCount = activityDAO.getCountWhereCategoryIsNull(peopleFrom, peopleTo);
                    if (req.getParameter("sort") != null && !req.getParameter("sort").isEmpty()) {
                        String sort = req.getParameter("sort");
                        String order = req.getParameter("order");
                        if (order == null || order.isEmpty())
                            order = "asc";
                        activityList = activityDAO.getAllWhereCategoryIsNullOrder(sort, order, peopleFrom,
                                peopleTo, start, TOTAL_ACTIVITIES);
                    } else {
                        activityList = activityDAO.getAllWhereCategoryIsNull(peopleFrom, peopleTo,start, TOTAL_ACTIVITIES);
                    }
                } else {
                    activityCount = activityDAO.getCountWhereCategory(filteredList, peopleFrom, peopleTo);
                    if (req.getParameter("sort") != null && !req.getParameter("sort").isEmpty()) {
                        String sort = req.getParameter("sort");
                        String order = req.getParameter("order");
                        if (order == null || order.isEmpty())
                            order = "asc";
                        activityList = activityDAO.getAllWhereCategoryOrder(filteredList, sort, order, peopleFrom,
                                peopleTo, start, TOTAL_ACTIVITIES);
                    } else {
                        activityList = activityDAO.getAllWhereCategory(filteredList, peopleFrom, peopleTo, start, TOTAL_ACTIVITIES);
                    }
                }
            } else if (req.getParameter("sort") != null && !req.getParameter("sort").isEmpty()) {
                String sort = req.getParameter("sort");
                String order = req.getParameter("order");
                if (order == null || order.isEmpty())
                    order = "asc";
                activityCount = activityDAO.getCount(peopleFrom, peopleTo);
                activityList = activityDAO.getAllOrder(sort, order, peopleFrom, peopleTo, start, TOTAL_ACTIVITIES);
            } else {
                activityCount = activityDAO.getCount(peopleFrom, peopleTo);
                activityList = activityDAO.getAll(peopleFrom, peopleTo, start, TOTAL_ACTIVITIES);
            }
            int pageCount = getPageCount(activityCount, TOTAL_ACTIVITIES);
            int previousPage = 0;
            if (page > 1)
                previousPage = page - 1;
            int nextPage = 0;
            if (activityCount > TOTAL_ACTIVITIES && page < pageCount)
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
