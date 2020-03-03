package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.GetAllProductDtoRequest;
import net.thumbtack.onlineshop.dto.Request.GetReportDtoWithValid;
import net.thumbtack.onlineshop.dto.Response.ProductInfoDtoResponse;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.entities.Product;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.service.EmailService;
import net.thumbtack.onlineshop.service.PersonService;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static net.thumbtack.onlineshop.securiry.CheckAccessPerson.checkAccessAdmin;

@RestController
@RequestMapping("/products")
public class ProductInfoController {

    private final PersonService personService;
    private final ProductService productService;
    private final EmailService emailService;

    public ProductInfoController(PersonService personService,
                                 ProductService productService,
                                 EmailService emailService
    ) {
        this.personService = personService;
        this.productService = productService;
        this.emailService = emailService;
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
            GetAllProductDtoRequest request,
            Principal principal
            ) {
        Optional<Principal> name = Optional.ofNullable(principal);
        if (!name.isPresent())
            throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
        return productService.findAllProducts(request.getCategory(), request.getOrder());
    }

    @PostMapping("/report")
    public String getReportInExcelToEmail(@Valid @RequestBody GetReportDtoWithValid request,
                                          Authentication auth
    ) throws MessagingException, IOException {
        checkAccessAdmin(auth);
        Person admin = personService.findByLogin(auth.getPrincipal().toString());
        List<Product> products = productService.getProductReport(request.getCount());
        emailService.sendMessage(admin, request.getEmail(), products);
        return "{}";
    }
}
