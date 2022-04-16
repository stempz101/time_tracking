package com.tracking.dao;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.dao.mysql.MysqlActivityDAO;
import com.tracking.dao.mysql.MysqlRequestDAO;
import com.tracking.models.Activity;
import com.tracking.models.Request;
import com.tracking.models.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class MysqlRequestDAOTest {

    @InjectMocks private MysqlRequestDAO requestDAO;
    @InjectMocks private MysqlActivityDAO activityDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllTest() throws DBException {
        List<Request> expectedList = new ArrayList<>();
        expectedList.add(new Request(52, 109, Request.Status.CONFIRMED, false,
                new Date(1650053008000L), new Activity(109, "Test Remove", Activity.Status.BY_USER),
                new User(46, "Ackerman", "Levi", "1648997092108.jpg")));
        expectedList.add(new Request(51, 108, Request.Status.DECLINED, false,
                new Date(1650045913000L), new Activity(108, "Test Add Decline", Activity.Status.ADD_DECLINED),
                new User(46, "Ackerman", "Levi", "1648997092108.jpg")));
        expectedList.add(new Request(43, 95, Request.Status.WAITING, false,
                new Date(1649358471000L), new Activity(95, "Design Axe", Activity.Status.ADD_WAITING),
                new User(22, "Warlord", "Troll", "1649358432947.jpg")));

        User user = getAdmin();
        List<Request> requestList = requestDAO.getAll("create_time", "desc", 1, 3, user);
        assertNotNull(requestList);
        assertFalse(requestList.isEmpty());
        assertEquals(expectedList, requestList);
    }

    @Test
    public void getTest() throws DBException {
        List<Integer> categoryList = new ArrayList<>();
        categoryList.add(9);
        categoryList.add(14);
        categoryList.add(12);
        categoryList.add(32);

        Activity activity = new Activity();
        activity.setId(109);
        activity.setName("Test Remove");
        activity.setCategories(categoryList);
        activity.setDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit. A adipisci assumenda consequuntur dignissimos distinctio eaque eligendi enim et excepturi hic ipsum modi nam nemo, nostrum placeat qui quo ratione voluptatibus.");
        activity.setImage("1650053008673.png");
        activity.setCreatorId(46);
        activity.setStatus(Activity.Status.BY_USER);

        Request expected = new Request();
        expected.setId(52);
        expected.setActivityId(109);
        expected.setStatus(Request.Status.CONFIRMED);
        expected.setForDelete(false);
        expected.setActivity(activity);

        Request request = requestDAO.get(52);
        assertNotNull(request);
        assertEquals(expected, request);
    }

    @Test
    public void createForAddConfirmAddTest() throws DBException {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(17);
        categoryIds.add(18);
        categoryIds.add(19);
        Activity activity = new Activity();
        activity.setName("Activity Test");
        activity.setCategories(categoryIds);
        activity.setDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid eos excepturi necessitatibus obcaecati quibusdam. Animi dolorum eaque earum fuga necessitatibus quasi, sapiente veritatis. Cum nostrum quam quis sunt tenetur ut?");
        activity.setImage("filename.png");
        activity.setCreatorId(47);
        activity.setStatus(Activity.Status.ADD_WAITING);

        Request request = new Request();
        request.setStatus(Request.Status.WAITING);
        request.setActivity(activity);
        request.setForDelete(false);
        Request createdRequest = requestDAO.create(activity, false);
        assertNotNull(createdRequest);
        assertEquals(request.getActivity(), createdRequest.getActivity());
        assertEquals(request.getStatus(), createdRequest.getStatus());
        assertEquals(request.isForDelete(), createdRequest.isForDelete());

        request = requestDAO.get(createdRequest.getId());
        assertNotNull(request);

        requestDAO.confirmAdd(request.getId(), request.getActivityId(),
                request.getActivity().getCreatorId());
        request = requestDAO.get(createdRequest.getId());
        assertEquals(Request.Status.CONFIRMED, request.getStatus());
        assertEquals(Activity.Status.BY_USER, request.getActivity().getStatus());

        assertTrue(activityDAO.delete(request.getActivity().getId()));
    }

    @Test
    public void createForAddConfirmRemoveTest() throws DBException {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(17);
        categoryIds.add(18);
        categoryIds.add(19);
        Activity activity = new Activity();
        activity.setName("Activity Test");
        activity.setCategories(categoryIds);
        activity.setDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid eos excepturi necessitatibus obcaecati quibusdam. Animi dolorum eaque earum fuga necessitatibus quasi, sapiente veritatis. Cum nostrum quam quis sunt tenetur ut?");
        activity.setImage("filename.png");
        activity.setCreatorId(47);
        activity.setStatus(Activity.Status.ADD_WAITING);

        Request request = new Request();
        request.setStatus(Request.Status.WAITING);
        request.setActivity(activity);
        request.setForDelete(false);
        Request createdRequest = requestDAO.create(activity, false);
        assertNotNull(createdRequest);
        assertEquals(request.getActivity(), createdRequest.getActivity());
        assertEquals(request.getStatus(), createdRequest.getStatus());
        assertEquals(request.isForDelete(), createdRequest.isForDelete());

        request = requestDAO.get(createdRequest.getId());
        assertNotNull(request);

        requestDAO.confirmRemove(request.getId(), request.getActivityId());
        request = requestDAO.get(createdRequest.getId());
        assertEquals(Request.Status.CONFIRMED, request.getStatus());
        assertEquals(Activity.Status.DEL_CONFIRMED, request.getActivity().getStatus());

        assertTrue(activityDAO.delete(request.getActivity().getId()));
    }

    @Test
    public void createForAddDeclineAddTest() throws DBException {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(17);
        categoryIds.add(18);
        categoryIds.add(19);
        Activity activity = new Activity();
        activity.setName("Activity Test");
        activity.setCategories(categoryIds);
        activity.setDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid eos excepturi necessitatibus obcaecati quibusdam. Animi dolorum eaque earum fuga necessitatibus quasi, sapiente veritatis. Cum nostrum quam quis sunt tenetur ut?");
        activity.setImage("filename.png");
        activity.setCreatorId(47);
        activity.setStatus(Activity.Status.ADD_WAITING);

        Request request = new Request();
        request.setStatus(Request.Status.WAITING);
        request.setActivity(activity);
        request.setForDelete(false);
        Request createdRequest = requestDAO.create(activity, false);
        assertNotNull(createdRequest);
        assertEquals(request.getActivity(), createdRequest.getActivity());
        assertEquals(request.getStatus(), createdRequest.getStatus());
        assertEquals(request.isForDelete(), createdRequest.isForDelete());

        request = requestDAO.get(createdRequest.getId());
        assertNotNull(request);

        requestDAO.declineAdd(request.getId(), request.getActivityId());
        request = requestDAO.get(createdRequest.getId());
        assertEquals(Request.Status.DECLINED, request.getStatus());
        assertEquals(Activity.Status.ADD_DECLINED, request.getActivity().getStatus());

        assertTrue(activityDAO.delete(request.getActivity().getId()));
    }

    @Test
    public void createForAddDeclineRemoveTest() throws DBException {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(17);
        categoryIds.add(18);
        categoryIds.add(19);
        Activity activity = new Activity();
        activity.setName("Activity Test");
        activity.setCategories(categoryIds);
        activity.setDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid eos excepturi necessitatibus obcaecati quibusdam. Animi dolorum eaque earum fuga necessitatibus quasi, sapiente veritatis. Cum nostrum quam quis sunt tenetur ut?");
        activity.setImage("filename.png");
        activity.setCreatorId(47);
        activity.setStatus(Activity.Status.ADD_WAITING);

        Request request = new Request();
        request.setStatus(Request.Status.WAITING);
        request.setActivity(activity);
        request.setForDelete(false);
        Request createdRequest = requestDAO.create(activity, false);
        assertNotNull(createdRequest);
        assertEquals(request.getActivity(), createdRequest.getActivity());
        assertEquals(request.getStatus(), createdRequest.getStatus());
        assertEquals(request.isForDelete(), createdRequest.isForDelete());

        request = requestDAO.get(createdRequest.getId());
        assertNotNull(request);

        requestDAO.declineRemove(request.getId(), request.getActivityId());
        request = requestDAO.get(createdRequest.getId());
        assertEquals(Request.Status.DECLINED, request.getStatus());
        assertEquals(Activity.Status.BY_USER, request.getActivity().getStatus());

        assertTrue(activityDAO.delete(request.getActivity().getId()));
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
