package com.tracking.controllers.services.activities;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.LoginService;
import com.tracking.dao.ActivityDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.models.Activity;
import com.tracking.models.Category;
import com.tracking.models.User;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.categories.CategoriesService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Service, that contains all actions with activities
 */
public class ActivitiesService extends Service {

    private static final Logger logger = Logger.getLogger(ActivitiesService.class);

    /**
     * Getting the maximum current count of people in one activity among activities
     * @return max count of people in one activity among activities
     * @throws ServiceException if something went wrong while executing
     */
    public int getMaxPeopleCount() throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            return activityDAO.getMaxPeopleCount();
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivitiesService: getMaxPeopleCount was failed", e);
        }
    }

    /**
     * Getting all essential for Activities page
     * @param req for getting session to get/set attributes, getting parameters
     * @param resp for sending redirect
     * @return true if process of activities was successful, false if request parameter "page" equal or less than 0 and greater than assigned page count
     * @throws ServiceException if something went wrong while executing
     * @throws IOException if something went wrong while redirecting
     */
    public boolean processActivities(HttpServletRequest req, HttpServletResponse resp) throws ServiceException, IOException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        CategoriesService categoriesService = new CategoriesService();

        List<Integer> filteredList;
        List<Category> categoryList;
        List<Activity> activityList;
        try {
            User authUser = (User) req.getSession().getAttribute("authUser");

            int start = 1;
            int page = 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page <= 0) {
                    redirectWithoutPage(req, resp, "activities", authUser);
                    return false;
                }
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
//            Locale locale = Service.getLocale(req);
            categoryList = categoriesService.getAllCategories(Service.getLocale((String) req.getSession().getAttribute("lang")));
            categoryList.add(new Category(0, "Other", "Інше"));

            String sort = req.getParameter("sort");
            String order = req.getParameter("order");
            if (order == null || order.isEmpty())
                order = "asc";
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
                        activityCount = activityDAO.getCountWhereLikeAndCategoryIsNull(searchQuery, peopleFrom, peopleTo, authUser);
                        activityList = activityDAO.getAllLikeAndWhereCategoryIsNull(searchQuery, sort, order,
                                peopleFrom, peopleTo, start, TOTAL_ACTIVITIES, authUser);
                    } else {
                        activityCount = activityDAO.getCountWhereLikeAndCategory(searchQuery, filteredList, peopleFrom, peopleTo, authUser);
                        activityList = activityDAO.getAllLikeAndWhereCategory(searchQuery, filteredList, sort,
                                order, peopleFrom, peopleTo, start, TOTAL_ACTIVITIES, authUser);
                    }
                } else {
                    activityCount = activityDAO.getCountWhereLike(searchQuery, peopleFrom, peopleTo, authUser);
                    activityList = activityDAO.getAllLike(searchQuery, sort, order, peopleFrom,
                            peopleTo, start, TOTAL_ACTIVITIES, authUser);
                }
            } else if (req.getParameterValues("filter") != null) {
                filteredList = Arrays.stream(req.getParameterValues("filter"))
                        .map(Integer::parseInt)
                        .toList();
                req.setAttribute("filterCategories", filteredList);
                if (filteredList.size() == 1 && filteredList.contains(0)) {
                    activityCount = activityDAO.getCountWhereCategoryIsNull(peopleFrom, peopleTo, authUser);
                    activityList = activityDAO.getAllWhereCategoryIsNull(sort, order, peopleFrom, peopleTo, start, TOTAL_ACTIVITIES, authUser);
                } else {
                    activityCount = activityDAO.getCountWhereCategory(filteredList, peopleFrom, peopleTo, authUser);
                    activityList = activityDAO.getAllWhereCategory(filteredList, sort, order, peopleFrom, peopleTo, start, TOTAL_ACTIVITIES, authUser);
                }
            } else {
                activityCount = activityDAO.getCount(peopleFrom, peopleTo, authUser);
                activityList = activityDAO.getAll(sort, order, peopleFrom, peopleTo, start, TOTAL_ACTIVITIES, authUser);
            }

            int pageCount = getPageCount(activityCount, TOTAL_ACTIVITIES);
            if (pageCount > 0 && page > pageCount) {
                redirectWithoutPage(req, resp, "activities", authUser);
                return false;
            }

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

            return true;
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivitiesService: processActivities was failed", e);
        }
    }
}
