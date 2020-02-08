package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.entities.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketDao extends JpaRepository<Basket, Long> {
}
