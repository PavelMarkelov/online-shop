package net.thumbtack.onlineshop.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.controller.data.ListOfCategoriesWithCookie;
import net.thumbtack.onlineshop.controller.data.ProductInfo;
import net.thumbtack.onlineshop.dto.Request.AdminDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.CategoryDtoRequest;
import net.thumbtack.onlineshop.dto.Request.ProductDtoRequest;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductInfoControllerTest {
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
    void clearDatabase() {
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

    private Cookie regUser() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/admins")
                .content(asJsonString(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        return response.getCookie(cookieName);
    }

    private ListOfCategoriesWithCookie initDb() throws Exception {
        Cookie cookie = regUser();
        CategoryDtoRequest category = new CategoryDtoRequest("Продукты", 0);
        MockHttpServletResponse response = saveCategory(category, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse parentCategory = mapper.readValue(response.getContentAsString(), CategoryParentDtoResponse.class);
        CategoryDtoRequest category1 = new CategoryDtoRequest("Одежда", 0);
        response = saveCategory(category1, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse parentCategory1 = mapper.readValue(response.getContentAsString(), CategoryParentDtoResponse.class);
        CategoryDtoRequest category2 = new CategoryDtoRequest("Напитки", parentCategory.getId());
        response = saveCategory(category2, cookie);
        CategoryParentDtoResponse childCategory = mapper.readValue(response.getContentAsString(), CategoryChildrenDtoResponse.class);
        cookie = response.getCookie(cookieName);
        CategoryDtoRequest category3 = new CategoryDtoRequest("Куртки", parentCategory1.getId());
        response = saveCategory(category3, cookie);
        CategoryParentDtoResponse childCategory1 = mapper.readValue(response.getContentAsString(), CategoryChildrenDtoResponse.class);
        cookie = response.getCookie(cookieName);
        Set<Long> categoriesId = new HashSet<>();
        categoriesId.add(parentCategory.getId());
        ProductDtoRequest product = new ProductDtoRequest("Сыр", 600, 0, categoriesId);
        response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        categoriesId.clear();
        categoriesId.add(childCategory.getId());
        product = new ProductDtoRequest("Лемонад", 70, 2, categoriesId);
        response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        categoriesId.clear();
        categoriesId.add(childCategory.getId());
        product = new ProductDtoRequest("Чай", 50, 20, categoriesId);
        response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        categoriesId.clear();
        categoriesId.add(childCategory1.getId());
        product = new ProductDtoRequest("Ветрока", 4000, 5, categoriesId);
        response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
//        Товар добавляется в несколько категорий
        categoriesId.add(parentCategory1.getId());
        product = new ProductDtoRequest("Толстовка", 6000, 10, categoriesId);
        response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        product = new ProductDtoRequest("Наушники", 10000, 3, null);
        response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        response = mvc.perform(get("/categories/")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
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
    void getProduct() throws Exception {
        Cookie cookie = regUser();
        CategoryDtoRequest category = new CategoryDtoRequest("Фотоаппараты", 0);
        MockHttpServletResponse response = saveCategory(category, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse parentCategory = mapper.readValue(response.getContentAsString(), CategoryParentDtoResponse.class);
        Set<Long> categoriesId = new HashSet<>();
        categoriesId.add(parentCategory.getId());
        ProductDtoRequest product = new ProductDtoRequest("Nikon d700", 90000, 3, categoriesId);
        response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        ProductDtoResponse productResp = mapper.readValue(response.getContentAsString(), ProductDtoResponse.class);
        cookie = response.getCookie(cookieName);
        mvc.perform(get("/products/{id}", productResp.getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.price", is(product.getPrice())))
                .andExpect(jsonPath("$.count", is(product.getCount())))
                .andExpect(jsonPath("$.categories", hasItems(parentCategory.getName())));
    }


//        [\"name\":\"Одежда\",\"parentName\":null}," +
//                "{\"name\":\"Куртки\",\"parentName\":\"Одежда\"}," +
//                "{\"name\":\"Продукты\",\"parentName\":null}," +
//                "{\"name\":\"Напитки\",\"parentName\":\"Продукты\"}]";

    @Test
    void getAllProductsByOrderByProduct() throws Exception {
        ListOfCategoriesWithCookie data = initDb();
        List<CategoryParentDtoResponse> categories = data.getCategories();
        Cookie cookie = data.getCookie();
        MockHttpServletResponse response = mvc.perform(get("/products")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        JavaType listType = mapper
                .getTypeFactory()
                .constructCollectionType(List.class, ProductInfo.class);
        List<ProductInfo> productsList = mapper
                .readValue(response.getContentAsString(), listType);
        String serialize = mapper.writeValueAsString(productsList);
//        Ожидаем получить весь список товаров, отсортированный по названиям
        assertEquals(serialize, "[{\"name\":\"Ветрока\",\"price\":4000,\"count\":5,\"categories\":[\"Куртки\"]}," +
                "{\"name\":\"Лемонад\",\"price\":70,\"count\":2,\"categories\":[\"Напитки\"]}," +
                "{\"name\":\"Наушники\",\"price\":10000,\"count\":3,\"categories\":[]}," +
                "{\"name\":\"Сыр\",\"price\":600,\"count\":0,\"categories\":[\"Продукты\"]}," +
                "{\"name\":\"Толстовка\",\"price\":6000,\"count\":10,\"categories\":[\"Куртки\",\"Одежда\"]}," +
                "{\"name\":\"Чай\",\"price\":50,\"count\":20,\"categories\":[\"Напитки\"]}]");
//        Передаем пустой список категорий
        response = mvc.perform(get("/products?category=")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        productsList = mapper.readValue(response.getContentAsString(), listType);
        serialize = mapper.writeValueAsString(productsList);
        assertEquals(serialize, "[{\"name\":\"Наушники\",\"price\":10000,\"count\":3,\"categories\":[]}]");
        response = mvc.perform(get("/products?category={id},{id}",
                categories.get(3).getId(),categories.get(0).getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        productsList = mapper.readValue(response.getContentAsString(), listType);
        serialize = mapper.writeValueAsString(productsList);
//        Ожидаем получить товары только указанных категорий, отсортированных по названию
        assertEquals(serialize, "[{\"name\":\"Лемонад\",\"price\":70,\"count\":2,\"categories\":[\"Напитки\"]}," +
                "{\"name\":\"Толстовка\",\"price\":6000,\"count\":10,\"categories\":[\"Куртки\",\"Одежда\"]}," +
                "{\"name\":\"Чай\",\"price\":50,\"count\":20,\"categories\":[\"Напитки\"]}]");
    }

    @Test
    void getAllProductsByOrderByCategory() throws Exception {
        ListOfCategoriesWithCookie data = initDb();
        List<CategoryParentDtoResponse> categories = data.getCategories();
        Cookie cookie = data.getCookie();
        MockHttpServletResponse response = mvc
                    .perform(get("/products?category={id},{id}&order=category",
                    categories.get(0).getId(), categories.get(3).getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        JavaType listType = mapper
                .getTypeFactory()
                .constructCollectionType(List.class, ProductInfo.class);
        List<ProductInfo> productsList = mapper
                .readValue(response.getContentAsString(), listType);
        String serialize = mapper.writeValueAsString(productsList);
        assertEquals(serialize, "[{\"name\":\"Толстовка\",\"price\":6000,\"count\":10,\"categories\":[\"Одежда\"]}," +
                "{\"name\":\"Лемонад\",\"price\":70,\"count\":2,\"categories\":[\"Напитки\"]}," +
                "{\"name\":\"Чай\",\"price\":50,\"count\":20,\"categories\":[\"Напитки\"]}]");
        response = mvc.perform(get("/products?order=category")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        productsList = mapper.readValue(response.getContentAsString(), listType);
        serialize = mapper.writeValueAsString(productsList);
        assertEquals("[{\"name\":\"Наушники\",\"price\":10000,\"count\":3,\"categories\":[]}," +
                "{\"name\":\"Ветрока\",\"price\":4000,\"count\":5,\"categories\":[\"Куртки\"]}," +
                "{\"name\":\"Толстовка\",\"price\":6000,\"count\":10,\"categories\":[\"Куртки\"]}," +
                "{\"name\":\"Лемонад\",\"price\":70,\"count\":2,\"categories\":[\"Напитки\"]}," +
                "{\"name\":\"Чай\",\"price\":50,\"count\":20,\"categories\":[\"Напитки\"]}," +
                "{\"name\":\"Толстовка\",\"price\":6000,\"count\":10,\"categories\":[\"Одежда\"]}," +
                "{\"name\":\"Сыр\",\"price\":600,\"count\":0,\"categories\":[\"Продукты\"]}]", serialize);
        mvc.perform(get("/products?order=unknown")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message",
                        is("Incorrect sort order. Possible value: \"product\" or \"category\"")));
    }
}

