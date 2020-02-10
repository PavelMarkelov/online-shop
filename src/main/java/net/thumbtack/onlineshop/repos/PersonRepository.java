package net.thumbtack.onlineshop.repos;

import net.thumbtack.onlineshop.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByLogin(String login);
}
