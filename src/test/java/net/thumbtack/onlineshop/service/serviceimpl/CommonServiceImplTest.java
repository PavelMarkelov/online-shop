package net.thumbtack.onlineshop.service.serviceimpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class CommonServiceImplTest {

    @Autowired
    private CommonServiceImpl commonService;

    @Test
    void clearDatabase() {
        commonService.clearDatabase();
    }
}