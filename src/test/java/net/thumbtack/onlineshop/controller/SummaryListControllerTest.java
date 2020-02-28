package net.thumbtack.onlineshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.thumbtack.onlineshop.dto.Request.AdminDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.PersonDtoWithValid;
import net.thumbtack.onlineshop.service.ClearDatabaseService;
import net.thumbtack.onlineshop.sorting.Sorter;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.io.IOException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "/reset-primary-key-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SummaryListControllerTest extends InitSummaryList {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClearDatabaseService clearDb;
    @Autowired
    private Sorter sorter;

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

    @BeforeEach
    void initTestDb() {
        super.initDatabase();
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

    private Cookie regAdmin(PersonDtoWithValid admin) throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/admins")
                .content(asJsonString(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        return response.getCookie(cookieName);
    }

    private String delDate(String response) throws JsonProcessingException {
        JsonNode rootNode = mapper.readTree(response);
        JsonNode locatedNode = rootNode.path("data");
        locatedNode.forEach(jsonNode -> ((ObjectNode) jsonNode).remove("purchaseDate"));
        return locatedNode.toString();
    }

    @Test
    void getWithDefaultOptions() throws Exception {
        Cookie cookie = regAdmin(admin);
        MockHttpServletResponse response = mvc.perform(get("/purchases/")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalPages", is((int) Math.round(17.0 / sorter.getLimit()))))
                .andExpect(jsonPath("currentPage", is(1)))
                .andExpect(jsonPath("totalRecords", is(17)))
                .andExpect(jsonPath("data[*]", hasSize(sorter.getLimit())))
                .andReturn()
                .getResponse();
        String content = delDate(response.getContentAsString());
        assertTrue(content.contains("[{\"categories\":[{\"name\":\"Фотоаппараты\"}]," +
                "\"product\":{\"id\":2},\"person\":{\"id\":1}," +
                "\"name\":\"Canon Mark 2\",\"price\":120000,\"count\":1,\"total\":120000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":2}," +
                "\"person\":{\"id\":2}," +
                "\"name\":\"Canon Mark 2\",\"price\":120000,\"count\":2,\"total\":240000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":1}," +
                "\"person\":{\"id\":1},\"name\":\"Nikon D700\"," +
                "\"price\":80000,\"count\":1,\"total\":80000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":1}," +
                "\"person\":{\"id\":1},\"name\":\"Nikon D700\"," +
                "\"price\":80000,\"count\":1,\"total\":80000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":1}," +
                "\"person\":{\"id\":2},\"name\":\"Nikon D700\"," +
                "\"price\":80000,\"count\":1,\"total\":80000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":1}," +
                "\"person\":{\"id\":2},\"name\":\"Nikon D700\"," +
                "\"price\":80000,\"count\":3,\"total\":240000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":3}," +
                "\"person\":{\"id\":1},\"name\":\"Sony A-7S\"," +
                "\"price\":90000,\"count\":3,\"total\":270000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":3}," +
                "\"person\":{\"id\":2},\"name\":\"Sony A-7S\"," +
                "\"price\":90000,\"count\":4,\"total\":360000}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":1},\"name\":\"Апельсин\"," +
                "\"price\":10,\"count\":6,\"total\":60}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":1},\"name\":\"Апельсин\"," +
                "\"price\":10,\"count\":12,\"total\":120}]"));
    }

    @Test
    void offsetAndLimitParamTest() throws Exception {
        Cookie cookie = regAdmin(admin);
        int offset = 9, limit = 16;
        MockHttpServletResponse response = mvc
                .perform(get("/purchases/?offset={offset}&limit={limit}", offset, limit)
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalPages", is((int) Math.round(17.0 / (limit - offset)))))
                .andExpect(jsonPath("currentPage", is(1)))
                .andExpect(jsonPath("totalRecords", is(17)))
                .andExpect(jsonPath("data[*]", hasSize(limit - offset + 1)))
                .andReturn()
                .getResponse();
        String content = delDate(response.getContentAsString());
        assertTrue(content.contains("[{\"categories\":[{\"name\":\"Продукты\"}]," +
                "\"product\":{\"id\":5},\"person\":{\"id\":1},\"name\":\"Апельсин\"," +
                "\"price\":10,\"count\":12,\"total\":120}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":2},\"name\":\"Апельсин\",\"price\":10,\"count\":12," +
                "\"total\":120}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":2},\"name\":\"Апельсин\",\"price\":10,\"count\":20," +
                "\"total\":200}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"},{\"name\":\"Продукты\"}]," +
                "\"product\":{\"id\":4},\"person\":{\"id\":1},\"name\":\"Сыр\"," +
                "\"price\":800,\"count\":6,\"total\":4800}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"},{\"name\":\"Продукты\"}]," +
                "\"product\":{\"id\":4},\"person\":{\"id\":2},\"name\":\"Сыр\"," +
                "\"price\":800,\"count\":12,\"total\":9600}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":6}," +
                "\"person\":{\"id\":2},\"name\":\"Хлеб\",\"price\":25,\"count\":4," +
                "\"total\":100}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":6}," +
                "\"person\":{\"id\":1},\"name\":\"Хлеб\",\"price\":25,\"count\":10," +
                "\"total\":250}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":6}," +
                "\"person\":{\"id\":1},\"name\":\"Хлеб\",\"price\":25,\"count\":10," +
                "\"total\":250}]"));
    }

    @Test
    void getOnlyTotalPrice() throws Exception {
        Cookie cookie = regAdmin(admin);
        MockHttpServletResponse response = mvc.perform(get("/purchases/?total=true&limit=17")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        assertEquals("{\"totalSum\":" + totalSum + "}", content);
    }

    @Test
    void sortByCountAndPriceAscTest() throws Exception {
        Cookie cookie = regAdmin(admin);
        MockHttpServletResponse response = mvc
                .perform(get("/purchases/?sort=count&offset=5&limit=14")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        String content = delDate(response.getContentAsString());
        assertTrue(content.contains("[{\"categories\":[{\"name\":\"Фотоаппараты\"}]," +
                "\"product\":{\"id\":1},\"person\":{\"id\":2},\"name\":\"Nikon D700\"," +
                "\"price\":80000,\"count\":3,\"total\":240000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":3}," +
                "\"person\":{\"id\":1},\"name\":\"Sony A-7S\",\"price\":90000,\"count\":3," +
                "\"total\":270000}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":6}," +
                "\"person\":{\"id\":2},\"name\":\"Хлеб\",\"price\":25,\"count\":4," +
                "\"total\":100},{\"categories\":[{\"name\":\"Фотоаппараты\"}]," +
                "\"product\":{\"id\":3},\"person\":{\"id\":2},\"name\":\"Sony A-7S\"," +
                "\"price\":90000,\"count\":4,\"total\":360000}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":1},\"name\":\"Апельсин\",\"price\":10,\"count\":6," +
                "\"total\":60}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"},{\"name\":\"Продукты\"}]," +
                "\"product\":{\"id\":4},\"person\":{\"id\":1},\"name\":\"Сыр\"," +
                "\"price\":800,\"count\":6,\"total\":4800}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":6}," +
                "\"person\":{\"id\":1},\"name\":\"Хлеб\",\"price\":25,\"count\":10," +
                "\"total\":250}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":6}," +
                "\"person\":{\"id\":1},\"name\":\"Хлеб\",\"price\":25,\"count\":10," +
                "\"total\":250}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":1},\"name\":\"Апельсин\",\"price\":10,\"count\":12," +
                "\"total\":120}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":2},\"name\":\"Апельсин\",\"price\":10,\"count\":12," +
                "\"total\":120}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"},{\"name\":\"Продукты\"}]," +
                "\"product\":{\"id\":4},\"person\":{\"id\":2},\"name\":\"Сыр\"," +
                "\"price\":800,\"count\":12,\"total\":9600}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":2},\"name\":\"Апельсин\",\"price\":10,\"count\":20," +
                "\"total\":200}]"));
    }

    @Test
    void sortByCountAndPriceDescTest() throws Exception {
        Cookie cookie = regAdmin(admin);
        MockHttpServletResponse response = mvc
                .perform(get("/purchases/?order=desc&sort=count")
                        .cookie(cookie)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        String content = delDate(response.getContentAsString());
        assertTrue(content.contains("[{\"categories\":[{\"name\":\"Продукты\"}]," +
                "\"product\":{\"id\":5},\"person\":{\"id\":2},\"name\":\"Апельсин\"," +
                "\"price\":10,\"count\":20,\"total\":200}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"},{\"name\":\"Продукты\"}]," +
                "\"product\":{\"id\":4},\"person\":{\"id\":2},\"name\":\"Сыр\"," +
                "\"price\":800,\"count\":12,\"total\":9600}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":2},\"name\":\"Апельсин\",\"price\":10,\"count\":12," +
                "\"total\":120}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":1},\"name\":\"Апельсин\",\"price\":10,\"count\":12," +
                "\"total\":120}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":6}," +
                "\"person\":{\"id\":1},\"name\":\"Хлеб\",\"price\":25,\"count\":10," +
                "\"total\":250}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":6}," +
                "\"person\":{\"id\":1},\"name\":\"Хлеб\",\"price\":25,\"count\":10," +
                "\"total\":250}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"},{\"name\":\"Продукты\"}]," +
                "\"product\":{\"id\":4},\"person\":{\"id\":1},\"name\":\"Сыр\"," +
                "\"price\":800,\"count\":6,\"total\":4800}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":1},\"name\":\"Апельсин\",\"price\":10,\"count\":6," +
                "\"total\":60}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":3}," +
                "\"person\":{\"id\":2},\"name\":\"Sony A-7S\",\"price\":90000," +
                "\"count\":4,\"total\":360000}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":6}," +
                "\"person\":{\"id\":2},\"name\":\"Хлеб\",\"price\":25,\"count\":4," +
                "\"total\":100}]"));
    }

    @Test
    void getPurchasesByTimeTest() throws Exception {
        Cookie cookie = regAdmin(admin);
        mvc
                .perform(get("/purchases/?limit=17&time=1")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalRecords", is(12)));
   }

    @Test
    void getPurchasesByCategoriesTest() throws Exception {
        Cookie cookie = regAdmin(admin);
        MockHttpServletResponse response = mvc
                .perform(get("/purchases/?category={category}&limit=17", category1.getId())
                        .cookie(cookie)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalRecords", is(9)))
                .andReturn()
                .getResponse();
        String content = delDate(response.getContentAsString());
        assertTrue(content.contains("[{\"categories\":[{\"name\":\"Продукты\"}]," +
                "\"product\":{\"id\":5},\"person\":{\"id\":1},\"name\":\"Апельсин\"," +
                "\"price\":10,\"count\":6,\"total\":60}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":1},\"name\":\"Апельсин\",\"price\":10,\"count\":12," +
                "\"total\":120}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":2},\"name\":\"Апельсин\",\"price\":10," +
                "\"count\":12,\"total\":120}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":2},\"name\":\"Апельсин\",\"price\":10,\"count\":20," +
                "\"total\":200}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"},{\"name\":\"Продукты\"}]," +
                "\"product\":{\"id\":4},\"person\":{\"id\":1},\"name\":\"Сыр\"," +
                "\"price\":800,\"count\":6,\"total\":4800}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"},{\"name\":\"Продукты\"}]," +
                "\"product\":{\"id\":4},\"person\":{\"id\":2},\"name\":\"Сыр\"," +
                "\"price\":800,\"count\":12,\"total\":9600}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":6}," +
                "\"person\":{\"id\":2},\"name\":\"Хлеб\",\"price\":25,\"count\":4," +
                "\"total\":100}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":6}," +
                "\"person\":{\"id\":1},\"name\":\"Хлеб\",\"price\":25," +
                "\"count\":10,\"total\":250}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":6}," +
                "\"person\":{\"id\":1},\"name\":\"Хлеб\",\"price\":25,\"count\":10," +
                "\"total\":250}]"));
    }

    @Test
    void getPurchasesByProductsTest() throws Exception {
        Cookie cookie = regAdmin(admin);
        MockHttpServletResponse response = mvc
                .perform(get("/purchases/?product={id},{id},{id}",
                        product2.getId(), product.getId(), product4.getId())
                        .cookie(cookie)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalRecords", is(10)))
                .andReturn()
                .getResponse();
        String content = delDate(response.getContentAsString());
        assertTrue(content.contains("[{\"categories\":[{\"name\":\"Фотоаппараты\"}]," +
                "\"product\":{\"id\":1},\"person\":{\"id\":1},\"name\":\"Nikon D700\"," +
                "\"price\":80000,\"count\":1,\"total\":80000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":1}," +
                "\"person\":{\"id\":1},\"name\":\"Nikon D700\",\"price\":80000," +
                "\"count\":1,\"total\":80000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":1}," +
                "\"person\":{\"id\":2},\"name\":\"Nikon D700\",\"price\":80000," +
                "\"count\":1,\"total\":80000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":1}," +
                "\"person\":{\"id\":2},\"name\":\"Nikon D700\",\"price\":80000,\"count\":3" +
                ",\"total\":240000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":3}," +
                "\"person\":{\"id\":1},\"name\":\"Sony A-7S\",\"price\":90000,\"count\":3," +
                "\"total\":270000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":3}," +
                "\"person\":{\"id\":2},\"name\":\"Sony A-7S\",\"price\":90000,\"count\":4," +
                "\"total\":360000}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":1},\"name\":\"Апельсин\",\"price\":10,\"count\":6," +
                "\"total\":60}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":1},\"name\":\"Апельсин\",\"price\":10,\"count\":12," +
                "\"total\":120}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":2},\"name\":\"Апельсин\",\"price\":10,\"count\":12," +
                "\"total\":120}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":2},\"name\":\"Апельсин\",\"price\":10,\"count\":20," +
                "\"total\":200}]"));
    }

    @Test
    void getPurchasesByCustomersWithAllParamsTest() throws Exception {
        Cookie cookie = regAdmin(admin);
        MockHttpServletResponse response = mvc
                .perform(get("/purchases/?customer={id}&offset=1&limit=25&" +
                                "order=desc&sort=count&time=7",
                        customer1.getId())
                        .cookie(cookie)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalRecords", is(8)))
                .andExpect(jsonPath("data[*]", hasSize(7)))
                .andReturn()
                .getResponse();
        String content = delDate(response.getContentAsString());
        assertTrue(content.contains("[{\"categories\":[{\"name\":\"Фотоаппараты\"}," +
                "{\"name\":\"Продукты\"}],\"product\":{\"id\":4},\"person\":{\"id\":2}," +
                "\"name\":\"Сыр\",\"price\":800,\"count\":12,\"total\":9600}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":5}," +
                "\"person\":{\"id\":2},\"name\":\"Апельсин\",\"price\":10,\"count\":12," +
                "\"total\":120}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":3}," +
                "\"person\":{\"id\":2},\"name\":\"Sony A-7S\",\"price\":90000,\"count\":4," +
                "\"total\":360000}," +
                "{\"categories\":[{\"name\":\"Продукты\"}],\"product\":{\"id\":6}," +
                "\"person\":{\"id\":2},\"name\":\"Хлеб\",\"price\":25,\"count\":4," +
                "\"total\":100}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":1}," +
                "\"person\":{\"id\":2},\"name\":\"Nikon D700\",\"price\":80000," +
                "\"count\":3,\"total\":240000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":2}," +
                "\"person\":{\"id\":2},\"name\":\"Canon Mark 2\",\"price\":120000," +
                "\"count\":2,\"total\":240000}," +
                "{\"categories\":[{\"name\":\"Фотоаппараты\"}],\"product\":{\"id\":1}," +
                "\"person\":{\"id\":2},\"name\":\"Nikon D700\",\"price\":80000," +
                "\"count\":1,\"total\":80000}]"));
    }
}