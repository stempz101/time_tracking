package com.tracking.dao;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.dao.mysql.MysqlActivityDAO;
import com.tracking.dao.mysql.MysqlUserDAO;
import com.tracking.models.User;
import com.tracking.models.UserActivity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class MysqlUserDAOTest {

    @InjectMocks private MysqlUserDAO userDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void ifExistsTest() throws DBException {
        String email = "zxc@zxc.com";
        assertTrue(userDAO.ifExists(email));
    }

    @Test
    public void checkPasswordTest() {
        User user = new User();
        user.setPassword("$2a$10$P/5qZH7vHKhUgMJnUPhRGuWo.emoVjaAd1DhN56c/Wbq6ae1P2j7q");
        assertTrue(userDAO.checkPassword(user, "qwerty123"));
    }

    @Test
    public void getAllTest() throws DBException {
        List<User> expected = new ArrayList<>();
        expected.add(new User(26, "Yeager", "Eren", "eren@qwe.com",
                "$2a$10$jmh7fouzVu8wa.X2UzIH7eaHarUOdbk0aKA71K8L3zwIYZVFKHVHS",
                "1649358134761.jpg", 0, 0, false, false));
        expected.add(new User(21, "Wedasd", "Wudu", "wuduwed@qwe.com",
                "$2a$10$ivYK1CoDB2fAQy.g0MkOL.zUqMJ9hNugJIuxi9HXAPpbosZUpwtA2",
                null, 0, 0, false, false));
        expected.add(new User(22, "Warlord", "Troll", "troll@qwe.com",
                "$2a$10$Rfjl2K2MPkERJ5CihtXYTepISGJq3fA75m5DFKCFXTudoRT6Gc3lK",
                "1649358432947.jpg", 0, 0, false, false));

        List<User> userList = userDAO.getAll("last_name", "desc", 1, 3);
        assertNotNull(userList);
        assertFalse(userList.isEmpty());
        assertEquals(expected, userList);
    }

    @Test
    public void getAllWhereInActivityTest() throws DBException {
        List<UserActivity> expected = new ArrayList<>();
        expected.add(new UserActivity(23, "Crystal", "Maiden", "1649357892725.png",
                false, new Date(1649363907000L), new Date(1649405835000L), 41927, UserActivity.Status.STOPPED));
        expected.add(new UserActivity(47, "Assa", "Adas", null, false,
                new Date(1649363871000L), new Date(1649367249000L), 3378, UserActivity.Status.STOPPED));
        expected.add(new UserActivity(46, "Ackerman", "Levi", "1648997092108.jpg",
                false, new Date(1649017140000L), new Date(1649017747000L), 609, UserActivity.Status.STOPPED));

        List<UserActivity> userList = userDAO.getAllWhereInActivity(78, "spent_time", "desc", 1, 3);
        assertNotNull(userList);
        assertFalse(userList.isEmpty());
        assertEquals(expected, userList);
    }

    @Test
    public void getAllInActivityWhereNameTest() throws DBException {
        List<UserActivity> expected = new ArrayList<>();
        expected.add(new UserActivity(18, "Ken", "Kaneki", null, false,
                null, null, 0, UserActivity.Status.NOT_STARTED));
        expected.add(new UserActivity(45, "Kit", "Kat", null, false,
                new Date(1649363979000L), null, 0, UserActivity.Status.STARTED));

        List<UserActivity> userList = userDAO.getAllInActivityWhereName(78, "k", "ka",
                "last_name", "asc", 1, 3);
        assertNotNull(userList);
        assertFalse(userList.isEmpty());
        assertEquals(expected, userList);
    }

    @Test
    public void getAllWhereNotInActivityTest() throws DBException {
        List<User> expected = new ArrayList<>();
        expected.add(new User(24, "Test", "Avatar", "test@test.com",
                "$2a$10$0gugiq5LIJZrVg7XbsxMl.yHrymNtjU.JFJBh/Rhijk7a.C5PAB9u",
                null, 0, 0, false, false));
        expected.add(new User(29, "Test", "Qwe", "test1@qwe.com",
                "$2a$10$Lotf03PsnlGUer46AYVkfulINCYlnE9SXDfKQBxXTzN3TyDu3SmEu",
                null, 0, 0, false, false));
        expected.add(new User(30, "Test", "Asd", "test2@qwe.com",
                "$2a$10$4aCSkh8IyryZ3LmQ7p2bBut.HIkWaHFC1VC3R.0hGtMAWvTYpJpnC",
                null, 0, 0, false, false));

        List<User> userList = userDAO.getAllWhereNotInActivity(78);
        assertNotNull(userList);
        assertFalse(userList.isEmpty());
        userList = userList.stream().limit(3).toList();
        assertEquals(expected, userList);
    }

    @Test
    public void getWhereInActivityTest() throws DBException {
        UserActivity expected = new UserActivity();
        expected.setUserId(47);
        expected.setUserLastName("Assa");
        expected.setUserFirstName("Adas");
        expected.setStartTime(new Date(1649363859000L));
        expected.setStopTime(new Date(1649406218000L));
        expected.setSpentTime(42359);
        expected.setStatus(UserActivity.Status.STOPPED);

        User user = new User();
        user.setId(47);
        UserActivity userInfo = userDAO.getWhereInActivity(user, 79);
        assertNotNull(userInfo);
        assertEquals(expected, userInfo);
    }

    @Test
    public void getAllWhereNameTest() throws DBException {
        List<User> expected = new ArrayList<>();
        expected.add(new User(30, "Test", "Asd", "test2@qwe.com",
                "$2a$10$4aCSkh8IyryZ3LmQ7p2bBut.HIkWaHFC1VC3R.0hGtMAWvTYpJpnC",
                null, 0, 0, false, false));
        expected.add(new User(34, "Test", "Asdasdas", "test6@qwe.com",
                "$2a$10$fQph6S0yAeYihh22Uj28juchaVqL5yrZiBw7pdE.Ev3jtzM64qNG.",
                null, 0, 0, false, false));
        expected.add(new User(37, "Test", "Asd", "test9@qwe.com",
                "$2a$10$urKFLyWqAqjGntaFNziET.7SrkxF6rBvu0K38LYdYHo66cFiMNEJe",
                null, 0, 0, false, false));
        expected.add(new User(38, "Test", "Asddds", "test10@qwe.com",
                "$2a$10$rTfrK81d0BPgOyhhnGXQdu/DRgoICOBjTuHnMfGBldH15uSBQcA7G",
                null, 0, 0, false, false));

        List<User> userList = userDAO.getAllWhereName("Test", "as", "id",
                "asc", 1, 10);
        assertNotNull(userList);
        assertFalse(userList.isEmpty());
        assertEquals(expected, userList);
    }

    @Test
    public void getByIdTest() throws DBException {
        User expected = new User(47, "Assa", "Adas", "asdfasd@qwe.com",
                "$2a$10$HefYR9IURHMV1Jcjoqv61OUh/PPXR8Swk82.z2vGiyrviQ2KcSk3W",
                null, 2, 45737, false, false);
        User user = userDAO.getById(47);
        assertNotNull(user);
        assertEquals(expected, user);
    }

    @Test
    public void getByEmailTest() throws DBException {
        User expected = new User(47, "Assa", "Adas", "asdfasd@qwe.com",
                "$2a$10$HefYR9IURHMV1Jcjoqv61OUh/PPXR8Swk82.z2vGiyrviQ2KcSk3W",
                null, 2, 45737, false, false);
        User user = userDAO.getByEmail("asdfasd@qwe.com");
        assertNotNull(user);
        assertEquals(expected, user);
    }

    @Test
    public void getCountTest() throws DBException {
        int expected = 24;
        int count = userDAO.getCount();
        assertEquals(expected, count);
    }

    @Test
    public void getCountWhereNameTest() throws DBException {
        int expected = 12;
        int count = userDAO.getCountWhereName("test", null);
        assertEquals(expected, count);
    }

    @Test
    public void createUpdateDeleteTest() throws DBException {
        User expected = new User("Testing", "Guy", "man@qwe.com", "zxcvb123", "filename.jpg");
        User createdUser = userDAO.create("Testing", "Guy", "man@qwe.com", "zxcvb123",
                "zxcvb123", "filename.jpg", false);
        assertNotNull(createdUser);
        assertEquals(expected.getLastName(), createdUser.getLastName());
        assertEquals(expected.getFirstName(), createdUser.getFirstName());
        assertEquals(expected.getEmail(), createdUser.getEmail());
        assertTrue(BCrypt.checkpw(expected.getPassword(), createdUser.getPassword()));
        assertEquals(expected.getImage(), createdUser.getImage());
        assertFalse(createdUser.isAdmin());

        String lastName = "Test";
        String firstName = "Goose";
        String email = "goose@qwe.com";
        assertTrue(userDAO.updateProfile(createdUser.getId(), lastName, firstName, email));
        createdUser = userDAO.getById(createdUser.getId());
        assertEquals(lastName, createdUser.getLastName());
        assertEquals(firstName, createdUser.getFirstName());
        assertEquals(email, createdUser.getEmail());

        String image = "image1.png";
        assertTrue(userDAO.updatePhoto(createdUser.getId(), image));
        createdUser = userDAO.getById(createdUser.getId());
        assertEquals(image, createdUser.getImage());

        String currentPassword = "zxcvb123";
        String newPassword = "newpass123";
        assertTrue(userDAO.updatePassword(createdUser.getId(), currentPassword, newPassword, newPassword));
        createdUser = userDAO.getById(createdUser.getId());
        assertTrue(BCrypt.checkpw(newPassword, createdUser.getPassword()));

        assertTrue(userDAO.delete(createdUser));
        assertNull(userDAO.getById(createdUser.getId()));
    }

    @Test
    public void startStopTimeTest() throws DBException {
        int activityId = 72;
        User user = new User();
        user.setId(46);
        UserActivity userInfo = userDAO.getWhereInActivity(user, activityId);
        double spentTime = userInfo.getSpentTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm");

        String startTime = sdf.format(new Date());
        userDAO.startTime(activityId, user.getId());
        userInfo = userDAO.getWhereInActivity(user, activityId);
        assertNotNull(userInfo);
        assertEquals(startTime, sdf.format(userInfo.getStartTime()));

        String stopTime = sdf.format(new Date());
        userDAO.stopTime(activityId, user.getId());
        userInfo = userDAO.getWhereInActivity(user, activityId);
        assertNotNull(userInfo);
        assertEquals(stopTime, sdf.format(userInfo.getStopTime()));
        double spentTimeAfterStop = Math.round((double) (userInfo.getStopTime().getTime() - userInfo.getStartTime().getTime())
                / 3600.0 * 10.0) / 10.0;
        assertEquals(spentTime + spentTimeAfterStop, userInfo.getSpentTime(), 0.001);
    }

    @Test
    public void setBlockTest() throws DBException {
        int userId = 36;
        userDAO.setBlock(userId, true);
        User user = userDAO.getById(userId);
        assertTrue(user.isBlocked());
        userDAO.setBlock(userId, false);
        user = userDAO.getById(userId);
        assertFalse(user.isBlocked());
    }

}
