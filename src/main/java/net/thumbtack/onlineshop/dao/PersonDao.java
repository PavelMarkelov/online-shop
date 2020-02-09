package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonDao extends JpaRepository<Person, Long> {
}
