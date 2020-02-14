package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.Request.AdminDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.CustomerDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.LoginDtoRequest;
import net.thumbtack.onlineshop.dto.Response.AdminDtoResponse;
import net.thumbtack.onlineshop.dto.Response.CustomerDtoResponse;
import net.thumbtack.onlineshop.dto.Response.PersonDtoResponse;
import net.thumbtack.onlineshop.entities.*;
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
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Person findByLoginAndPassword(String login, String password) {
        return null;
    }

    public Person findByLogin(String login) {
        return personRepository.findByLogin(login);
    }

    public List<Person> findAllCustomers() {
        return null;
    }

    public Person updateAdminProfile(Person admin) {
        return null;
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
        return personRepository.save(customer);
    }

    public Person updateCustomerProfile(Person customer) {
        return null;
    }

    public Person addMoney(String login, Integer amount) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return personRepository.findByLogin(login);

    }

    public Person getPersonForLogin(LoginDtoRequest loginDto) {
        Optional<Person> personOpt =  Optional.ofNullable(personRepository
                .findByLogin(loginDto.getLogin()));
        boolean matches = false;
        if (personOpt.isPresent()) {
            String password = personOpt.get().getPassword();
            matches = passwordEncoder.matches(password, loginDto.getPassword());
        }
        if (!matches)
            personOpt.orElseThrow(() -> new BadCredentialsException("Invalid login or password"));
        Token token = new Token(UUID.randomUUID().toString());
        Person person = personOpt.get();
        token.setId(person.getId());
        token.setPerson(person);
        person.setToken(token);
        return personRepository.save(person);
    }
}
