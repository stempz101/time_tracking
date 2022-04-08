package com.tracking.validations;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.activities.ActivitiesService;
import com.tracking.dao.ActivityDAO;
import com.tracking.dao.CategoryDAO;
import com.tracking.dao.UserDAO;
import com.tracking.dao.mysql.MysqlActivityDAO;
import com.tracking.dao.mysql.MysqlDAOFactory;
import com.tracking.models.Activity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidationTest {

    @Test
    public void activityValidateNameTestTrue() throws SQLException {
        assertTrue(ActivityDAO.validateName("Test 43 testing 23 activity 45"));
        assertTrue(ActivityDAO.validateName("Моя активність на 5-ку!?"));
        assertTrue(ActivityDAO.validateName("Тест Test"));
    }

    @Test
    public void activityValidateNameTestFalse() throws SQLException {
        assertFalse(ActivityDAO.validateName("Test. <script>alert('Test');</script>"));
        assertFalse(ActivityDAO.validateName(null));
        assertFalse(ActivityDAO.validateName(""));
    }

    @Test
    public void activityValidateDescriptionTestTrue() {
        assertTrue(ActivityDAO.validateDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab aut beatae consectetur debitis deleniti dolores enim est eum excepturi fuga minus, nobis obcaecati odio pariatur perferendis placeat quia quibusdam quos repellendus sed similique tempore temporibus unde vitae voluptatibus? Aperiam dolorum, exercitationem impedit inventore maxime nemo officiis quasi quibusdam suscipit veritatis!"));
        assertTrue(ActivityDAO.validateDescription("Лорем ипсум долор сит амет, еффициантур детерруиссет те еам, нисл сцрипторем дуо ан. Аццусата номинати принципес еи еам, ириуре медиоцритатем вих ат? Иус сцрипта репримияуе ан! Хис ат вениам аццоммодаре, сит ид иллуд репримияуе интеллегам, саепе оцурререт ех мел.\n\nАн хас виде цонституто, утинам ментитум ассуеверит ид хас, еа яуидам."));
        assertTrue(ActivityDAO.validateDescription("演ぜ数昭ホ電61盗チレセナ現辺めやせい中報ッあぶ大青コヤフラ多5映ス上真リ女静ド記76著問59速ぼび走住坊斉遣ぞりル。蔵アネミツ作絵スヨミマ日録詳んばちす米直トスヱハ経既道ヌカネ反30作求とお筋藤でゅ英化ラひすそ際気レれわあ白笑あ移川ょ山要歳はドへ。雪査績がぞでの立危コホニ上毛だぜで記官ムワシ室加面テネ水断ツ医期調ソ政開の領益げわめを売直ヨノ供近オ刊坊斉遣周てをやで。"));
    }

    @Test
    public void activityValidateDescriptionTestFalse() {
        assertFalse(ActivityDAO.validateDescription(""));
        assertFalse(ActivityDAO.validateDescription(null));
    }

    @Test
    public void categoryValidateNameEnTestTrue() {
        assertTrue(CategoryDAO.validateNameEn("3D"));
        assertTrue(CategoryDAO.validateNameEn("AAA"));
        assertTrue(CategoryDAO.validateNameEn("Mobile"));
    }

    @Test
    public void categoryValidateNameEnTestFalse() {
        assertFalse(CategoryDAO.validateNameEn(""));
        assertFalse(CategoryDAO.validateNameEn(null));
        assertFalse(CategoryDAO.validateNameEn("Азбука"));
    }

    @Test
    public void categoryValidateNameUaTestTrue() {
        assertTrue(CategoryDAO.validateNameUa("3D дизайн"));
        assertTrue(CategoryDAO.validateNameUa("Вірші"));
        assertTrue(CategoryDAO.validateNameUa("Пісні"));
    }

    @Test
    public void categoryValidateNameUaTestFalse() {
        assertFalse(CategoryDAO.validateNameUa(""));
        assertFalse(CategoryDAO.validateNameUa(null));
        assertFalse(CategoryDAO.validateNameUa("Мемы"));
    }

    @Test
    public void userValidatePasswordTestTrue() {
        assertTrue(UserDAO.validatePassword("qwerty123"));
        assertTrue(UserDAO.validatePassword("D6sdf687"));
        assertTrue(UserDAO.validatePassword("naj78shg428gtVHJda7gt23"));
    }

    @Test
    public void userValidatePasswordTestFalse() {
        assertFalse(UserDAO.validatePassword("qwer2"));
        assertFalse(UserDAO.validatePassword(""));
        assertFalse(UserDAO.validatePassword(null));
        assertFalse(UserDAO.validatePassword("uyt4!#gf"));
    }

    @Test
    public void userConfirmPasswordTestTrue() {
        assertTrue(UserDAO.confirmPassword("qwerty123", "qwerty123"));
        assertTrue(UserDAO.confirmPassword("D6sdf687", "D6sdf687"));
        assertTrue(UserDAO.confirmPassword("naj78shg428gtVHJda7gt23", "naj78shg428gtVHJda7gt23"));
    }

    @Test
    public void userConfirmPasswordTestFalse() {
        assertFalse(UserDAO.confirmPassword("qwerty123", "qwerty"));
        assertFalse(UserDAO.confirmPassword("qwerty123", ""));
        assertFalse(UserDAO.confirmPassword("qwerty123", null));
    }

    @Test
    public void userValidateNameTestTrue() {
        assertTrue(UserDAO.validateName("Max"));
        assertTrue(UserDAO.validateName("Smith"));
        assertTrue(UserDAO.validateName("Андрій"));
        assertTrue(UserDAO.validateName("Ґвалда"));
    }

    @Test
    public void userValidateNameTestFalse() {
        assertFalse(UserDAO.validateName("Max322"));
        assertFalse(UserDAO.validateName(""));
        assertFalse(UserDAO.validateName(null));
    }

    @Test
    public void userValidateEmailTestTrue() {
        assertTrue(UserDAO.validateEmail("qwerty@q.ua"));
        assertTrue(UserDAO.validateEmail("q2w3e4r5ty@qwe.com"));
        assertTrue(UserDAO.validateEmail("qwerty_123_asd@qwe.coma"));
    }

    @Test
    public void userValidateEmailTestFalse() {
        assertFalse(UserDAO.validateEmail("qwe!da@qwe.com"));
        assertFalse(UserDAO.validateEmail("qweasasd"));
        assertFalse(UserDAO.validateEmail("asdasda@das"));
        assertFalse(UserDAO.validateEmail("asdasda@das.c"));
        assertFalse(UserDAO.validateEmail("asdasda@das.comama"));
        assertFalse(UserDAO.validateEmail(""));
        assertFalse(UserDAO.validateEmail(null));
    }

}
