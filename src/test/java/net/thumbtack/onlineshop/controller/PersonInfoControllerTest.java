package net.thumbtack.onlineshop.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.Request.AdminDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.CustomerDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.PersonDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.editDto.AdminEditDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.editDto.CustomerDtoEditWithValid;
import net.thumbtack.onlineshop.dto.Response.GetAllCustomerDtoResponse;
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
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest()
@AutoConfigureMockMvc
class PersonInfoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClearDatabaseService clearDb;

    @Value(("${cookie_name}"))
    private String cookieName;

    private AdminDtoWithValid admin;
    private CustomerDtoWithValid customer;
    private CustomerDtoWithValid customer1;
    private final String regUrlAdmin = "/admins";
    private final String regUrlCustomer = "/clients";


    @BeforeAll
    static void initAppProp() throws CheckerException, IOException {
        PropertiesFileChecker.check("application.yml");
    }

    private void initPerson() {
        admin = new AdminDtoWithValid("Василий", "Бочкин", "Иванович",
                "Иvanoв35", "sddsvwe34s", "Работник");
        customer = new CustomerDtoWithValid("Федор", "Иванов", "Петрович",
                "qwert", "sddsvwe34s", "fedor@csdcq.ru", "4100008",
                "+7 (874) 756-56-38");
        customer1 = new CustomerDtoWithValid("Иван", "Петров", "Сергеевич",
                "qwe", "sddsvwe34s", "ivan@csdcq.ru", "4100008",
                "+7 (874) 756-56-38");
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Cookie regUser(PersonDtoWithValid person, String url) throws Exception {
        MockHttpServletResponse response = mvc.perform(post(url)
                .content(asJsonString(person))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        return response.getCookie(cookieName);
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
    void getAllCustomer() throws Exception {
        Cookie cookie = regUser(admin, regUrlAdmin);
        regUser(customer, regUrlCustomer);
        regUser(customer1, regUrlCustomer);
        MockHttpServletResponse response = mvc.perform(get("/clients")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn()
                .getResponse();
        String body = response.getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JavaType listType = mapper
                .getTypeFactory()
                .constructCollectionType(List.class, GetAllCustomerDtoResponse.class);
        List<GetAllCustomerDtoResponse> users = mapper.readValue(body, listType);
        assertEquals(users.get(0).getFirstName(), customer1.getFirstName());
        assertEquals(users.get(1).getFirstName(), customer.getFirstName());
    }

    @Test
    void editAdmin() throws Exception {
        Cookie cookie = regUser(admin, regUrlAdmin);
        AdminEditDtoWithValid edit = new AdminEditDtoWithValid("Дмитрий",
                "Васильев", "Иванович",
                "sddsvwewefew2344ferdsc", "sddsvwe456",
                "Admin");
        MockHttpServletResponse response = mvc.perform(put("/admins")
                .content(asJsonString(edit))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode", is(GlobalExceptionErrorCode.BAD_PASSWORD.name())))
                .andExpect(jsonPath("$.field", is("oldPassword")))
                .andExpect(jsonPath("$.message", is(GlobalExceptionErrorCode.BAD_PASSWORD.getErrorString())))
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        edit.setOldPassword("sddsvwe34s");
        mvc.perform(put("/admins")
                .content(asJsonString(edit))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("firstName", is(edit.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(edit.getLastName())))
                .andExpect(jsonPath("$.patronymic", is(edit.getPatronymic())))
                .andExpect(jsonPath("$.position", is(edit.getPosition())));
    }

    @Test
    void editCustomer() throws Exception {
        Cookie cookie = regUser(customer, regUrlCustomer);
        CustomerDtoEditWithValid edit = new CustomerDtoEditWithValid("Дмитрий",
                "Васильев", "Иванович",
                "sddsvwe34s", "sddsvwe456", "dmitry@csdcq.ru",
                "4100568", "89763455673");
        mvc.perform(put("/clients")
                .content(asJsonString(edit))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("firstName", is(edit.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(edit.getLastName())))
                .andExpect(jsonPath("$.patronymic", is(edit.getPatronymic())))
                .andExpect(jsonPath("$.email", is(edit.getEmail())))
                .andExpect(jsonPath("$.address", is(edit.getAddress())))
                .andExpect(jsonPath("$.phone", is("89763455673")));
    }
}