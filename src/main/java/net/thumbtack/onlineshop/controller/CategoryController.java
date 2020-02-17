package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.CategoryDtoRequest;
import net.thumbtack.onlineshop.dto.Response.CategoryParentDtoResponse;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.service.CategoryService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
        checkAccess(auth);
        return categoryService.addCategory(addDto);
    }

    @GetMapping("/{id}")
    public CategoryParentDtoResponse getCategory(@PathVariable("id") Long id,
                                                   Authentication auth
    ) {
        checkAccess(auth);
        return categoryService.findCategoryById(id);
    }

    @PutMapping("/{id}")
    public CategoryParentDtoResponse editCategory(
            @PathVariable("id") Long id,
            @RequestBody CategoryDtoRequest editDto,
            Authentication auth
    ) {
        checkAccess(auth);
        return categoryService.updateCategory(id, editDto);
    }

    @DeleteMapping("/{id}")
    public String delCategory(
            @PathVariable("id") Long id,
            Authentication auth
    ) {
        checkAccess(auth);
        categoryService.deleteCategory(id);
        return "{}";
    }

    @GetMapping()
    public List<CategoryParentDtoResponse> getAllCategories(Authentication auth) {
        checkAccess(auth);
        return categoryService.findAllCategories();
    }

    private void checkAccess(Authentication auth) {
        if (auth == null)
            throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            throw new AccessDeniedException("");
    }
}
