package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Response.ProductInfoDtoResponse;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductInfoController {

    private final ProductService productService;

    public ProductInfoController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ProductInfoDtoResponse getProduct(@PathVariable("id") Long id, Principal principal) {
        Optional<Principal> name = Optional.ofNullable(principal);
        if (!name.isPresent())
            throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
        return productService.findProductById(id);
    }

    @GetMapping()
    public List<ProductInfoDtoResponse> getAllProductsByOrder(
            @RequestParam(required = false) List<Long> category,
            @RequestParam(required = false) String order,
            Principal principal
            ) {
        Optional<Principal> name = Optional.ofNullable(principal);
        if (!name.isPresent())
            throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
        return productService.findAllProducts(category, order);
    }
}
