package net.thumbtack.onlineshop.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.controller.data.ListOfCategoriesWithCookie;
import net.thumbtack.onlineshop.dto.Request.AdminDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.CategoryDtoRequest;
import net.thumbtack.onlineshop.dto.Request.ProductDtoRequest;
import net.thumbtack.onlineshop.dto.Request.ProductEditDtoRequest;
import net.thumbtack.onlineshop.dto.Response.CategoryChildrenDtoResponse;
import net.thumbtack.onlineshop.dto.Response.CategoryParentDtoResponse;
import net.thumbtack.onlineshop.dto.Response.ProductDtoResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
class ProductAdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClearDatabaseService clearDb;

    @Value(("${cookie_name}"))
    private String cookieName;

    private AdminDtoWithValid admin = new AdminDtoWithValid("Василий",
            "Бочкин", "Иванович",
            "Иvanoв35", "sddsvwe34s", "Работник");
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void initAppProp() throws CheckerException, IOException {
        PropertiesFileChecker.check("application.yml");
    }

    @AfterEach
    void initAfterPersons() {
        clearDb.clear();
    }

    private MockHttpServletResponse saveCategory(CategoryDtoRequest category, Cookie cookie) throws Exception {
        return mvc.perform(post("/categories")
                .cookie(cookie)
                .content(asJsonString(category))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
    }

    private ListOfCategoriesWithCookie initDb() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/admins")
                .content(asJsonString(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        response.getCookie(cookieName);
        Cookie cookie = response.getCookie(cookieName);
        CategoryDtoRequest category = new CategoryDtoRequest("Продукты", 0);
        response = saveCategory(category, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse parentCategory = mapper.readValue(response.getContentAsString(), CategoryParentDtoResponse.class);
        CategoryDtoRequest category1 = new CategoryDtoRequest("Одежда", 0);
        response = saveCategory(category1, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse parentCategory1 = mapper.readValue(response.getContentAsString(), CategoryParentDtoResponse.class);
        CategoryDtoRequest category2 = new CategoryDtoRequest("Напитки", parentCategory.getId());
        response = saveCategory(category2, cookie);
        cookie = response.getCookie(cookieName);
        CategoryDtoRequest category3 = new CategoryDtoRequest("Куртки", parentCategory1.getId());
        response = saveCategory(category3, cookie);
        cookie = response.getCookie(cookieName);
        response = mvc.perform(get("/categories/")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        JavaType listType = mapper
                .getTypeFactory()
                .constructCollectionType(List.class, CategoryChildrenDtoResponse.class);
        List<CategoryParentDtoResponse> categories = mapper
                .readValue(response.getContentAsString(), listType);
        return new ListOfCategoriesWithCookie(categories, cookie);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void editProduct() throws Exception {
        ListOfCategoriesWithCookie data = initDb();
        List<CategoryParentDtoResponse> categories = data.getCategories();
        Cookie cookie = data.getCookie();
        Set<Long> categoriesId = categories.stream()
                .map(CategoryParentDtoResponse::getId)
                .collect(Collectors.toSet());
        ProductDtoRequest product = new ProductDtoRequest("Сыр", 600, 0, categoriesId);
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
        categoriesId = categories.stream()
                .filter(item -> item.getName().equals("Продукты"))
                .map(CategoryParentDtoResponse::getId)
                .collect(Collectors.toSet());
        ProductEditDtoRequest product1 = new ProductEditDtoRequest("", 0, 50, categoriesId);
        product.setCategories(categoriesId);
        response = mvc.perform(put("/products/{id}", productResp.getId())
                .cookie(cookie)
                .content(asJsonString(product1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder(
                        "Product name can't be empty",
                        "Product price can't be less than or equal to zero"
                )))
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        product1.setName("Мороженное");
        product1.setPrice(30);
        response = mvc.perform(put("/products/{id}", productResp.getId())
                .cookie(cookie)
                .content(asJsonString(product1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        ProductDtoResponse prod = mapper.readValue(response.getContentAsString(), ProductDtoResponse.class);
        assertEquals(prod.getId(), productResp.getId());
        assertEquals(prod.getName(), product1.getName());
        assertEquals(prod.getPrice(), product1.getPrice());
        assertEquals(prod.getCount(), product1.getCount());
        assertEquals(prod.getCategories(), new ArrayList<>(categoriesId));
    }

    @Test
    void delProduct() throws Exception {
        ListOfCategoriesWithCookie data = initDb();
        List<CategoryParentDtoResponse> categories = data.getCategories();
        Cookie cookie = data.getCookie();
        Set<Long> categoriesId = categories.stream()
                .map(CategoryParentDtoResponse::getId)
                .collect(Collectors.toSet());
        ProductDtoRequest product = new ProductDtoRequest("Сыр", 600, 0, categoriesId);
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
        response = mvc.perform(delete("/products/{id}", productResp.getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        mvc.perform(get("/products/{id}", productResp.getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",
                        is("Product with the specified id not exists")));
    }
}