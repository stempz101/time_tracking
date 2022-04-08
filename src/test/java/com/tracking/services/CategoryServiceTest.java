package com.tracking.services;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.categories.CategoryService;
import com.tracking.models.Category;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class CategoryServiceTest {

    @InjectMocks private CategoryService categoryService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getTest() throws ServiceException {
        Category expectedCategory = new Category(32, "Games", "Ігри");
        Category category = categoryService.get(32);
        assertNotNull(category);
        assertEquals(expectedCategory, category);
    }

}
