package net.thumbtack.onlineshop.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.Request.AdminDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.CategoryDtoRequest;
import net.thumbtack.onlineshop.dto.Request.PersonDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.ProductDtoRequest;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest()
@AutoConfigureMockMvc
class CategoryControllerTest {

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

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Cookie regAdmin(PersonDtoWithValid admin) throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/admins")
                .content(asJsonString(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        return response.getCookie(cookieName);
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

    @AfterEach
    void initAfterPersons() {
        clearDb.clear();
    }

    @Test
    void addCategory() throws Exception {
        Cookie cookie = regAdmin(admin);
        CategoryDtoRequest category = new CategoryDtoRequest("", 0);
        MockHttpServletResponse response = saveCategory(category, cookie);
        cookie = response.getCookie(cookieName);
        ValidationErrorDto errorsDto = mapper.readValue(response.getContentAsString(), ValidationErrorDto.class);
        FieldErrorDto errorDto = errorsDto.getErrors().get(0);
        assertEquals(errorDto.getErrorCode(), GlobalExceptionErrorCode.FORM_ERROR.name());
        assertEquals(errorDto.getField(), "name");
        assertEquals(errorDto.getMessage(), "Category name can't be empty");
        category.setName("Одежда");
        response = saveCategory(category, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse categoryDto = mapper.readValue(response.getContentAsString(), CategoryParentDtoResponse.class);
        assertTrue(categoryDto.getId() > 0);
        assertEquals(categoryDto.getName(), category.getName());
        response = saveCategory(category, cookie);
        cookie = response.getCookie(cookieName);
        errorDto = mapper.readValue(response.getContentAsString(), FieldErrorDto.class);
        assertEquals(errorDto.getMessage(), GlobalExceptionErrorCode.CATEGORY_EXIST.getErrorString());
        CategoryDtoRequest childCategory = new CategoryDtoRequest("Обувь", categoryDto.getId());
        response = saveCategory(childCategory, cookie);
        CategoryChildrenDtoResponse categoryDto1 = mapper.readValue(response.getContentAsString(), CategoryChildrenDtoResponse.class);
        assertTrue(categoryDto1.getId() > 0);
        assertEquals(categoryDto1.getParentId(), childCategory.getParentId());
        assertEquals(categoryDto1.getName(), childCategory.getName());
        assertEquals(categoryDto1.getParentName(), category.getName());
    }

    @Test
    void getCategory() throws Exception {
        Cookie cookie = regAdmin(admin);
        CategoryDtoRequest category = new CategoryDtoRequest("Одежда", 0);
        MockHttpServletResponse response = saveCategory(category, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse parentCategory = mapper.readValue(response.getContentAsString(), CategoryParentDtoResponse.class);
        CategoryDtoRequest childCategory = new CategoryDtoRequest("Обувь", parentCategory.getId());
        response = saveCategory(childCategory, cookie);
        cookie = response.getCookie(cookieName);
        CategoryChildrenDtoResponse categoryDto1 = mapper.readValue(response.getContentAsString(), CategoryChildrenDtoResponse.class);
        response = mvc.perform(get("/categories/{id}", parentCategory.getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) parentCategory.getId())))
                .andExpect(jsonPath("name", is(parentCategory.getName())))
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        mvc.perform(get("/categories/{id}", categoryDto1.getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) categoryDto1.getId())))
                .andExpect(jsonPath("name", is(categoryDto1.getName())))
                .andExpect(jsonPath("parentId", is((int) parentCategory.getId())))
                .andExpect(jsonPath("parentName", is(parentCategory.getName())));
    }

