package net.thumbtack.onlineshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BasketControllerTest {

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
    void clearDb() {
        clearDb.clear();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private ProductDtoResponse[] initDatabase() throws Exception {
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
        ProductDtoResponse productResp1 = mapper.readValue(response.getContentAsString(), ProductDtoResponse.class);
        return new ProductDtoResponse[]{productResp, productResp1};
    }

        @Test
    void addItemToBasket() throws Exception {
            ProductDtoResponse[] productsDto = initDatabase();
            Cookie cookieCustomer = regCustomer();
            BuyProductDtoRequest productBasket = new BuyProductDtoRequest(productsDto[0].getId(), productsDto[0].getName(),
                    productsDto[0].getPrice(), 50);
            mvc.perform(post("/baskets")
                    .cookie(cookieCustomer)
                    .content(asJsonString(productBasket))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.[*]", hasSize(1)))
                    .andExpect(jsonPath("[0].id", is((int) productBasket.getId())))
                    .andExpect(jsonPath("[0].name", is(productBasket.getName())))
                    .andExpect(jsonPath("[0].price", is(productBasket.getPrice())))
                    .andExpect(jsonPath("[0].count", is(productBasket.getCount())));
        }

    @Test
    void delItemFromBasket() throws Exception {
        ProductDtoResponse[] productsDto = initDatabase();
        Cookie cookieCustomer = regCustomer();
        BuyProductDtoRequest productBasket = new BuyProductDtoRequest(productsDto[0].getId(), productsDto[0].getName(),
                productsDto[0].getPrice(), 50);
        mvc.perform(post("/baskets")
                .cookie(cookieCustomer)
                .content(asJsonString(productBasket))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mvc.perform(delete("/baskets/{id}", productsDto[0].getId())
                .cookie(cookieCustomer)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mvc.perform(get("/baskets")
                .cookie(cookieCustomer)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", empty()));
    }

    @Test
    void changeQuantity() throws Exception {
        ProductDtoResponse[] productsDto = initDatabase();
        Cookie cookieCustomer = regCustomer();
        BuyProductDtoRequest productBasket = new BuyProductDtoRequest(productsDto[0].getId(), productsDto[0].getName(),
                productsDto[0].getPrice(), 50);
        mvc.perform(post("/baskets")
                .cookie(cookieCustomer)
                .content(asJsonString(productBasket))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        admin.setLogin("VAG");
        Cookie adminCookie = regAdmin();
        mvc.perform(delete("/products/{id}", productsDto[0].getId())
                .cookie(adminCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        productBasket.setId(0);
        productBasket.setPrice(300);
        productBasket.setCount(4);
        mvc.perform(put("/baskets")
                .cookie(cookieCustomer)
                .content(asJsonString(productBasket))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(
                        GlobalExceptionErrorCode.PRODUCT_NOT_FOUND.getErrorString()
                )));
        productBasket.setId(productsDto[0].getId());
        mvc.perform(put("/baskets")
                .cookie(cookieCustomer)
                .content(asJsonString(productBasket))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(
                        GlobalExceptionErrorCode.BUY_ERROR.getErrorString()
                )));
        productBasket.setPrice(productsDto[0].getPrice());
        mvc.perform(put("/baskets")
                .cookie(cookieCustomer)
                .content(asJsonString(productBasket))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("[0].id", is((int) productBasket.getId())))
                .andExpect(jsonPath("[0].name", is(productBasket.getName())))
                .andExpect(jsonPath("[0].price", is(productBasket.getPrice())))
                .andExpect(jsonPath("[0].count", is(productBasket.getCount())));
    }

    @Test
    void getBasket() throws Exception {
        ProductDtoResponse[] productsDto = initDatabase();
        Cookie cookieCustomer = regCustomer();
        BuyProductDtoRequest productBasket = new BuyProductDtoRequest(productsDto[0].getId(), productsDto[0].getName(),
                productsDto[0].getPrice(), 18);
        BuyProductDtoRequest productBasket1 = new BuyProductDtoRequest(productsDto[1].getId(), productsDto[1].getName(),
                productsDto[1].getPrice(), 10);
        mvc.perform(post("/baskets")
                .cookie(cookieCustomer)
                .content(asJsonString(productBasket))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mvc.perform(post("/baskets")
                .cookie(cookieCustomer)
                .content(asJsonString(productBasket1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mvc.perform(get("/baskets")
                .cookie(cookieCustomer)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(2)))
                .andExpect(jsonPath("[0].id", is((int) productBasket.getId())))
                .andExpect(jsonPath("[0].name", is(productBasket.getName())))
                .andExpect(jsonPath("[0].price", is(productBasket.getPrice())))
                .andExpect(jsonPath("[0].count", is(productBasket.getCount())))
                .andExpect(jsonPath("[1].id", is((int) productBasket1.getId())))
                .andExpect(jsonPath("[1].name", is(productBasket1.getName())))
                .andExpect(jsonPath("[1].price", is(productBasket1.getPrice())))
                .andExpect(jsonPath("[1].count", is(productBasket1.getCount())));
    }
}