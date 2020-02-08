package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.entities.Person;

public interface CommonService {

    Person findByLoginAndPassword(String login, String password);
    Person findByLogin(String login);
    void clearDatabase();
}