    @Test
    void editCategory() throws Exception {
        Cookie cookie = regAdmin(admin);
        CategoryDtoRequest category = new CategoryDtoRequest("Одежда", 0);
        MockHttpServletResponse response = saveCategory(category, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse parentCategory = mapper.readValue(response.getContentAsString(), CategoryParentDtoResponse.class);
        CategoryDtoRequest category1 = new CategoryDtoRequest("Продукты", 0);
        response = saveCategory(category1, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse parentCategory1 = mapper.readValue(response.getContentAsString(), CategoryParentDtoResponse.class);
        CategoryDtoRequest category2 = new CategoryDtoRequest("Обувь", parentCategory.getId());
        response = saveCategory(category2, cookie);
        cookie = response.getCookie(cookieName);
        CategoryChildrenDtoResponse childCategory = mapper.readValue(response.getContentAsString(), CategoryChildrenDtoResponse.class);
        Set<Long> childCategoryId = Collections.singleton(childCategory.getId());
        ProductDtoRequest product = new ProductDtoRequest("Сыр", 600, 10, childCategoryId);
        response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        ProductDtoResponse productResp = mapper.readValue(response.getContentAsString(), ProductDtoResponse.class);
        category.setName(null);
        category.setParentId(childCategory.getId());
        response = mvc.perform(put("/categories/{id}", parentCategory.getId())
                .content(asJsonString(category))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andReturn()
                .getResponse();
        assertEquals(response.getContentAsString(), "{\"errorCode\":\"PARENT_CATEGORY\"," +
                "\"field\":\"parentId\",\"message\":\"Forbidden to move a category to a subcategory\"}");
        category.setName("Одежда");
        cookie = response.getCookie(cookieName);
        CategoryDtoRequest category3 = new CategoryDtoRequest("Конфеты", parentCategory1.getId());
        response = saveCategory(category3, cookie);
        cookie = response.getCookie(cookieName);
        category3.setParentId(childCategory.getId());
        category3.setName(null);
        response = mvc.perform(put("/categories/{id}", childCategory.getId())
                .content(asJsonString(category3))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        category3.setName("Конфеты");
        assertEquals(response.getContentAsString(), "{\"errorCode\":\"CHILD_CATEGORY\"," +
                "\"field\":\"parentId\",\"message\":\"Forbidden add child category to a subcategory\"}");
//        Проверяем, можно ли подкатегорию переместить в категорию
        category2.setName("Молочные продукты");
        category2.setParentId(null);
        response = mvc.perform(put("/categories/{id}", childCategory.getId())
                .content(asJsonString(category2))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        assertEquals(response.getContentAsString(), "{\"errorCode\":\"ERROR_CHILD\"," +
                "\"field\":\"parentId\",\"message\":\"Forbidden to move a subcategory to a category\"}");
        category2.setParentId(parentCategory1.getId());
        response = mvc.perform(put("/categories/{id}", childCategory.getId())
                .content(asJsonString(category2))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        childCategory = mapper.readValue(response.getContentAsString(), CategoryChildrenDtoResponse.class);
        assertEquals(childCategory.getName(), category2.getName());
        assertEquals(childCategory.getParentId(), parentCategory1.getId());
        assertEquals(childCategory.getParentName(), parentCategory1.getName());
//        Проверяем остался ли товар в перемещенной категории
        mvc.perform(get("/products/{id}", productResp.getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("name", is(product.getName())))
                .andExpect(jsonPath("price", is(product.getPrice())))
                .andExpect(jsonPath("count", is(product.getCount())))
                .andExpect(jsonPath("$.categories[*]", hasItems(category2.getName())));
    }

    @Test
    void delCategory() throws Exception {
//  Добавляем в бд 2 категории, 2 подкатегории и 1 товар
        Cookie cookie = regAdmin(admin);
        CategoryDtoRequest category = new CategoryDtoRequest("Одежда", 0);
        MockHttpServletResponse response = saveCategory(category, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse parentCategory = mapper.readValue(response.getContentAsString(), CategoryParentDtoResponse.class);
        CategoryDtoRequest category1 = new CategoryDtoRequest("Продукты", 0);
        response = saveCategory(category1, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse parentCategory1 = mapper.readValue(response.getContentAsString(), CategoryParentDtoResponse.class);
        CategoryDtoRequest category2 = new CategoryDtoRequest("Куртки", parentCategory.getId());
        response = saveCategory(category2, cookie);
        cookie = response.getCookie(cookieName);
        CategoryChildrenDtoResponse childCategory = mapper.readValue(response.getContentAsString(), CategoryChildrenDtoResponse.class);
        CategoryDtoRequest category3 = new CategoryDtoRequest("Напитки", parentCategory1.getId());
        response = saveCategory(category3, cookie);
        cookie = response.getCookie(cookieName);
        CategoryChildrenDtoResponse childCategory1 = mapper.readValue(response.getContentAsString(), CategoryChildrenDtoResponse.class);
        Set<Long> childCategoryId = Collections.singleton(childCategory1.getId());
        ProductDtoRequest product = new ProductDtoRequest("Кофе", 600, 10, childCategoryId);
        response = mvc.perform(post("/products")
                .cookie(cookie)
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        ProductDtoResponse productResp = mapper.readValue(response.getContentAsString(), ProductDtoResponse.class);
//       Удаляем категорию Одежда, ожидаем, что ее подкатегория тоже удалится
        response = mvc.perform(delete("/categories/{id}", parentCategory.getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        response = mvc.perform(get("/categories/{id}", parentCategory.getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        assertEquals(response.getContentAsString(), "{\"errorCode\":\"CATEGORY_NOT_FOUND\"," +
                "\"field\":\"id\",\"message\":\"Category with the specified id not exists\"}");
        response = mvc.perform(get("/categories/{id}", childCategory.getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
//        Удаляем подкатегорию "напитки", ожидаем, что категория "продукты" останется и товар тоже
        response = mvc.perform(delete("/categories/{id}", childCategory1.getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        response = mvc.perform(get("/categories/{id}", childCategory1.getId())
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();
        cookie = response.getCookie(cookieName);
        response = mvc.perform(get("/categories/{id}", parentCategory1.getId())
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
                .andExpect(status().isOk())
//        Товар не должен принадлежать никакой категории
                .andExpect(jsonPath("$.categories[*]", is(empty())));
    }

    @Test
    void getAllCategories() throws Exception {
        //  Добавляем в бд 2 категории, 4 подкатегории
        Cookie cookie = regAdmin(admin);
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
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse childCategory = mapper.readValue(response.getContentAsString(), CategoryChildrenDtoResponse.class);
        CategoryDtoRequest category3 = new CategoryDtoRequest("Куртки", parentCategory1.getId());
        response = saveCategory(category3, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse childCategory1 = mapper.readValue(response.getContentAsString(), CategoryChildrenDtoResponse.class);
        CategoryDtoRequest category4 = new CategoryDtoRequest("Бакалея", parentCategory.getId());
        response = saveCategory(category4, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse childCategory2 = mapper.readValue(response.getContentAsString(), CategoryChildrenDtoResponse.class);
        CategoryDtoRequest category5 = new CategoryDtoRequest("Ботинки", parentCategory1.getId());
        response = saveCategory(category5, cookie);
        cookie = response.getCookie(cookieName);
        CategoryParentDtoResponse childCategory3 = mapper.readValue(response.getContentAsString(), CategoryChildrenDtoResponse.class);
        response = mvc.perform(get("/categories/")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        JavaType listType = mapper
                .getTypeFactory()
                .constructCollectionType(List.class, CategoryChildrenDtoResponse.class);
        List<CategoryParentDtoResponse> categories = mapper
                .readValue(response.getContentAsString(), listType);
        assertEquals(categories.get(0).getName(), parentCategory1.getName());
        assertEquals(categories.get(1).getName(), childCategory3.getName());
        assertEquals(categories.get(2).getName(), childCategory1.getName());
        assertEquals(categories.get(3).getName(), parentCategory.getName());
        assertEquals(categories.get(4).getName(), childCategory2.getName());
        assertEquals(categories.get(5).getName(), childCategory.getName());
    }
}