package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.CategoryDtoRequest;
import net.thumbtack.onlineshop.dto.Request.ProductDtoRequest;
import net.thumbtack.onlineshop.dto.Request.ProductEditDtoRequest;
import net.thumbtack.onlineshop.dto.Response.ProductDtoResponse;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

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
        checkAccess(auth);
        return productService.addProduct(request);
    }

    @PutMapping("/{id}")
    public ProductDtoResponse editProduct(@PathVariable("id") Long id,
            @Valid @RequestBody ProductEditDtoRequest request,
                                          Authentication auth
    ) {
        checkAccess(auth);
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public String delProduct(@PathVariable("id") Long id, Authentication auth) {
        checkAccess(auth);
        productService.deleteProduct(id);
        return "{}";
    }


    private void checkAccess(Authentication auth) {
        if (auth == null)
            throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            throw new AccessDeniedException("");
    }
}