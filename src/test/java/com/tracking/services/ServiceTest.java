package com.tracking.services;

import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.activities.ActivityService;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class ServiceTest {

    @Test
    public void getLocaleTest() {
        Locale expectedLocale = new Locale("uk", "UA");
        Locale locale = Service.getLocale("uk_UA");
        assertNotNull(locale);
        assertEquals(expectedLocale, locale);
    }

    @Test
    public void getPageCountTest() {
        int itemsCount = 56;
        int total = 9;
        int expectedCount = 7;
        int pageCount = new ActivityService().getPageCount(itemsCount, total);
        assertTrue(pageCount > 0 && pageCount * total >= itemsCount);
        assertEquals(expectedCount, pageCount);
    }

}
