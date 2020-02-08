package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.entities.Person;

import java.util.List;

public interface AdminService {

    Person createAdmin(Person admin);
    List<Person> findAllCustomers();
    Person updateAdminProfile(Person admin);

}
