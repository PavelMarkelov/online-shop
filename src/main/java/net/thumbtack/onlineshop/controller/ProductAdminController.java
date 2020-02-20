package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.ProductDtoRequest;
import net.thumbtack.onlineshop.dto.Request.ProductEditDtoRequest;
import net.thumbtack.onlineshop.dto.Response.ProductDtoResponse;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static net.thumbtack.onlineshop.securiry.CheckAccessPerson.checkAccessAdmin;

@RestController
@RequestMapping("/products")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    public ProductDtoResponse addProduct(@Valid @RequestBody ProductDtoRequest request,
                                         Authentication auth
    ) {
        checkAccessAdmin(auth);
        return productService.addProduct(request);
    }

    @PutMapping("/{id}")
    public ProductDtoResponse editProduct(@PathVariable("id") Long id,
            @Valid @RequestBody ProductEditDtoRequest request,
                                          Authentication auth
    ) {
        checkAccessAdmin(auth);
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public String delProduct(@PathVariable("id") Long id, Authentication auth) {
        checkAccessAdmin(auth);
        productService.deleteProduct(id);
        return "{}";
    }
}