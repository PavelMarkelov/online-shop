package net.thumbtack.onlineshop.repos;

import net.thumbtack.onlineshop.entities.Category;
import net.thumbtack.onlineshop.entities.PurchaseHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {

//    @Query(value = "select * from PurchaseHistory ph where ph.categories")
    List<PurchaseHistory> findByPurchaseDateAndCategoriesIdIsIn(
            Date purchaseDate, List<Long> categories_id, Pageable pageable);

    List<PurchaseHistory> findByPurchaseDateAndProductIdIn(
            Date purchaseDate, List<Long> product_id, Pageable pageable);

    List<PurchaseHistory> findByPurchaseDateAndPersonIdIn(Date purchaseDate,
            List<Long> person_id, Pageable pageable);

    List<PurchaseHistory> findByPurchaseDate(Date purchaseDate, Pageable pageable);
}
