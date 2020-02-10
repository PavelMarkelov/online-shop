package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.AdminDto;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.exception.EmailExistException;
import net.thumbtack.onlineshop.service.AdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class SignUpAdminController {

    private final AdminService adminService;

    public SignUpAdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("admins")
    public Person regAdmin(@Valid @RequestBody AdminDto adminDto) throws EmailExistException {
        Person admin = adminService.createAdmin(adminDto);
        return admin;
    }
}
