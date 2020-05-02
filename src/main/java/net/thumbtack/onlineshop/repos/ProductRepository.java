package net.thumbtack.onlineshop.repos;

import net.thumbtack.onlineshop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByNameAsc();
    List<Product> findByCountBetweenOrderByCountAscNameAsc(int minCount, int maxCount);
}
