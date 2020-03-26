package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.AdminDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.CustomerDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.LoginDtoRequest;
import net.thumbtack.onlineshop.dto.Response.AdminDtoResponse;
import net.thumbtack.onlineshop.dto.Response.CustomerDtoResponse;
import net.thumbtack.onlineshop.dto.Response.PersonDtoResponse;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.entities.Token;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.service.PersonService;
import net.thumbtack.onlineshop.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
public class SignUpAndLogoutController {

    private final PersonService personService;
    private final String cookieName;
    private final TokenService tokenService;

    public SignUpAndLogoutController(PersonService personService,
                                     @Value("${cookie_name}") String cookieName,
                                     TokenService tokenService) {
        this.personService = personService;
        this.cookieName = cookieName;
        this.tokenService = tokenService;
    }

    @PostMapping("/admins")
    public AdminDtoResponse regAdmin(@Valid @RequestBody AdminDtoWithValid adminDtoWithValid,
                                                     HttpServletResponse response
    ) {
        Person admin = personService.createAdmin(adminDtoWithValid);
        Cookie cookie = new Cookie(cookieName, admin.getTokenWithoutEncoding());
        response.addCookie(cookie);
        return new AdminDtoResponse(admin);
    }

    @PostMapping("/clients")
    public CustomerDtoResponse regCustomer(@Valid @RequestBody CustomerDtoWithValid customerDtoWithValid,
                                           HttpServletResponse response
    ) {
        Person customer = personService.createCustomer(customerDtoWithValid);
        Cookie cookie = new Cookie(cookieName, customer.getTokenWithoutEncoding());
        response.addCookie(cookie);
        return new CustomerDtoResponse(customer);
    }

    @PostMapping("/sessions")
    public PersonDtoResponse login(@RequestBody LoginDtoRequest loginDto,
                                   HttpServletResponse response
    ) {
        Person person = personService.getPersonForLogin(loginDto);
        Cookie cookie = new Cookie(cookieName, person.getTokenWithoutEncoding());
        response.addCookie(cookie);
        return StringUtils.isEmpty(person.getEmail()) ?
                new AdminDtoResponse(person) : new CustomerDtoResponse(person);
    }

    @DeleteMapping("/sessions")
    public String logout(HttpServletRequest request, Principal principal) {
        Optional<Principal> user = Optional.ofNullable(principal);
        if (!user.isPresent())
            throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
        Optional<Cookie> cookie = Optional.ofNullable(WebUtils.getCookie(request, cookieName));
        cookie.ifPresent(cookie1 -> {
            Optional<Token> token = tokenService.findUserByToken(cookie1.getValue());
            token.ifPresent(tokenService::updateToken);
        });
        return "{}";
    }
}
