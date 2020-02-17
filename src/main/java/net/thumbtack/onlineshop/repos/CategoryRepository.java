package net.thumbtack.onlineshop.repos;

import net.thumbtack.onlineshop.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);
    Category findCategoryById(Long id);
    List<Category> findAllByOrderByNameAsc();
}
