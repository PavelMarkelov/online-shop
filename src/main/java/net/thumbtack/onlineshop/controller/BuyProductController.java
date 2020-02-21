package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.BuyProductDtoRequest;
import net.thumbtack.onlineshop.dto.Response.BuyBasketResponseDto;
import net.thumbtack.onlineshop.dto.Response.BuyProductDtoResponse;
import net.thumbtack.onlineshop.securiry.CheckAccessPerson;
import net.thumbtack.onlineshop.service.BasketService;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class BuyProductController {

    private final ProductService productService;
    private final BasketService basketService;

    public BuyProductController(ProductService productService, BasketService basketService) {
        this.productService = productService;
        this.basketService = basketService;
    }

    @PostMapping
    public BuyProductDtoResponse buyProduct(@RequestBody BuyProductDtoRequest request,
                                            Authentication auth
    ) {
        CheckAccessPerson.checkAccessCustomer(auth);
        String login = auth.getPrincipal().toString();
        return productService.buyProduct(login, request);
    }

    @PostMapping("/baskets")
    public BuyBasketResponseDto buyBasket(
            @RequestBody List<BuyProductDtoRequest> request,
            Authentication auth
    ) {
        CheckAccessPerson.checkAccessCustomer(auth);
        String login = auth.getPrincipal().toString();
        return basketService.buyingFromBasket(login, request);
    }

}
