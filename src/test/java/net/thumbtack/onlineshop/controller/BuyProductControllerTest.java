package net.thumbtack.onlineshop.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.Request.*;
import net.thumbtack.onlineshop.dto.Response.CategoryChildrenDtoResponse;
import net.thumbtack.onlineshop.dto.Response.CategoryParentDtoResponse;
import net.thumbtack.onlineshop.dto.Response.ProductDtoResponse;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.exception.dto.FieldErrorDto;
import net.thumbtack.onlineshop.exception.dto.ValidationErrorDto;
import net.thumbtack.onlineshop.service.ClearDatabaseService;
import net.thumbtack.onlineshop.utils.propfilecheck.CheckerException;
import net.thumbtack.onlineshop.utils.propfilecheck.PropertiesFileChecker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc

class BuyProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClearDatabaseService clearDb;

    @Value(("${cookie_name}"))
    private String cookieName;

    private PersonDtoWithValid admin = new AdminDtoWithValid("Василий", "Бочкин", "Иванович",
            "Иvanoв35", "sddsvwe34s", "Работник");
    private PersonDtoWithValid customer = new CustomerDtoWithValid("Федор", "Иванов", "Петрович",
            "qwert", "sddsvwe34s", "fedor@csdcq.ru", "4100008",
            "+7 (874) 756-56-38");
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void initAppProp() throws CheckerException, IOException {
        PropertiesFileChecker.check("application.yml");
    }

    @AfterEach
    void clearDatabase() {
        clearDb.clear();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Cookie regCustomer() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/clients")
                .content(asJsonString(customer))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Cookie cookie = response.getCookie(cookieName);
        DepositDtoRequest request = new DepositDtoRequest(50000);
        response = mvc.perform(put("/deposits")
                .content(asJsonString(request))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        return response.getCookie(cookieName);
    }

        private Cookie regAdmin() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/admins")
                .content(asJsonString(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        return response.getCookie(cookieName);
    }

    @Test
    void failBuyProduct() throws Exception {
        Cookie cookie = regAdmin();
        ProductDtoRequest product = new ProductDtoRequest("Сыр", 60000, 1, null);
        MockHttpServletResponse response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = regCustomer();
        ProductDtoResponse productResp = mapper.readValue(response.getContentAsString(), ProductDtoResponse.class);
        BuyProductDtoRequest request = new BuyProductDtoRequest(productResp.getId(), "Масло",
                300, 1);
        response = mvc.perform(post("/purchases")
                .cookie(cookie)
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(
                        GlobalExceptionErrorCode.BUY_ERROR.getErrorString()
                )))
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        request.setName("Сыр");
        request.setPrice(60000);
        request.setCount(2);
        response = mvc.perform(post("/purchases")
                .cookie(cookie)
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(
                        GlobalExceptionErrorCode.ERROR_COUNT.getErrorString()
                )))
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        request.setCount(1);
        mvc.perform(post("/purchases")
                .cookie(cookie)
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message", is(
                        GlobalExceptionErrorCode.NO_MONEY.getErrorString()
                )));
    }

    @Test
    void buyProduct() throws Exception {
        Cookie cookie = regAdmin();
        ProductDtoRequest product = new ProductDtoRequest("Сыр", 800, 1, null);
        MockHttpServletResponse response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = regCustomer();
        ProductDtoResponse productResp = mapper.readValue(response.getContentAsString(), ProductDtoResponse.class);
        BuyProductDtoRequest request = new BuyProductDtoRequest(productResp.getId(), productResp.getName(),
                productResp.getPrice(), productResp.getCount());
        mvc.perform(post("/purchases")
                .cookie(cookie)
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) request.getId())))
                .andExpect(jsonPath("$.name", is(request.getName())))
                .andExpect(jsonPath("$.price", is(request.getPrice())))
                .andExpect(jsonPath("$.count", is(request.getCount())));
    }

    @Test
    void buyBasket() {
    }
}