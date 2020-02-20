package net.thumbtack.onlineshop.repos;

import net.thumbtack.onlineshop.entities.ProductInBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProductInBasketRepository  extends CrudRepository<ProductInBasket, Long> {
}
