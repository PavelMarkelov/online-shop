package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.Request.BuyProductDtoRequest;
import net.thumbtack.onlineshop.dto.Request.ProductDtoRequest;
import net.thumbtack.onlineshop.dto.Request.ProductEditDtoRequest;
import net.thumbtack.onlineshop.dto.Response.BuyProductDtoResponse;
import net.thumbtack.onlineshop.dto.Response.ProductDtoResponse;
import net.thumbtack.onlineshop.dto.Response.ProductInfoDtoResponse;
import net.thumbtack.onlineshop.entities.Category;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.entities.Product;
import net.thumbtack.onlineshop.entities.PurchaseHistory;
import net.thumbtack.onlineshop.exception.*;
import net.thumbtack.onlineshop.repos.CategoryRepository;
import net.thumbtack.onlineshop.repos.PersonRepository;
import net.thumbtack.onlineshop.repos.ProductRepository;
import net.thumbtack.onlineshop.repos.PurchaseHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PersonRepository personRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, PersonRepository personRepository, PurchaseHistoryRepository purchaseHistoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.personRepository = personRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
    }

    public ProductDtoResponse addProduct(ProductDtoRequest request) {
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
            products
                    .stream()
                    .filter(product -> product.getCategories().isEmpty())
                    .forEach(product -> response.add(new ProductInfoDtoResponse(product)));
        else
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
        if (order == null || order.equals("product")) {
            Iterator<ProductInfoDtoResponse> iterator = response.listIterator();
            while (iterator.hasNext()) {
                ProductInfoDtoResponse item = iterator.next();
                int count = (int) response.stream()
                        .filter(elem -> elem.getId() == (item.getId())).count();
                if (count > 1)
                    iterator.remove();
            }
        } else if (categoriesId != null && !categoriesId.isEmpty() && order.equals("category")) {
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
        }
        if (order!= null && !order.equals("product") && !order.equals("category"))
            throw new IncorrectOrderException(GlobalExceptionErrorCode.BAD_ORDER);
        return response;
    }

    public BuyProductDtoResponse buyProduct(String login, BuyProductDtoRequest request) {
        Optional<Product> productOpt = productRepository.findById(request.getId());
        if (!productOpt.isPresent())
            throw new ProductNotFoundException(GlobalExceptionErrorCode.PRODUCT_NOT_FOUND);
        Product product = productOpt.get();
        if (!request.getName().equals(product.getName()) || request.getPrice() != product.getPrice())
            throw new ProductNotFoundException(GlobalExceptionErrorCode.BUY_ERROR);
        int count = request.getCount();
        if (count <= 0)
            count = 1;
        if (product.getCount() < count)
            throw new ProductNotFoundException(GlobalExceptionErrorCode.ERROR_COUNT);
        Person customer = personRepository.findByLogin(login);
        if (customer.getDeposit() < count * product.getPrice())
            throw new NoMoneyException(GlobalExceptionErrorCode.NO_MONEY);
        product.setCount(product.getCount() - count);
        List<Category> categories = new ArrayList<>(product.getCategories());
        PurchaseHistory history = new PurchaseHistory(categories, product,
                customer, new Date(), product.getName(), product.getPrice(), count);
        purchaseHistoryRepository.save(history);
        productRepository.save(product);
        customer.setDeposit(customer.getDeposit() - count * request.getPrice());
        return new BuyProductDtoResponse(request.getId(), request.getName(),
                request.getPrice(), count);
    }

    public List<ProductInfoDtoResponse> getProductReport(int minCount, int maxCount) {
        List<Product> products = productRepository
                .findByCountBetweenOrderByCountAscNameAsc(minCount, maxCount);
        return products
                .stream()
                .map(product -> {
                    ProductInfoDtoResponse productDto = new ProductInfoDtoResponse(product);
                    List<String> categories = new ArrayList<>();
                    product.getCategories()
                            .stream()
                            .sorted(Comparator.comparing(Category::getName))
                            .forEach(category -> {
                                Optional<Category> parentCategory = Optional.ofNullable(category.getParentCategory());
                                if (!parentCategory.isPresent()) {
                                    categories.add(category.getName());
                                    if (category.getChildrenCategories().size() != 0)
                                        category.getChildrenCategories()
                                                .stream()
                                                .sorted(Comparator.comparing(Category::getName))
                                                .forEach(childrenCategory -> {
                                                    if (product.getCategories().contains(childrenCategory))
                                                        categories.add(childrenCategory.getName());
                                                });
                                }
                            });
                    productDto.setCategories(categories);
                    return productDto;
                })
                .collect(Collectors.toList());
    }
}