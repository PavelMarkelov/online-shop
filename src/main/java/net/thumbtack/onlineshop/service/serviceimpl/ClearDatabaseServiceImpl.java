package net.thumbtack.onlineshop.service.serviceimpl;

import net.thumbtack.onlineshop.service.ClearDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Transactional
@Repository
public class ClearDatabaseServiceImpl implements ClearDatabaseService {

    private static Logger logger = LoggerFactory.getLogger(ClearDatabaseServiceImpl.class);


    private final static String DELETE_PERSON_TABLE = "delete from Person";
    private final static String DELETE_CATEGORY_TABLE = "delete from Category";
    private final static String DELETE_PRODUCT_TABLE = "delete from Product";

    private final EntityManager em;
    private final boolean isClearDatabase;

    public ClearDatabaseServiceImpl(@Value("${is_clear_database}") boolean isClearDatabase, EntityManager em) {
        this.isClearDatabase = isClearDatabase;
        this.em = em;
    }

    @Override
    public void clear() {
        if (isClearDatabase) {
            logger.debug("Clear database!");
            em.createQuery(DELETE_PERSON_TABLE).executeUpdate();
            em.createQuery(DELETE_CATEGORY_TABLE).executeUpdate();
            em.createQuery(DELETE_PRODUCT_TABLE).executeUpdate();
        }
    }
}
