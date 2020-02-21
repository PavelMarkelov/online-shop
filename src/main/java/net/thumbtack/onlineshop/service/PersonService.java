package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.Request.AdminDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.CustomerDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.DepositDtoRequest;
import net.thumbtack.onlineshop.dto.Request.LoginDtoRequest;
import net.thumbtack.onlineshop.dto.Request.editDto.AdminEditDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.editDto.CustomerDtoEditWithValid;
import net.thumbtack.onlineshop.dto.Response.AdminDtoResponse;
import net.thumbtack.onlineshop.dto.Response.CustomerDtoResponse;
import net.thumbtack.onlineshop.dto.Response.GetAllCustomerDtoResponse;
import net.thumbtack.onlineshop.entities.*;
import net.thumbtack.onlineshop.exception.FailPasswordException;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.exception.LoginExistException;
import net.thumbtack.onlineshop.repos.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PersonService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Person createAdmin(AdminDtoWithValid admin) {
        Person person = personRepository.findByLogin(admin.getLogin());
        if (person != null)
            throw new LoginExistException(GlobalExceptionErrorCode.LOGIN_EXIST);
        Person personForAdd = new Person(admin);
        personForAdd.addRole(Role.ADMIN);
        personForAdd.setPassword(passwordEncoder.encode(admin.getPassword()));
        Token token = new Token(UUID.randomUUID().toString());
        token.setPerson(personForAdd);
        personForAdd.setToken(token);
        return personRepository.save(personForAdd);
    }

    @Transactional(readOnly = true)
    public Person findByLogin(String login) {
        return personRepository.findByLogin(login);
    }

    @Transactional(readOnly = true)
    public List<GetAllCustomerDtoResponse> findAllCustomers() {
        List<GetAllCustomerDtoResponse> customers = new ArrayList<>();
        personRepository.findAllByEmailIsNotNull().forEach(person ->
                customers.add(new GetAllCustomerDtoResponse(person)));
        customers.sort(Comparator
                .comparing(GetAllCustomerDtoResponse::getFirstName)
                .thenComparing(GetAllCustomerDtoResponse::getLastName));
        return customers;
    }

    public AdminDtoResponse updateAdminProfile(Person admin, AdminEditDtoWithValid adminDto) {
        if (!passwordEncoder.matches(adminDto.getOldPassword(), admin.getPassword()))
            throw new FailPasswordException(GlobalExceptionErrorCode.BAD_PASSWORD);
        admin.setFirstName(adminDto.getFirstName());
        admin.setLastName(adminDto.getLastName());
        admin.setPatronymic(adminDto.getPatronymic());
        admin.setPosition(adminDto.getPosition());
        admin.setPassword(passwordEncoder.encode(adminDto.getNewPassword()));
        return new AdminDtoResponse(personRepository.save(admin));
    }

    public Person createCustomer(CustomerDtoWithValid customerDto) {
        Person person = personRepository.findByLogin(customerDto.getLogin());
        if (person != null)
            throw new LoginExistException(GlobalExceptionErrorCode.LOGIN_EXIST);
        Person customer = new Person(customerDto);
        customer.addRole(Role.USER);
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        Token token = new Token(UUID.randomUUID().toString());
        token.setPerson(customer);
        customer.setToken(token);
        Address address = new Address(customerDto.getAddress());
        address.setPerson(customer);
        customer.setAddress(address);
        Basket basket = new Basket();
        basket.setPerson(customer);
        customer.setBasket(basket);
        return personRepository.save(customer);
    }

    public CustomerDtoResponse updateCustomerProfile(Person customer,
                                                     CustomerDtoEditWithValid customerDto
                                                     ) {
        if (!passwordEncoder.matches(customerDto.getOldPassword(), customer.getPassword()))
            throw new IllegalArgumentException();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPatronymic(customerDto.getPatronymic());
        customer.setEmail(customerDto.getEmail());
        Address address = new Address(customerDto.getAddress());
        address.setId(customer.getId());
        address.setPerson(customer);
        customer.setAddress(address);
        customer.setPhone(customerDto.getPhone());
        customer.setPassword(passwordEncoder.encode(customerDto.getNewPassword()));
        return new CustomerDtoResponse(personRepository.save(customer));
    }

    public CustomerDtoResponse addMoney(DepositDtoRequest depositDto, String login) {
        Person customer = personRepository.findByLogin(login);
        customer.setDeposit(depositDto.getDeposit() + customer.getDeposit());
        return new CustomerDtoResponse(personRepository.save(customer));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return personRepository.findByLogin(login);

    }

    public Person getPersonForLogin(LoginDtoRequest loginDto) {
        Optional<Person> personOpt =  Optional.ofNullable(personRepository
                .findByLogin(loginDto.getLogin()));
        boolean matches = false;
        if (personOpt.isPresent()) {
            String password = personOpt.get().getPassword();
            matches = passwordEncoder.matches(loginDto.getPassword(), password);
        }
        if (!matches)
            throw new BadCredentialsException("Invalid login or password");
        Token token = new Token(UUID.randomUUID().toString());
        Person person = personOpt.get();
        token.setId(person.getId());
        token.setPerson(person);
        person.setToken(token);
        return personRepository.save(person);
    }
}
