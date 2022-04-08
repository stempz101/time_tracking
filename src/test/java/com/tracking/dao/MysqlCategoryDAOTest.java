package com.tracking.dao;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.dao.mysql.MysqlCategoryDAO;
import com.tracking.lang.Language;
import com.tracking.models.Category;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class MysqlCategoryDAOTest {

    @InjectMocks private MysqlCategoryDAO categoryDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllTest() throws DBException {
        List<Category> expectedList = new ArrayList<>();
        expectedList.add(new Category(12, "Design", "Дизайн"));
        expectedList.add(new Category(32, "Games", "Ігри"));
        expectedList.add(new Category(9, "Cinema", "Кіно"));

        List<Category> categoryList = categoryDAO.getAll(new Locale("en", "EN"), "name_ua",
                "asc", 1, 3);
        assertNotNull(categoryList);
        assertFalse(categoryList.isEmpty());
        assertEquals(expectedList, categoryList);
    }

    @Test
    public void getAllWhereNameTest() throws DBException {
        List<Category> expectedList = new ArrayList<>();
        expectedList.add(new Category(14, "Cooking", "Кулінарія"));
        expectedList.add(new Category(9, "Cinema", "Кіно"));

        List<Category> categoryList = categoryDAO.getAllWhereName("uk", "к", "name_ua",
                "desc", 1, 3);
        assertNotNull(categoryList);
        assertFalse(categoryList.isEmpty());
        assertEquals(expectedList, categoryList);
    }

    @Test
    public void getAllForActivitiesTest() throws DBException {
        List<Category> expectedList = new ArrayList<>();
        expectedList.add(new Category(12, "Design", "Дизайн"));
        expectedList.add(new Category(32, "Games", "Ігри"));
        expectedList.add(new Category(9, "Cinema", "Кіно"));
        expectedList.add(new Category(14, "Cooking", "Кулінарія"));
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

        List<Category> categoryList = categoryDAO.getAllForActivities(new Locale("uk", "UA"));
        assertNotNull(categoryList);
        assertFalse(categoryList.isEmpty());
        assertEquals(expectedList, categoryList);
    }

    @Test
    public void getAllByIdTest() throws DBException {
        List<Category> expectedList = new ArrayList<>();
        expectedList.add(new Category(9, "Cinema", "Кіно"));
        expectedList.add(new Category(14, "Cooking", "Кулінарія"));
        expectedList.add(new Category(12, "Design", "Дизайн"));
        expectedList.add(new Category(32, "Games", "Ігри"));

        List<Integer> idList = new ArrayList<>();
        idList.add(9);
        idList.add(12);
        idList.add(14);
        idList.add(32);

        List<Category> categoryList = categoryDAO.getAllById(idList, new Locale("en", "EN"));
        assertNotNull(categoryList);
        assertFalse(categoryList.isEmpty());
        assertEquals(expectedList, categoryList);
    }

    @Test
    public void getByIdTest() throws DBException {
        Category expected = new Category(28, "Test 10", "Тест 10");
        Category category = categoryDAO.getById(28);
        assertNotNull(category);
        assertEquals(expected, category);
    }

    @Test
    public void getByNameTest() throws DBException {
        Category expected = new Category(32, "Games", "Ігри");
        Category category = categoryDAO.getByName("ігри", Language.UA);
        assertNotNull(category);
        assertEquals(expected, category);
    }

    @Test
    public void getByNameOtherTest() throws DBException {
        Category category = categoryDAO.getByNameOther(14, "Cuisine", Language.EN);
        assertNull(category);
    }

    @Test
    public void getCountTest() throws DBException {
        int expected = 16;
        int count = categoryDAO.getCount();
        assertEquals(expected, count);
    }

    @Test
    public void getCountWhereNameTest() throws DBException {
        int expected = 12;
        int count = categoryDAO.getCountWhereName("test", "en");
        assertEquals(expected, count);
    }

    @Test
    public void createdUpdateDeleteTest() throws DBException {
        Category category = new Category("Sport", "Спорт");
        Category createdCategory = categoryDAO.create(category.getNameEN(), category.getNameUA());
        assertNotNull(createdCategory);
        assertEquals(category.getNameEN(), createdCategory.getNameEN());
        assertEquals(category.getNameUA(), createdCategory.getNameUA());

        category.setId(createdCategory.getId());
        category.setNameEN("Gym");
        category.setNameEN("Зал");
        assertTrue(categoryDAO.update(category.getId(), category.getNameEN(), category.getNameUA()));
        Category updatedCategory = categoryDAO.getById(category.getId());
        assertNotNull(updatedCategory);
        assertEquals(category.getId(), updatedCategory.getId());
        assertEquals(category.getNameEN(), updatedCategory.getNameEN());
        assertEquals(category.getNameUA(), updatedCategory.getNameUA());

        assertTrue(categoryDAO.delete(category.getId()));
    }

}
