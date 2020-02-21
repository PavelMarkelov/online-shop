package net.thumbtack.onlineshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.Request.CustomerDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.DepositDtoRequest;
import net.thumbtack.onlineshop.dto.Request.PersonDtoWithValid;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DepositControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClearDatabaseService clearDb;

    @Value(("${cookie_name}"))
    private String cookieName;

    private PersonDtoWithValid customer = new CustomerDtoWithValid("Федор", "Иванов", "Петрович",
            "qwert", "sddsvwe34s", "fedor@csdcq.ru", "4100008",
            "+7 (874) 756-56-38");

    @BeforeAll
    static void initAppProp() throws CheckerException, IOException {
        PropertiesFileChecker.check("application.yml");
    }

    @AfterEach
    void initAfterPersons() {
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
        return response.getCookie(cookieName);
    }


    @Test
    void depositingMoney() throws Exception {
        Cookie cookie = regCustomer();
        DepositDtoRequest request = new DepositDtoRequest(5000);
        MockHttpServletResponse response = mvc.perform(put("/deposits")
                .content(asJsonString(request))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deposit", is(request.getDeposit())))
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        response = mvc.perform(put("/deposits")
                .content(asJsonString(request))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        mvc.perform(get("/deposits")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deposit",
                        is(request.getDeposit() + request.getDeposit())));
    }
}