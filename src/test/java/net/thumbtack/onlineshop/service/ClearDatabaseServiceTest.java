package net.thumbtack.onlineshop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClearDatabaseServiceTest {

    @Autowired
    private ClearDatabaseService clearDb;

    @Test
    void clear() {
        clearDb.clear();
    }
}