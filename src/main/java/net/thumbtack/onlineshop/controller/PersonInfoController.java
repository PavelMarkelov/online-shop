package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Response.AdminDtoResponse;
import net.thumbtack.onlineshop.dto.Response.CustomerDtoResponse;
import net.thumbtack.onlineshop.dto.Response.PersonDtoResponse;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.service.PersonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class PersonInfoController {

    private final PersonService personService;

    public PersonInfoController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public PersonDtoResponse getPersonInfo(Principal principal) {
        Optional<Principal> name = Optional.ofNullable(principal);
        if (name.isPresent()) {
            Person person = personService.findByLogin(name.get().getName());
            return StringUtils.isEmpty(person.getEmail()) ? new AdminDtoResponse(person) : new CustomerDtoResponse(person);
        }
        throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
    }
}
