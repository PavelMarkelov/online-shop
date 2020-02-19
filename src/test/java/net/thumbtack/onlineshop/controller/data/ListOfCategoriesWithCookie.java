package net.thumbtack.onlineshop.controller.data;

import net.thumbtack.onlineshop.dto.Response.CategoryParentDtoResponse;

import javax.servlet.http.Cookie;
import java.util.List;

public class ListOfCategoriesWithCookie {

    private List<CategoryParentDtoResponse> categories;
    private Cookie cookie;

    public ListOfCategoriesWithCookie(List<CategoryParentDtoResponse> categories, Cookie cookie) {
        this.categories = categories;
        this.cookie = cookie;
    }

    public List<CategoryParentDtoResponse> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryParentDtoResponse> categories) {
        this.categories = categories;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }
}
