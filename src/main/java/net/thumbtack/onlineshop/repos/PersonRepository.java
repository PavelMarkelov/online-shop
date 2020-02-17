package net.thumbtack.onlineshop.repos;

import net.thumbtack.onlineshop.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByLogin(String login);
    Person findByLoginAndPassword(String login, String password);
    List<Person> findAllByEmailIsNotNull();
}
