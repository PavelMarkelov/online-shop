package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.repos.PersonRepository;
import net.thumbtack.onlineshop.entities.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommonService {

    private static Logger logger = LoggerFactory.getLogger(CommonService.class);

    private final PersonRepository personRepository;

    public CommonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    public Person findByLoginAndPassword(String login, String password) {
        return null;
    }

    public Person findByLogin(String login) {
        return null;
    }
}

