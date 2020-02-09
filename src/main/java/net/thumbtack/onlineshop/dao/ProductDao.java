package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {
}
