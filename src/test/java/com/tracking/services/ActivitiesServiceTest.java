package com.tracking.services;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.activities.ActivitiesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class ActivitiesServiceTest {

    @InjectMocks
    ActivitiesService activitiesService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getMaxPeopleTest() throws ServiceException {
        assertEquals(activitiesService.getMaxPeopleCount(), 7);
    }
}
