package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.entities.Category;

import java.util.List;

public interface CategoryService {

    Category addCategory(Category category);
    Category findCategoryById(Long id);
    Category updateCategory(Long id);
    void deleteCategory(Long id);
    List<Category> findAllCategories();
}
