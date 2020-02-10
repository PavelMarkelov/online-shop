package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.AdminDto;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.exception.EmailExistException;
import net.thumbtack.onlineshop.exception.ExceptionErrorCode;
import net.thumbtack.onlineshop.repos.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Service
@Transactional
public class AdminService {

    private static Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Person createAdmin(@Valid AdminDto admin) throws EmailExistException {
        Person person = personRepository.findByLogin(admin.getLogin());
        if (person != null)
            throw new EmailExistException(ExceptionErrorCode.LOGIN_EXIST);
        Person personForAdd = new Person(admin);
        personForAdd.setPassword(passwordEncoder.encode(admin.getPassword()));
        return personRepository.save(personForAdd);
    }

    public List<Person> findAllCustomers() {
        return null;
    }

    public Person updateAdminProfile(Person admin) {
        return null;
    }
}
