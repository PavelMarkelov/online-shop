package net.thumbtack.onlineshop.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.controller.data.BuyBasket;
import net.thumbtack.onlineshop.controller.data.ListOfProductsWithCookie;
import net.thumbtack.onlineshop.controller.data.ProductInfo;
import net.thumbtack.onlineshop.dto.Request.*;
import net.thumbtack.onlineshop.dto.Response.ProductDtoResponse;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
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
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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
        DepositDtoRequest request = new DepositDtoRequest(500);
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

    private ListOfProductsWithCookie initBasket() throws Exception {
        Cookie cookie = regAdmin();
        ProductDtoRequest product = new ProductDtoRequest("Сыр", 500, 5, null);
        MockHttpServletResponse response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        ProductDtoResponse productResp = mapper.readValue(response.getContentAsString(), ProductDtoResponse.class);
        product = new ProductDtoRequest("Масло", 500, 5, null);
        response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        ProductDtoResponse productResp1 = mapper.readValue(response.getContentAsString(), ProductDtoResponse.class);
        product = new ProductDtoRequest("Молоко", 50, 5, null);
        response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        ProductDtoResponse productResp2 = mapper.readValue(response.getContentAsString(), ProductDtoResponse.class);
        product = new ProductDtoRequest("Кефир", 60, 5, null);
        response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        ProductDtoResponse productResp3 = mapper.readValue(response.getContentAsString(), ProductDtoResponse.class);
        Cookie cookieCustomer = regCustomer();
        BuyProductDtoRequest productBasket = new BuyProductDtoRequest(productResp.getId(), productResp.getName(),
                productResp.getPrice(), productResp.getCount());
        response = mvc.perform(post("/baskets")
                .cookie(cookieCustomer)
                .content(asJsonString(productBasket))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookieCustomer = response.getCookie(cookieName);
        BuyProductDtoRequest productBasket1 = new BuyProductDtoRequest(productResp1.getId(), productResp1.getName(),
                productResp1.getPrice(), 2);
        response = mvc.perform(post("/baskets")
                .cookie(cookieCustomer)
                .content(asJsonString(productBasket1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookieCustomer = response.getCookie(cookieName);
        BuyProductDtoRequest productBasket2 = new BuyProductDtoRequest(productResp2.getId(), productResp2.getName(),
                productResp2.getPrice(), 3);
        response = mvc.perform(post("/baskets")
                .cookie(cookieCustomer)
                .content(asJsonString(productBasket2))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookieCustomer = response.getCookie(cookieName);
        BuyProductDtoRequest productBasket3 = new BuyProductDtoRequest(productResp3.getId(), productResp3.getName(),
                productResp3.getPrice(), 10);
        response = mvc.perform(post("/baskets")
                .cookie(cookieCustomer)
                .content(asJsonString(productBasket3))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookieCustomer = response.getCookie(cookieName);
        mvc.perform(delete("/products/{id}", productResp1.getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<BuyProductDtoRequest> request = Arrays.asList(productBasket, productBasket1,
                productBasket2, productBasket3);
        return new ListOfProductsWithCookie(request, cookieCustomer);
    }

    @Test
    void failBuyProduct() throws Exception {
        Cookie cookie = regAdmin();
        ProductDtoRequest product = new ProductDtoRequest("Сыр", 600, 1, null);
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
        request.setPrice(600);
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
        ProductDtoRequest product = new ProductDtoRequest("Сыр", 400, 1, null);
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
    void buyBasket() throws Exception {
        ListOfProductsWithCookie initBasket = initBasket();
        Cookie cookie = initBasket.getCookie();
        List<BuyProductDtoRequest> request = initBasket.getResponse();
        mvc.perform(post("/purchases/baskets")
                .cookie(cookie)
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message", is(
                        GlobalExceptionErrorCode.ERROR_BASKET.getErrorString()
                )));
        MockHttpServletResponse response = mvc.perform(get("/products")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        JavaType listType = mapper
                .getTypeFactory()
                .constructCollectionType(List.class, ProductInfo.class);
        List<ProductInfo> productsList = mapper
                .readValue(response.getContentAsString(), listType);
        String serialize = mapper.writeValueAsString(productsList);
        assertEquals("[{\"name\":\"Кефир\",\"price\":60,\"count\":5,\"categories\":[]}," +
                "{\"name\":\"Молоко\",\"price\":50,\"count\":5,\"categories\":[]}," +
                "{\"name\":\"Сыр\",\"price\":500,\"count\":5,\"categories\":[]}]", serialize);
        DepositDtoRequest deposit = new DepositDtoRequest(4500);
        mvc.perform(put("/deposits")
                .content(asJsonString(deposit))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        request.get(2).setCount(5); // молоко
        response = mvc.perform(post("/purchases/baskets")
                .cookie(cookie)
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        BuyBasket buyBasket = mapper.readValue(response.getContentAsString(), BuyBasket.class);
        serialize = mapper.writeValueAsString(buyBasket);
        assertEquals("{\"bought\":" +
                "[{\"name\":\"Сыр\",\"price\":500,\"count\":5}," +
                "{\"name\":\"Молоко\",\"price\":50,\"count\":3}]," +
                "\"remaining\":" +
                "[{\"name\":\"Кефир\",\"price\":60,\"count\":10}," +
                "{\"name\":\"Масло\",\"price\":500,\"count\":2}]}", serialize);
        int total = 5000 - 500 * 5 - 50 * 3;
        mvc.perform(get("/deposits")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deposit", is(total)));
        response = mvc.perform(get("/products")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        productsList = mapper.readValue(response.getContentAsString(), listType);
        serialize = mapper.writeValueAsString(productsList);
        assertEquals("[{\"name\":\"Кефир\",\"price\":60,\"count\":5,\"categories\":[]}," +
                "{\"name\":\"Молоко\",\"price\":50,\"count\":2,\"categories\":[]}," +
                "{\"name\":\"Сыр\",\"price\":500,\"count\":0,\"categories\":[]}]", serialize);
    }
}