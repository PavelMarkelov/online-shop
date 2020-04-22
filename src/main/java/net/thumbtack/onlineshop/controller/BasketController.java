package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.BuyProductDtoRequest;
import net.thumbtack.onlineshop.dto.Response.BuyProductDtoResponse;
import net.thumbtack.onlineshop.service.BasketService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static net.thumbtack.onlineshop.securiry.CheckAccessPerson.checkAccessCustomer;

@RestController
@RequestMapping("/baskets")
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
public class BasketController {

    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping
    private List<BuyProductDtoResponse> addItemToBasket(
            @RequestBody BuyProductDtoRequest request,
            Authentication auth
    ) {
        checkAccessCustomer(auth);
        String login = auth.getPrincipal().toString();
        return basketService.addToBasket(login, request);
    }

    @DeleteMapping("/{id}")
    public String delItemFromBasket(@PathVariable("id") long id, Authentication auth) {
        checkAccessCustomer(auth);
        String login = auth.getPrincipal().toString();
        basketService.deleteProductFromBasket(login, id);
        return "{}";
    }

    @PutMapping
    public List<BuyProductDtoResponse> changeQuantity(
            @RequestBody BuyProductDtoRequest request,
            Authentication auth
    ) {
        checkAccessCustomer(auth);
        String login = auth.getPrincipal().toString();
        return basketService.changingQuantity(login, request);
    }

    @GetMapping
    public List<BuyProductDtoResponse> getBasket(Authentication auth) {
        checkAccessCustomer(auth);
        String login = auth.getPrincipal().toString();
        return basketService.findBasketByPersonLogin(login);
    }
}
