package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.DepositDtoRequest;
import net.thumbtack.onlineshop.dto.Response.CustomerDtoResponse;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.service.PersonService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static net.thumbtack.onlineshop.securiry.CheckAccessPerson.checkAccessCustomer;

@RestController
@RequestMapping("/deposits")
public class DepositController {

    private final PersonService personService;

    public DepositController(PersonService personService) {
        this.personService = personService;
    }

    @PutMapping
    public CustomerDtoResponse depositingMoney(@RequestBody DepositDtoRequest depositDto,
                                               Authentication auth
    ) {
        checkAccessCustomer(auth);
        String login = auth.getPrincipal().toString();
        return personService.addMoney(depositDto, login);
    }

    @GetMapping
    public CustomerDtoResponse getDeposit(Authentication auth) {
        checkAccessCustomer(auth);
        String login = auth.getPrincipal().toString();
        return new CustomerDtoResponse((Person) personService.loadUserByUsername(login));
    }
}
