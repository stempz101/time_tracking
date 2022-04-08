package com.tracking.services;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.categories.CategoriesService;
import com.tracking.models.Category;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class CategoriesServiceTest {

    @InjectMocks private CategoriesService categoriesService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllCategoriesTest() throws ServiceException {
        List<Category> expectedList = new ArrayList<>();
        expectedList.add(new Category(9, "Cinema", "Кіно"));
        expectedList.add(new Category(14, "Cooking", "Кулінарія"));
        expectedList.add(new Category(12, "Design", "Дизайн"));
        expectedList.add(new Category(32, "Games", "Ігри"));
        expectedList.add(new Category(15, "Test 01", "Тест 01"));
        expectedList.add(new Category(16, "Test 02", "Тест 02"));
        expectedList.add(new Category(17, "Test 03", "Тест 03"));
        expectedList.add(new Category(18, "Test 04", "Тест 04"));
        expectedList.add(new Category(19, "Test 05", "Тест 05"));
        expectedList.add(new Category(20, "Test 06", "Тест 06"));
        expectedList.add(new Category(21, "Test 07", "Тест 07"));
        expectedList.add(new Category(26, "Test 08", "Тест 08"));
        expectedList.add(new Category(27, "Test 09", "Тест 09"));
        expectedList.add(new Category(28, "Test 10", "Тест 10"));
        expectedList.add(new Category(29, "Test 11", "Тест 11"));
        expectedList.add(new Category(30, "Test 12", "Тест 12"));

        List<Category> categoryList = categoriesService.getAllCategories(new Locale("en", "EN"));
        assertNotNull(categoryList);
        assertFalse(categoryList.isEmpty());
        assertEquals(expectedList, categoryList);
    }

}
