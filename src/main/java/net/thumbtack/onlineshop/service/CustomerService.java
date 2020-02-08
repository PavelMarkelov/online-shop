package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.entities.Person;

public interface CustomerService {

    Person createCustomer(Person customer);
    Person updateCustomerProfile(Person customer);
    Person addMoney(String login, Integer amount);

}
