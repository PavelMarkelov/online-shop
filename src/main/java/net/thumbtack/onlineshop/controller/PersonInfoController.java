package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.editDto.AdminEditDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.editDto.CustomerDtoEditWithValid;
import net.thumbtack.onlineshop.dto.Response.AdminDtoResponse;
import net.thumbtack.onlineshop.dto.Response.CustomerDtoResponse;
import net.thumbtack.onlineshop.dto.Response.GetAllCustomerDtoResponse;
import net.thumbtack.onlineshop.dto.Response.PersonDtoResponse;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.service.PersonService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
        if (auth == null)
            throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            return personService.findAllCustomers();
        throw new AccessDeniedException("");
    }

    @PutMapping("/admins")
    public AdminDtoResponse editAdmin(@Valid @RequestBody AdminEditDtoWithValid editDto,
                                           Authentication auth
    ) {
        if (auth == null)
            throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            Person admin = personService.findByLogin(auth.getPrincipal().toString());
            return personService.updateAdminProfile(admin, editDto);
        }
        throw new AccessDeniedException("");
    }

    @PutMapping("/clients")
    public CustomerDtoResponse editCustomer(@Valid @RequestBody CustomerDtoEditWithValid editDto,
                                            Authentication auth
    ) {
        if (auth == null)
            throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("USER"))) {
            Person customer = personService.findByLogin(auth.getPrincipal().toString());
            return personService.updateCustomerProfile(customer, editDto);
        }
        throw new AccessDeniedException("");

    }
}
