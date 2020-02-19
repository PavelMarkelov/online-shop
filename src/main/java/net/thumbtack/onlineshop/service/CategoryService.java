package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.Request.CategoryDtoRequest;
import net.thumbtack.onlineshop.dto.Response.CategoryChildrenDtoResponse;
import net.thumbtack.onlineshop.dto.Response.CategoryParentDtoResponse;
import net.thumbtack.onlineshop.entities.Category;
import net.thumbtack.onlineshop.exception.CategoryExistException;
import net.thumbtack.onlineshop.exception.CategoryNotFoundException;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.exception.ParentCategoryException;
import net.thumbtack.onlineshop.repos.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryParentDtoResponse addCategory(CategoryDtoRequest categoryDto) {
        if (categoryRepository.findByName(categoryDto.getName()) != null)
            throw  new CategoryExistException(GlobalExceptionErrorCode.CATEGORY_EXIST);
        Category savedCategory;
        if (categoryDto.getParentId() != 0) {
            Optional<Category> parentCategoryOpt = Optional
                    .ofNullable(categoryRepository.findCategoryById(categoryDto.getParentId()));
            if (parentCategoryOpt.isPresent()) {
                Category parentCategory = parentCategoryOpt.get();
                if (parentCategory.getParentCategory() != null)
                    throw new ParentCategoryException(GlobalExceptionErrorCode.CHILD_CATEGORY);
                Category childrenCategory = new Category(categoryDto.getName());
                childrenCategory.setParentCategory(parentCategory);
                categoryRepository.save(childrenCategory);
                return new CategoryChildrenDtoResponse(childrenCategory.getId(),
                        childrenCategory.getName(), parentCategory.getId(), parentCategory.getName()
                );
            } else
                throw new ParentCategoryException(GlobalExceptionErrorCode.PARENT_CATEGORY_NOT_EXIST);
        }
        savedCategory = categoryRepository.save(new Category(categoryDto.getName()));
        return new CategoryParentDtoResponse(savedCategory.getId(), savedCategory.getName());
    }

    @Transactional(readOnly = true)
    public CategoryParentDtoResponse findCategoryById(Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (!categoryOpt.isPresent())
            throw new CategoryNotFoundException(GlobalExceptionErrorCode.CATEGORY_NOT_FOUND);
        Category category = categoryOpt.get();
        if (category.getParentCategory() == null)
            return new CategoryParentDtoResponse(category.getId(), category.getName());
        return new CategoryChildrenDtoResponse(category.getId(), category.getName(),
        category.getParentCategory().getId(), category.getParentCategory().getName());
    }

    public CategoryParentDtoResponse updateCategory(Long id, CategoryDtoRequest editDto) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (!categoryOpt.isPresent())
            throw new CategoryNotFoundException(GlobalExceptionErrorCode.CATEGORY_NOT_FOUND);
        Category category = categoryOpt.get();
        if (editDto.getName() != null) {
            if (categoryRepository.findByName(editDto.getName()) != null)
                throw  new CategoryExistException(GlobalExceptionErrorCode.CATEGORY_EXIST);
            category.setName(editDto.getName());
        }
        if (editDto.getParentId() != 0) {
            if (category.getParentCategory() == null)
                throw new ParentCategoryException(GlobalExceptionErrorCode.PARENT_CATEGORY);
            Optional<Category> newParentCategoryOpt = categoryRepository.findById(editDto.getParentId());
            if (!newParentCategoryOpt.isPresent())
                throw new CategoryNotFoundException(GlobalExceptionErrorCode.CATEGORY_NOT_FOUND);
            Category newParentCategory = newParentCategoryOpt.get();
            if (newParentCategory.getParentCategory() != null)
                throw new ParentCategoryException(GlobalExceptionErrorCode.CHILD_CATEGORY);
            category.setParentCategory(newParentCategory);
            categoryRepository.save(category);
            return new CategoryChildrenDtoResponse(category.getId(), category.getName(),
                    category.getParentCategory().getId(),
                    category.getParentCategory().getName());
        }   else if (category.getParentCategory() != null)
            throw new ParentCategoryException(GlobalExceptionErrorCode.ERROR_CHILD);
        categoryRepository.save(category);
        return new CategoryParentDtoResponse(category.getId(), category.getName());
    }

    public void deleteCategory(Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (!categoryOpt.isPresent())
            throw new CategoryNotFoundException(GlobalExceptionErrorCode.CATEGORY_NOT_FOUND);
        Category category = categoryOpt.get();
        category.getChildrenCategories().forEach(categoryRepository::delete);
        categoryRepository.delete(category);
    }

    public List<CategoryParentDtoResponse> findAllCategories() {
        List<CategoryParentDtoResponse> response = new ArrayList<>();
        for (Category item : categoryRepository.findAllByOrderByNameAsc()) {
            if (item.getParentCategory() == null) {
                response.add(new CategoryParentDtoResponse(item.getId(), item.getName()));
                item.getChildrenCategories()
                        .sort(Comparator.comparing(Category::getName));
                for (Category elem : item.getChildrenCategories())
                    response.add(new CategoryChildrenDtoResponse(elem.getId(), elem.getName(),
                            item.getId(), item.getName()));
            }
        }
        return response;
    }
}
