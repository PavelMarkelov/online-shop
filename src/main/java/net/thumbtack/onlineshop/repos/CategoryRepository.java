package net.thumbtack.onlineshop.repos;

import net.thumbtack.onlineshop.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
