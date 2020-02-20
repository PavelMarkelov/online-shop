package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.editDto.AdminEditDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.editDto.CustomerDtoEditWithValid;
import net.thumbtack.onlineshop.dto.Response.AdminDtoResponse;
import net.thumbtack.onlineshop.dto.Response.CustomerDtoResponse;
import net.thumbtack.onlineshop.dto.Response.GetAllCustomerDtoResponse;
import net.thumbtack.onlineshop.dto.Response.PersonDtoResponse;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.securiry.CheckAccessPerson;
import net.thumbtack.onlineshop.service.PersonService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static net.thumbtack.onlineshop.securiry.CheckAccessPerson.checkAccessCustomer;

@RestController
public class PersonInfoController {

    private final PersonService personService;

    public PersonInfoController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/accounts")
    public PersonDtoResponse getPersonInfo(Principal principal) {
        Optional<Principal> name = Optional.ofNullable(principal);
        if (name.isPresent()) {
            Person person = personService.findByLogin(name.get().getName());
            return StringUtils.isEmpty(person.getEmail()) ? new AdminDtoResponse(person) : new CustomerDtoResponse(person);
        }
        throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
    }

    @GetMapping("/clients")
    public List<GetAllCustomerDtoResponse> getAllCustomer(Authentication auth) {
        CheckAccessPerson.checkAccessAdmin(auth);
        return personService.findAllCustomers();
    }

    @PutMapping("/admins")
    public AdminDtoResponse editAdmin(@Valid @RequestBody AdminEditDtoWithValid editDto,
                                           Authentication auth
    ) {
        CheckAccessPerson.checkAccessAdmin(auth);
        Person admin = personService.findByLogin(auth.getPrincipal().toString());
        return personService.updateAdminProfile(admin, editDto);
    }

    @PutMapping("/clients")
    public CustomerDtoResponse editCustomer(@Valid @RequestBody CustomerDtoEditWithValid editDto,
                                            Authentication auth
    ) {
        checkAccessCustomer(auth);
        Person customer = personService.findByLogin(auth.getPrincipal().toString());
        return personService.updateCustomerProfile(customer, editDto);
    }
}
