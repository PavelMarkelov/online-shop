package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDao extends JpaRepository<Category, Long> {
}
