package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.CategoryDtoRequest;
import net.thumbtack.onlineshop.dto.Response.CategoryParentDtoResponse;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.service.CategoryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static net.thumbtack.onlineshop.securiry.CheckAccessPerson.checkAccessAdmin;

@RestController
@RequestMapping("/categories")
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
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
    public List<CategoryParentDtoResponse> getAllCategories(Principal principal) {
        Optional<Principal> name = Optional.ofNullable(principal);
        if (!name.isPresent())
            throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
        return categoryService.findAllCategories();
    }
}
