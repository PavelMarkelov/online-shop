package net.thumbtack.onlineshop.controller.data;

import net.thumbtack.onlineshop.dto.Request.BuyProductDtoRequest;

import javax.servlet.http.Cookie;
import java.util.List;

public class ListOfProductsWithCookie {

    private List<BuyProductDtoRequest> request;
    private Cookie cookie;

    public ListOfProductsWithCookie(List<BuyProductDtoRequest> request, Cookie cookie) {
        this.request = request;
        this.cookie = cookie;
    }

    public List<BuyProductDtoRequest> getResponse() {
        return request;
    }

    public void setResponse(List<BuyProductDtoRequest> response) {
        this.request = response;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }
}
