package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.CategoryDtoRequest;
import net.thumbtack.onlineshop.dto.Response.CategoryParentDtoResponse;
import net.thumbtack.onlineshop.service.CategoryService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static net.thumbtack.onlineshop.securiry.CheckAccessPerson.checkAccessAdmin;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping()
    public CategoryParentDtoResponse addCategory(@Valid @RequestBody CategoryDtoRequest addDto,
                                                 Authentication auth
    ) {
        checkAccessAdmin(auth);
        return categoryService.addCategory(addDto);
    }

    @GetMapping("/{id}")
    public CategoryParentDtoResponse getCategory(@PathVariable("id") Long id,
                                                   Authentication auth
    ) {
        checkAccessAdmin(auth);
        return categoryService.findCategoryById(id);
    }

    @PutMapping("/{id}")
    public CategoryParentDtoResponse editCategory(
            @PathVariable("id") Long id,
            @RequestBody CategoryDtoRequest editDto,
            Authentication auth
    ) {
        checkAccessAdmin(auth);
        return categoryService.updateCategory(id, editDto);
    }

    @DeleteMapping("/{id}")
    public String delCategory(
            @PathVariable("id") Long id,
            Authentication auth
    ) {
        checkAccessAdmin(auth);
        categoryService.deleteCategory(id);
        return "{}";
    }

    @GetMapping()
    public List<CategoryParentDtoResponse> getAllCategories(Authentication auth) {
        checkAccessAdmin(auth);
        return categoryService.findAllCategories();
    }
}
