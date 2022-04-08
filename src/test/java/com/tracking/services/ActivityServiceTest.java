package com.tracking.services;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.activities.ActivityService;
import com.tracking.models.Activity;
import com.tracking.models.Category;
import com.tracking.models.User;
import com.tracking.models.UserActivity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class ActivityServiceTest {

    @InjectMocks private ActivityService activityService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getTest() throws ServiceException {
        int activityId = 87;
        long time = 1649101093000L;
        Activity expectedActivity = new Activity();
        expectedActivity.setId(activityId);
        expectedActivity.setName("Test 10");
        expectedActivity.setDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias aut dolore eius natus officia. Ab accusamus accusantium adipisci, assumenda autem consectetur ea, et magni maxime obcaecati, perferendis qui ratione tempore.");
        expectedActivity.setImage("1649101093762.png");
        expectedActivity.setPeopleCount(0);
        expectedActivity.setCreatorId(19);
        expectedActivity.setByAdmin(true);
        expectedActivity.setCreateTime(new Date(time));
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(14);
        expectedActivity.setCategories(categoryIds);
        Activity activity = activityService.get(87);
        assertNotNull(activity);
        assertEquals(expectedActivity, activity);
    }

    @Test
    public void getActivityCategoriesTest() throws ServiceException {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(9);
        categoryIds.add(14);

        List<Category> expectedList = new ArrayList<>();
        expectedList.add(new Category(9, "Cinema", "Кіно"));
        expectedList.add(new Category(14, "Cooking", "Кулінарія"));

        List<Category> categoryList = activityService.getActivityCategories(categoryIds, new Locale("en", "EN"));
        assertNotNull(categoryList);
        assertFalse(categoryIds.isEmpty());
        assertEquals(expectedList, categoryList);
    }

    @Test
    public void getCreatorTest() throws ServiceException {
        int activityId = 72;
        User expectedCreator = new User();
        expectedCreator.setId(46);
        expectedCreator.setLastName("Ackerman");
        expectedCreator.setFirstName("Levi");
        expectedCreator.setImage("1648997092108.jpg");
        expectedCreator.setAdmin(false);
        expectedCreator.setBlocked(false);

        User creator = activityService.getCreator(activityId);
        assertNotNull(creator);
        assertEquals(expectedCreator, creator);
    }

    @Test
    public void getUserInfoTest() throws ServiceException {
        int activityId = 78;
        User user = new User();
        user.setId(46);
        user.setLastName("Ackerman");
        user.setFirstName("Levi");
        user.setImage("1648997092108.jpg");
        user.setAdmin(false);

        UserActivity expectedUserActivity = new UserActivity();
        expectedUserActivity.setUserId(user.getId());
        expectedUserActivity.setUserLastName(user.getLastName());
        expectedUserActivity.setUserFirstName(user.getFirstName());
        expectedUserActivity.setUserImage(user.getImage());
        expectedUserActivity.setAdmin(user.isAdmin());
        expectedUserActivity.setStartTime(new Date(1649017140000L));
        expectedUserActivity.setStopTime(new Date(1649017747000L));
        expectedUserActivity.setSpentTime(609);
        expectedUserActivity.setStatus(UserActivity.Status.STOPPED);

        UserActivity userActivity = activityService.getUserInfo(user, activityId);
        assertNotNull(userActivity);
        assertEquals(expectedUserActivity, userActivity);
    }
}
