package net.thumbtack.onlineshop.service.serviceimpl;

import net.thumbtack.onlineshop.dao.PersonDao;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommonServiceImpl implements CommonService {

    private static Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    private final PersonDao personDao;

    public CommonServiceImpl(PersonDao personDao) {
        this.personDao = personDao;
    }


    @Override
    public Person findByLoginAndPassword(String login, String password) {
        return null;
    }

    @Override
    public Person findByLogin(String login) {
        return null;
    }
}

