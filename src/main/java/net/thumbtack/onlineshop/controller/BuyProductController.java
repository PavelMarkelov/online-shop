package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.BuyProductDtoRequest;
import net.thumbtack.onlineshop.dto.Response.BuyProductDtoResponse;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.securiry.CheckAccessPerson;
import net.thumbtack.onlineshop.service.PersonService;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchases")
public class BuyProductController {

    private final ProductService productService;

    public BuyProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public BuyProductDtoResponse buyProduct(@RequestBody BuyProductDtoRequest request,
                                            Authentication auth
    ) {
        CheckAccessPerson.checkAccessCustomer(auth);
        String login = auth.getPrincipal().toString();
        return productService.buyProduct(login, request);
    }
}
