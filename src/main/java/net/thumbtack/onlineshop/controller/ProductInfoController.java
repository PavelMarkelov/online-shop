package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.GetAllProductDtoRequest;
import net.thumbtack.onlineshop.dto.Request.GetReportDtoWithValid;
import net.thumbtack.onlineshop.dto.Response.ProductInfoDtoResponse;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.service.EmailService;
import net.thumbtack.onlineshop.service.PersonService;
import net.thumbtack.onlineshop.service.ProductService;
import net.thumbtack.onlineshop.utils.ReportInExcelGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static net.thumbtack.onlineshop.securiry.CheckAccessPerson.checkAccessAdmin;

@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
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
    public List<ProductInfoDtoResponse> getReportInExcelToEmail(@Valid @RequestBody GetReportDtoWithValid request,
                                          Authentication auth
    ) {
        checkAccessAdmin(auth);
        Person admin = personService.findByLogin(auth.getPrincipal().toString());
        List<ProductInfoDtoResponse> products = productService
                .getProductReport(request.getMinCount(), request.getMaxCount());
        if (!StringUtils.isEmpty(request.getEmail()))
            emailService.sendMessage(admin, request.getEmail(), products);
        return products;
    }

    @GetMapping(value = "/report/download")
    public void downloadReport(@RequestParam int minCount,
                                              @RequestParam int maxCount,
                                              Authentication auth,
                                              HttpServletResponse response
    ) throws IOException {
        checkAccessAdmin(auth);
        response.setHeader("Content-Encoding", "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=report.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        List<ProductInfoDtoResponse> products = productService.getProductReport(minCount, maxCount);

        try (OutputStream out = response.getOutputStream()) {
            out.write(ReportInExcelGenerator.generateData(products));
        }
    }
}
