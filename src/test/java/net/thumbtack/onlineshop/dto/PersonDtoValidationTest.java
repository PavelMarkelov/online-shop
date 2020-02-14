package net.thumbtack.onlineshop.dto;

import net.thumbtack.onlineshop.dto.Request.AdminDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.CustomerDtoWithValid;
import net.thumbtack.onlineshop.utils.propfilecheck.CheckerException;
import net.thumbtack.onlineshop.utils.propfilecheck.PropertiesFileChecker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonDtoValidationTest {

    private AdminDtoWithValid admin = new AdminDtoWithValid("Васи-лий", "Ива нов", "Иванович",
            "Иvanoв35", "sddsvwe34s", "Работник магазина");
    private CustomerDtoWithValid user = new CustomerDtoWithValid("Васи-лий", "Ива нов", "Иванович",
            "Иvanoв35", "sddsvwe34s", "sads@csdcq.ru", "4100008",
            "+7 (874) 756-56-38");

    @BeforeAll
    static void initAppProp() throws CheckerException, IOException {
        PropertiesFileChecker.check("application.yml");
    }
    @Test
    void testAdminHasValidFields() {
        assertTrue(admin.isFistName());
        assertTrue(admin.isLastName());
        assertTrue(admin.isPatronymic());
        assertTrue(admin.isLogin());
        assertTrue(admin.isPassword());
        assertTrue(admin.isPosition());
    }

    @Test
    void testCustomerHasValidFields() {
        assertTrue(user.isAddress());
        assertTrue(user.isEmail());
        assertTrue(user.isPhone());
    }

    @Test
    void testPersonHasFailValidFields() {
        user.setFirstName("Василiй");
        assertFalse(user.isFistName());
        user.setPatronymic("Васильевич Васильевич Васильевич Васильевич Васильевич");
        assertFalse(user.isPatronymic());
        user.setLogin("Ива-нов");
        assertFalse(user.isLogin());
        user.setPassword("adFvdf3");
        assertFalse(user.isPassword());
        user.setEmail("sads@csdcqru");
        assertFalse(user.isEmail());
        user.setAddress("");
        assertFalse(user.isAddress());
        user.setPhone("956 788 98923");
        assertFalse(user.isPhone());
    }

}