package net.thumbtack.onlineshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.Request.AdminDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.CustomerDtoWithValid;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.service.ClearDatabaseService;
import net.thumbtack.onlineshop.utils.propfilecheck.CheckerException;
import net.thumbtack.onlineshop.utils.propfilecheck.PropertiesFileChecker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest()
@AutoConfigureMockMvc
class SignUpAndLogoutControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClearDatabaseService clearDb;

    @Value(("${cookie_name}"))
    private String cookieName;

    private AdminDtoWithValid admin;
    private CustomerDtoWithValid user;

    @BeforeAll
    static void initAppProp() throws CheckerException, IOException {
        PropertiesFileChecker.check("application.yml");
    }

    private void initPerson() {
        admin = new AdminDtoWithValid("Василий", "Бочкин", "Иванович",
                "Иvanoв35", "sddsvwe34s", "Работник");
        user = new CustomerDtoWithValid("Федор", "Иванов", "Петрович",
                "qwert", "sddsvwe34s", "sads@csdcq.ru", "4100008",
                "+7 (874) 756-56-38");
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void initBeforePerson() {
        initPerson();
    }

    @AfterEach
    void initAfterPersons() {
        initPerson();
        clearDb.clear();
    }

    @Test
    void failRegUser() throws Exception {
        user.setFirstName("Vasилий");
        user.setLastName("Dochкин");
        user.setPatronymic("Петровиch");
        user.setLogin("Иva  noв35");
        user.setPassword("sddsv");
        user.setEmail("assd@sdfsd");
        user.setAddress("");
        user.setPhone("(874) 756-56-38");
        mvc.perform(post("/clients")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.errors", hasSize(8)))
                .andExpect(jsonPath("$.errors[*].field", hasItems(
                        "lastName", "patronymic", "phone", "address", "email", "login", "fistName", "password"
                )))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder(
                        "FirstName can't be empty and have a maximum 50 letters and contain letters of the russian alphabet, spaces and dashes",
                        "LastName can't be empty and have a maximum length of 50 letters and contain letters of the russian alphabet, spaces and dashes",
                        "Patronymic have a maximum length of 50 letters and contain letters of the russian alphabet, spaces and dashes",
                        "Login can't be empty, have a maximum length of 50 letters and contain only letters and numbers",
                        "Password can't be empty and have a minimum length of 8 letters",
                        "Invalid format e-mail",
                        "Address can't be empty",
                        "Invalid format number of phone"
                )))
                .andExpect(cookie().doesNotExist(cookieName));
    }

    @Test
    void regPerson() throws Exception {
        mvc.perform(post("/clients")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.patronymic", is(user.getPatronymic())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.address", is(user.getAddress())))
                .andExpect(jsonPath("$.phone", is("+78747565638")))
                .andExpect(jsonPath("$.deposit", is(0)))
                .andExpect(cookie().exists(cookieName));


    }

    @Test
    void login() throws Exception {
       MockHttpServletResponse response = mvc.perform(post("/clients")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
       Cookie cookie = response.getCookie(cookieName);
        mvc.perform(get("/accounts")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.patronymic", is(user.getPatronymic())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.address", is(user.getAddress())))
                .andExpect(jsonPath("$.phone", is("+78747565638")))
                .andExpect(jsonPath("$.deposit", is(0)));
        response = mvc.perform(post("/admins")
                .content(asJsonString(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        mvc.perform(get("/accounts")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("firstName", is(admin.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(admin.getLastName())))
                .andExpect(jsonPath("$.patronymic", is(admin.getPatronymic())))
                .andExpect(jsonPath("$.position", is(admin.getPosition())));
    }

    @Test
    void logout() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/clients")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        Cookie cookie = response.getCookie(cookieName);
        mvc.perform(delete("/sessions")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists(cookieName));
        cookie = response.getCookie(cookieName);
        mvc.perform(get("/accounts")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void failRegUserWithDuplicateLogin() throws Exception {
        mvc.perform(post("/clients")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        admin.setLogin(user.getLogin());
        mvc.perform(post("/admins")
                .content(asJsonString(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.errorCode", is(GlobalExceptionErrorCode.LOGIN_EXIST.name())))
                .andExpect(jsonPath("$.field", is("login")))
                .andExpect(jsonPath("$.message", is(GlobalExceptionErrorCode.LOGIN_EXIST.getErrorString())));
    }

    @Test
    void failAccessUser() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/clients")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        Cookie cookie = response.getCookie(cookieName);
        mvc.perform(get("/clients")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode", is(GlobalExceptionErrorCode.FORBIDDEN.name())))
                .andExpect(jsonPath("$.field", is("")))
                .andExpect(jsonPath("$.message", is(GlobalExceptionErrorCode.FORBIDDEN.getErrorString())));

    }
}