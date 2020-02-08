package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.BasketDto;
import net.thumbtack.onlineshop.entities.Basket;
import net.thumbtack.onlineshop.entities.Product;

public interface BasketService {

    Basket addToBasket(Product product);
    void deleteProductFromBasket(Long productId);
    Basket changingQuantity(Product product);
    Basket findBasketByPersonLogin(String login);
    BasketDto buyingFromBasket(Basket basket);
}
