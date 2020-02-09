package net.thumbtack.onlineshop.service.serviceimpl;

import net.thumbtack.onlineshop.service.ClearDatabaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClearDatabaseServiceImplTest {

    @Autowired
    private ClearDatabaseService clearDb;

    @Test
    void clear() {
        clearDb.clear();
    }
}