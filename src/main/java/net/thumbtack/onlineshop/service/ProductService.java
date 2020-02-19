package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.Request.ProductDtoRequest;
import net.thumbtack.onlineshop.dto.Request.ProductEditDtoRequest;
import net.thumbtack.onlineshop.dto.Response.ProductDtoResponse;
import net.thumbtack.onlineshop.dto.Response.ProductInfoDtoResponse;
import net.thumbtack.onlineshop.entities.Category;
import net.thumbtack.onlineshop.entities.Product;
import net.thumbtack.onlineshop.exception.CategoryNotFoundException;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.exception.IncorrectOrderException;
import net.thumbtack.onlineshop.exception.ProductNotFoundException;
import net.thumbtack.onlineshop.repos.CategoryRepository;
import net.thumbtack.onlineshop.repos.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductDtoResponse addProduct(@Valid ProductDtoRequest request) {
        Product product = new Product(request);
        if (request.getCategories() == null || request.getCategories().size() == 0)
            return new ProductDtoResponse(productRepository.save(product));
        List<Category> categories = new ArrayList<>();
        for (Long item : request.getCategories()) {
            Category category = categoryRepository.findCategoryById(item);
            if (category == null)
                throw new CategoryNotFoundException(GlobalExceptionErrorCode.CATEGORY_NOT_FOUND);
            categories.add(category);
        }
        product.setCategories(categories);
        productRepository.save(product);
        ProductDtoResponse response = new ProductDtoResponse(product);
        List<Long> categoryList = product.getCategories()
                .stream()
                .map(Category::getId)
                .sorted(Long::compareTo)
                .collect(Collectors.toList());
        response.setCategories(categoryList);
        return response;
    }

    public ProductDtoResponse updateProduct(Long id, ProductEditDtoRequest request) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (!productOpt.isPresent())
            throw new ProductNotFoundException(GlobalExceptionErrorCode.PRODUCT_NOT_FOUND);
        Product product = productOpt.get();
        if (request.getName() != null)
            product.setName(request.getName());
        if (request.getPrice() != null)
            product.setPrice(request.getPrice());
        if (request.getCount() != null)
            product.setCount(request.getCount());
        if (request.getCategories() != null) {
            if (request.getCategories().isEmpty()) {
                product.setCategories(Collections.emptyList());
            } else {
                List<Category> categories = new ArrayList<>();
                for (Long item : request.getCategories()) {
                    Category category = categoryRepository.findCategoryById(item);
                    if (category == null)
                        throw new CategoryNotFoundException(GlobalExceptionErrorCode.CATEGORY_NOT_FOUND);
                    categories.add(category);
                }
                product.setCategories(categories);
            }
        }
        ProductDtoResponse response = new ProductDtoResponse(product);
        List<Long> categoryList = product.getCategories()
                .stream()
                .map(Category::getId)
                .sorted(Long::compareTo)
                .collect(Collectors.toList());
        response.setCategories(categoryList);
        return response;
    }

    public void deleteProduct(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (!productOpt.isPresent())
            throw new ProductNotFoundException(GlobalExceptionErrorCode.PRODUCT_NOT_FOUND);
        productRepository.delete(productOpt.get());
    }

    @Transactional(readOnly = true)
    public ProductInfoDtoResponse findProductById(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (!productOpt.isPresent())
            throw new ProductNotFoundException(GlobalExceptionErrorCode.PRODUCT_NOT_FOUND);
        Product product = productOpt.get();
        ProductInfoDtoResponse response = new ProductInfoDtoResponse(product);
        List<String> categoryList = product.getCategories()
                .stream()
                .map(Category::getName)
                .sorted(String::compareTo)
                .collect(Collectors.toList());
        response.setCategories(categoryList);
        return response;
    }

    @Transactional(readOnly = true)
    public List<ProductInfoDtoResponse> findAllProducts(List<Long> categoriesId, String order) {
        List<Product> products = productRepository.findAllByOrderByNameAsc();
        List<ProductInfoDtoResponse> response = new ArrayList<>();
        if (categoriesId == null)
            products.forEach(product -> {
                ProductInfoDtoResponse item = new ProductInfoDtoResponse(product);
                List<String> categoryList = product.getCategories()
                        .stream()
                        .map(Category::getName)
                        .sorted(String::compareTo)
                        .collect(Collectors.toList());
                item.setCategories(categoryList);
                response.add(item);
            });
        else if (categoriesId.isEmpty())
            products.forEach(product -> {
                if (product.getCategories().isEmpty()) {
                    ProductInfoDtoResponse item = new ProductInfoDtoResponse(product);
                    List<String> categoryList = product.getCategories()
                            .stream()
                            .map(Category::getName)
                            .sorted(String::compareTo)
                            .collect(Collectors.toList());
                    item.setCategories(categoryList);
                    response.add(item);
                }});
        else {
            products.forEach(product -> {
                List<Long> categories = product.getCategories()
                        .stream()
                        .map(Category::getId)
                        .collect(Collectors.toList());
                categories.forEach(id -> {
                    if (categoriesId.contains(id)) {
                        ProductInfoDtoResponse item = new ProductInfoDtoResponse(product);
                        List<String> categoryList = product.getCategories()
                                .stream()
                                .map(Category::getName)
                                .sorted(String::compareTo)
                                .collect(Collectors.toList());
                        item.setCategories(categoryList);
                        response.add(item);
                    }
                });
            });
        }
        if (order == null || order.equals("product")) {
            Iterator<ProductInfoDtoResponse> iterator = response.listIterator();
            while (iterator.hasNext()) {
                ProductInfoDtoResponse item = iterator.next();
                int count = (int) response.stream()
                        .filter(elem -> elem.getName().equals(item.getName())).count();
                if (count > 1)
                    iterator.remove();
            }
        } else if (categoriesId != null && order.equals("category")) {
            response.clear();
            categoriesId.forEach(id -> products
                    .forEach(product -> product.getCategories()
                            .forEach(category -> {
                                if (category.getId().equals(id)) {
                                    ProductInfoDtoResponse item = new ProductInfoDtoResponse(product);
                                    List<String> categoryList = product.getCategories()
                                            .stream()
                                            .filter(category1 -> category1.getId().equals(id))
                                            .map(Category::getName)
                                            .collect(Collectors.toList());
                                    item.setCategories(categoryList);
                                    response.add(item);
                                }
                            })));
            return response;
        } else if (categoriesId == null && order.equals("category")) {
            List<ProductInfoDtoResponse> responseList = new ArrayList<>();
            Set<String> categories = new TreeSet<>();
            response.forEach(item -> {
                categories.addAll(item.getCategories());
                if (item.getCategories() == null || item.getCategories().isEmpty()) {
                    responseList.add(item);
                }
            });
            categories.forEach(name -> products
                    .forEach(product -> product.getCategories()
                            .forEach(category -> {
                                if (category.getName().equals(name)) {
                                    ProductInfoDtoResponse item = new ProductInfoDtoResponse(product);
                                    item.setCategories(new ArrayList<>());
                                    item.getCategories().add(name);
                                    responseList.add(item);
                                }
                            })));
            return responseList;
        }   else
            throw new IncorrectOrderException(GlobalExceptionErrorCode.BAD_ORDER);
        return response;
    }

    public Product buyProduct(Product product) {
        return null;
    }
}
