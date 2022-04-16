package com.tracking.dao;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.dao.mysql.MysqlActivityDAO;
import com.tracking.models.Activity;
import com.tracking.models.User;
import com.tracking.models.UserActivity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class MysqlActivityDAOTest {

    @InjectMocks private MysqlActivityDAO activityDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllTest() throws DBException {
        List<Activity> expectedList = new ArrayList<>();

        List<Integer> categoryList = new ArrayList<>();
        categoryList.add(9);
        categoryList.add(14);
        categoryList.add(12);
        categoryList.add(32);
        expectedList.add(new Activity(109, "Test Remove", categoryList, "Lorem ipsum dolor sit amet, consectetur adipisicing elit. A adipisci assumenda consequuntur dignissimos distinctio eaque eligendi enim et excepturi hic ipsum modi nam nemo, nostrum placeat qui quo ratione voluptatibus.",
                "1650053008673.png", 1, 46, new Date(1650053008000L), Activity.Status.BY_USER));
        expectedList.add(new Activity(91, "test", new ArrayList<>(), "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias aut dolore eius natus officia. Ab accusamus accusantium adipisci, assumenda autem consectetur ea, et magni maxime obcaecati, perferendis qui ratione tempore.",
                "1649366160603.png", 0, 19, new Date(1649105596000L), Activity.Status.BY_ADMIN));
        categoryList = new ArrayList<>();
        categoryList.add(14);
        expectedList.add(new Activity(90, "Bla bla", categoryList, "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias aut dolore eius natus officia. Ab accusamus accusantium adipisci, assumenda autem consectetur ea, et magni maxime obcaecati, perferendis qui ratione tempore.",
                null, 0, 19, new Date(1649105578000L), Activity.Status.BY_ADMIN));

        User authUser = getAdmin();
        List<Activity> activityList = activityDAO.getAll("create_time", "desc", 0, 7,
                1, 3, authUser);
        assertNotNull(activityList);
        assertFalse(activityList.isEmpty());
        assertEquals(expectedList, activityList);
    }

    @Test
    public void getAllLike() throws DBException {
        String search = "test";
        List<Activity> expectedList = new ArrayList<>();

        List<Integer> categoryList = new ArrayList<>();
        categoryList.add(14);
        expectedList.add(new Activity(89, "Test test", categoryList, "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias aut dolore eius natus officia. Ab accusamus accusantium adipisci, assumenda autem consectetur ea, et magni maxime obcaecati, perferendis qui ratione tempore.",
                "1649366205058.jpg", 0, 19, new Date(1649101396000L), Activity.Status.BY_ADMIN));
        categoryList = new ArrayList<>();
        categoryList.add(9);
        categoryList.add(14);
        categoryList.add(12);
        categoryList.add(32);
        expectedList.add(new Activity(109, "Test Remove", categoryList, "Lorem ipsum dolor sit amet, consectetur adipisicing elit. A adipisci assumenda consequuntur dignissimos distinctio eaque eligendi enim et excepturi hic ipsum modi nam nemo, nostrum placeat qui quo ratione voluptatibus.",
                "1650053008673.png", 1, 46, new Date(1650053008000L), Activity.Status.BY_USER));
        categoryList = new ArrayList<>();
        categoryList.add(32);
        expectedList.add(new Activity(85, "Test 6", categoryList, "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Expedita, laborum, magni. Ab accusamus aliquam animi asperiores, commodi debitis expedita fugit id ipsum necessitatibus quis sapiente sequi vel veniam vero vitae.",
                "1649100262779.jpg", 0, 19, new Date(1649100262000L), Activity.Status.BY_ADMIN));

        User user = getAdmin();
        List<Activity> activityList = activityDAO.getAllLike(search, "name", "desc", 0, 7,
                1, 3, user);
        assertNotNull(activityList);
        assertFalse(activityList.isEmpty());
        assertEquals(expectedList, activityList);
    }

    @Test
    public void getAllWhereCategoryTest() throws DBException {
        List<Integer> selectedCategoriesList = new ArrayList<>();
        selectedCategoriesList.add(9);

        List<Activity> expectedList = new ArrayList<>();
        List<Integer> categoryList = new ArrayList<>();
        categoryList.add(9);
        categoryList.add(14);
        expectedList.add(new Activity(78, "Test 1", categoryList, "Lorem ipsum dolor sit amet, consectetur adipisicing elit. A adipisci assumenda consequuntur dignissimos distinctio eaque eligendi enim et excepturi hic ipsum modi nam nemo, nostrum placeat qui quo ratione voluptatibus.",
                "1649366174453.jpg", 7, 19, new Date(1648922737000L), Activity.Status.BY_ADMIN));
        categoryList = new ArrayList<>();
        categoryList.add(9);
        categoryList.add(14);
        categoryList.add(12);
        categoryList.add(32);
        expectedList.add(new Activity(109, "Test Remove", categoryList, "Lorem ipsum dolor sit amet, consectetur adipisicing elit. A adipisci assumenda consequuntur dignissimos distinctio eaque eligendi enim et excepturi hic ipsum modi nam nemo, nostrum placeat qui quo ratione voluptatibus.",
                "1650053008673.png", 1, 46, new Date(1650053008000L), Activity.Status.BY_USER));

        User user = getAdmin();
        List<Activity> activityList = activityDAO.getAllWhereCategory(selectedCategoriesList, "name", "asc",
                0, 7, 1, 3, user);
        assertNotNull(activityList);
        assertFalse(activityList.isEmpty());
        assertEquals(expectedList, activityList);
    }

    @Test
    public void getAllWhereCategoryIsNullTest() throws DBException {
        List<Activity> expectedList = new ArrayList<>();
        expectedList.add(new Activity(91, "test", new ArrayList<>(), "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias aut dolore eius natus officia. Ab accusamus accusantium adipisci, assumenda autem consectetur ea, et magni maxime obcaecati, perferendis qui ratione tempore.",
                "1649366160603.png", 0, 19, new Date(1649105596000L), Activity.Status.BY_ADMIN));

        User user = getAdmin();
        List<Activity> activityList = activityDAO.getAllWhereCategoryIsNull("create_time", "asc",
                0, 7, 1, 3, user);
        assertNotNull(activityList);
        assertFalse(activityList.isEmpty());
        assertEquals(expectedList, activityList);
    }

    @Test
    public void getAllLikeAndWhereCategoryTest() throws DBException {
        String search = "test";
        List<Integer> selectedCategoriesList = new ArrayList<>();
        selectedCategoriesList.add(14);

        List<Activity> expectedList = new ArrayList<>();
        List<Integer> categoryList = new ArrayList<>();
        categoryList.add(9);
        categoryList.add(14);
        expectedList.add(new Activity(78, "Test 1", categoryList, "Lorem ipsum dolor sit amet, consectetur adipisicing elit. A adipisci assumenda consequuntur dignissimos distinctio eaque eligendi enim et excepturi hic ipsum modi nam nemo, nostrum placeat qui quo ratione voluptatibus.",
                "1649366174453.jpg", 7, 19, new Date(1648922737000L), Activity.Status.BY_ADMIN));
        categoryList = new ArrayList<>();
        categoryList.add(14);
        expectedList.add(new Activity(79, "Test 2", categoryList, "Lorem ipsum dolor sit amet, consectetur adipisicing elit. A adipisci assumenda consequuntur dignissimos distinctio eaque eligendi enim et excepturi hic ipsum modi nam nemo, nostrum placeat qui quo ratione voluptatibus.",
                "1649366199133.jpg", 4, 19, new Date(1648922810000L), Activity.Status.BY_ADMIN));
        categoryList = new ArrayList<>();
        categoryList.add(9);
        categoryList.add(14);
        categoryList.add(12);
        categoryList.add(32);
        expectedList.add(new Activity(109, "Test Remove", categoryList, "Lorem ipsum dolor sit amet, consectetur adipisicing elit. A adipisci assumenda consequuntur dignissimos distinctio eaque eligendi enim et excepturi hic ipsum modi nam nemo, nostrum placeat qui quo ratione voluptatibus.",
                "1650053008673.png", 1, 46, new Date(1650053008000L), Activity.Status.BY_USER));

        User user = getAdmin();
        List<Activity> activityList = activityDAO.getAllLikeAndWhereCategory(search, selectedCategoriesList,
                "people_count", "desc", 0, 7, 1, 3, user);
        assertNotNull(activityList);
        assertFalse(activityList.isEmpty());
        assertEquals(expectedList, activityList);
    }

    @Test
    public void getAllLikeAndWhereCategoryIsNullTest() throws DBException {
        String search = "test";

        List<Activity> expectedList = new ArrayList<>();
        expectedList.add(new Activity(91, "test", new ArrayList<>(), "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias aut dolore eius natus officia. Ab accusamus accusantium adipisci, assumenda autem consectetur ea, et magni maxime obcaecati, perferendis qui ratione tempore.",
                "1649366160603.png", 0, 19, new Date(1649105596000L), Activity.Status.BY_ADMIN));

        User user = getAdmin();
        List<Activity> activityList = activityDAO.getAllLikeAndWhereCategoryIsNull(search, "name", "asc",
                0, 7, 1, 3, user);
        assertNotNull(activityList);
        assertFalse(activityList.isEmpty());
        assertEquals(expectedList, activityList);
    }

    @Test
    public void getAllCreatedTest() throws DBException {
        List<Activity> expectedList = new ArrayList<>();
        expectedList.add(new Activity(78, "Test 1", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. A adipisci assumenda consequuntur dignissimos distinctio eaque eligendi enim et excepturi hic ipsum modi nam nemo, nostrum placeat qui quo ratione voluptatibus.",
                "1649366174453.jpg", 7, 19, new Date(1648922737000L), Activity.Status.BY_ADMIN));
        expectedList.add(new Activity(79, "Test 2", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. A adipisci assumenda consequuntur dignissimos distinctio eaque eligendi enim et excepturi hic ipsum modi nam nemo, nostrum placeat qui quo ratione voluptatibus.",
                "1649366199133.jpg", 4, 19, new Date(1648922810000L), Activity.Status.BY_ADMIN));
        expectedList.add(new Activity(85, "Test 6", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Expedita, laborum, magni. Ab accusamus aliquam animi asperiores, commodi debitis expedita fugit id ipsum necessitatibus quis sapiente sequi vel veniam vero vitae.",
                "1649100262779.jpg", 0, 19, new Date(1649100262000L), Activity.Status.BY_ADMIN));

        List<Activity> activityList = activityDAO.getAllCreated(19, 1, 3);
        assertNotNull(activityList);
        assertFalse(activityList.isEmpty());
        assertEquals(expectedList, activityList);
    }

    @Test
    public void getAllForProfileTest() throws DBException {
        List<UserActivity> expectedList = new ArrayList<>();
        expectedList.add(new UserActivity(78, "Test 1", 609, UserActivity.Status.STOPPED));
        expectedList.add(new UserActivity(109, "Test Remove", 0, UserActivity.Status.NOT_STARTED));

        List<UserActivity> activityList = activityDAO.getAllForProfile(46, 1, 3);
        assertNotNull(activityList);
        assertFalse(activityList.isEmpty());
        assertEquals(expectedList, activityList);
    }

    @Test
    public void getCountTest() throws DBException {
        int expectedCount = 8;
        User user = getAdmin();
        int count = activityDAO.getCount(0, 7, user);
        assertEquals(expectedCount, count);
    }

    @Test
    public void getCountWhereLikeTest() throws DBException {
        String search = "test";
        int expectedCount = 7;
        User user = getAdmin();
        int count = activityDAO.getCountWhereLike(search, 0, 7, user);
        assertEquals(expectedCount, count);
    }

    @Test
    public void getCountWhereCategoryTest() throws DBException {
        List<Integer> categoryList = new ArrayList<>();
        categoryList.add(14);
        int expectedCount = 6;
        User user = getAdmin();
        int count = activityDAO.getCountWhereCategory(categoryList, 0, 7, user);
        assertEquals(expectedCount, count);
    }

    @Test
    public void getCountWhereCategoryIsNullTest() throws DBException {
        int expectedCount = 1;
        User user = getAdmin();
        int count = activityDAO.getCountWhereCategoryIsNull(0, 7, user);
        assertEquals(expectedCount, count);
    }

    @Test
    public void getCountWhereLikeAndCategoryTest() throws DBException {
        String search = "test";
        List<Integer> categoryList = new ArrayList<>();
        categoryList.add(14);
        int expectedCount = 5;
        User user = getAdmin();
        int count = activityDAO.getCountWhereLikeAndCategory(search, categoryList, 0, 7, user);
        assertEquals(expectedCount, count);
    }

    @Test
    public void getCountWhereLikeAndCategoryIsNullTest() throws DBException {
        String search = "test";
        int expectedCount = 1;
        User user = getAdmin();
        int count = activityDAO.getCountWhereLikeAndCategoryIsNull(search, 0, 7, user);
        assertEquals(expectedCount, count);
    }

    @Test
    public void getCountInActivityTest() throws DBException {
        int activityId = 78;
        int expectedCount = 7;
        int count = activityDAO.getCountInActivity(activityId);
        assertEquals(expectedCount, count);
    }

    @Test
    public void getCountInActivityWhereNameTest() throws DBException {
        int expectedCount = 2;
        int count = activityDAO.getCountInActivityWhereName(78, "k", "ka");
        assertEquals(expectedCount, count);
    }

    @Test
    public void getCreatedCountTest() throws DBException {
        int userId = 19;
        int expectedCount = 7;
        int count = activityDAO.getCreatedCount(userId);
        assertEquals(expectedCount, count);
    }

    @Test
    public void getCountForProfileTest() throws DBException {
        int userId = 46;
        int expectedCount = 2;
        int count = activityDAO.getCountForProfile(userId);
        assertEquals(expectedCount, count);
    }

    @Test
    public void getMaxPeopleCountTest() throws DBException {
        int expectedCount = 7;
        int count = activityDAO.getMaxPeopleCount();
        assertEquals(expectedCount, count);
    }

    @Test
    public void getCreatorTest() throws DBException {
        User expected = new User(46, "Ackerman", "Levi", "1648997092108.jpg",
                false, false);
        User creator = activityDAO.getCreator(109);
        assertNotNull(creator);
        assertEquals(expected, creator);
    }

    @Test
    public void getByIdTest() throws DBException {
        List<Integer> expectedCategories = new ArrayList<>();
        expectedCategories.add(32);
        Activity expected = new Activity(85, "Test 6", expectedCategories, "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Expedita, laborum, magni. Ab accusamus aliquam animi asperiores, commodi debitis expedita fugit id ipsum necessitatibus quis sapiente sequi vel veniam vero vitae.",
                "1649100262779.jpg", 0, 19, new Date(1649100262000L), Activity.Status.BY_ADMIN);
        Activity activity = activityDAO.getById(85);
        assertNotNull(activity);
        assertEquals(expected, activity);
    }

    @Test
    public void createUpdateDelete() throws DBException {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(17);
        categoryIds.add(18);
        categoryIds.add(19);
        Activity activity = new Activity();
        activity.setName("Activity Test");
        activity.setCategories(categoryIds);
        activity.setDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid eos excepturi necessitatibus obcaecati quibusdam. Animi dolorum eaque earum fuga necessitatibus quasi, sapiente veritatis. Cum nostrum quam quis sunt tenetur ut?");
        activity.setImage("filename.png");
        activity.setCreatorId(48);
        activity.setStatus(Activity.Status.BY_ADMIN);

        Activity createdActivity = activityDAO.create(activity);
        assertNotNull(createdActivity);
        assertEquals(activity.getName(), createdActivity.getName());
        assertEquals(activity.getCategories(), createdActivity.getCategories());
        assertEquals(activity.getDescription(), createdActivity.getDescription());
        assertEquals(activity.getImage(), createdActivity.getImage());
        assertEquals(activity.getCreatorId(), createdActivity.getCreatorId());
        assertEquals(activity.getStatus(), createdActivity.getStatus());

        categoryIds = new ArrayList<>();
        categoryIds.add(17);
        categoryIds.add(18);
        activity.setId(createdActivity.getId());
        activity.setName("Activity Test");
        activity.setCategories(categoryIds);
        activity.setDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid eos excepturi necessitatibus obcaecati quibusdam. Animi dolorum eaque earum fuga necessitatibus quasi, sapiente veritatis. Cum nostrum quam quis sunt tenetur ut?");
        activity.setImage("filename.png");
        assertTrue(activityDAO.update(activity));

        Activity updatedActivity = activityDAO.getById(activity.getId());
        assertEquals(activity.getName(), updatedActivity.getName());
        assertEquals(activity.getCategories(), updatedActivity.getCategories());
        assertEquals(activity.getDescription(), updatedActivity.getDescription());
        assertEquals(activity.getImage(), updatedActivity.getImage());

        assertTrue(activityDAO.delete(activity.getId()));
    }

    private User getAdmin() {
        User admin = new User();
        admin.setId(19);
        admin.setLastName("Adminov");
        admin.setFirstName("Adam");
        admin.setImage("1648932072391.jpg");
        admin.setActivityCount(0);
        admin.setSpentTime(0);
        admin.setAdmin(true);
        admin.setBlocked(false);
        return admin;
    }
}
