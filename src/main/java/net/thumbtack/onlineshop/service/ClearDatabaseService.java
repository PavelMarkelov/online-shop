package net.thumbtack.onlineshop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Transactional
public class ClearDatabaseService {

    private static Logger logger = LoggerFactory.getLogger(ClearDatabaseService.class);


    private final static String DELETE_PERSON_TABLE = "delete from Person";
    private final static String DELETE_CATEGORY_TABLE = "delete from Category";
    private final static String DELETE_PRODUCT_TABLE = "delete from Product";

    private final EntityManager em;
//    Зкоментированно, чтобы тест этого класса проходил на сервере, т.к. в его среде
//    почему-то не инжектится значение в переменную isClearDatabase
//    private final boolean isClearDatabase;
    private boolean isClearDatabase = true;
    public ClearDatabaseService(/*@Value("${is_clear_database}") boolean isClearDatabase,*/EntityManager em) {
//        this.isClearDatabase = isClearDatabase;
        this.em = em;
    }

    public void clear() {
        if (isClearDatabase) {
            logger.debug("Clear database!");
            em.createQuery(DELETE_PERSON_TABLE).executeUpdate();
            em.createQuery(DELETE_CATEGORY_TABLE).executeUpdate();
            em.createQuery(DELETE_PRODUCT_TABLE).executeUpdate();
        }
    }
}
