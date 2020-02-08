package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.entities.Product;

import java.util.List;

public interface ProductService {

    Product addProduct(Product product);
    Product updateProduct(Long id);
    void deleteProduct(Long id);
    Product findProductById(Long id);
    List<Product> findAllProducts(Long[] categoriesId, String order);
    Product buyProduct(Product product);


}
