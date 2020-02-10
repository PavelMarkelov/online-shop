package net.thumbtack.onlineshop.dto;

import net.thumbtack.onlineshop.utils.propfilecheck.CheckerException;
import net.thumbtack.onlineshop.utils.propfilecheck.PropertiesFileChecker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonDtoTest {

    private AdminDto admin = new AdminDto("Васи-лий", "Ива нов", "Иванович",
            "Иvanoв35", "sddsvwe34s", "Работник магазина");
    private UserDto user = new UserDto("Васи-лий", "Ива нов", "Иванович",
            "Иvanoв35", "sddsvwe34s", "sads@csdcq.ru", "4100008",
            "+7 (874) 756-56-38");

    @BeforeAll
    static void initAppProp() throws CheckerException, IOException {
        PropertiesFileChecker.check("application.yml");
    }
    @Test
    void adminDtoTrueTest() {
        assertTrue(admin.isFistNameTrue());
        assertTrue(admin.isLastNameTrue());
        assertTrue(admin.isPatronymicTrue());
        assertTrue(admin.isLoginTrue());
        assertTrue(admin.isPasswordTrue());
        assertTrue(admin.isPositionTrue());
    }

    @Test
    void userDtoTrueTest() {
        assertTrue(user.isAddressTrue());
        assertTrue(user.isEmailTrue());
        assertTrue(user.isPhoneTrue());
    }

    @Test
    void userDtoFalseTest() {
        user.setFirstName("Василiй");
        assertFalse(user.isFistNameTrue());
        user.setPatronymic("Васильевич Васильевич Васильевич Васильевич Васильевич");
        assertFalse(user.isPatronymicTrue());
        user.setLogin("Ива-нов");
        assertFalse(user.isLoginTrue());
        user.setPassword("adFvdf3");
        assertFalse(user.isPasswordTrue());
        user.setEmail("sads@csdcqru");
        assertFalse(user.isEmailTrue());
        user.setAddress("");
        assertFalse(user.isAddressTrue());
        user.setPhone("956 788 98923");
        assertFalse(user.isPhoneTrue());
    }

}