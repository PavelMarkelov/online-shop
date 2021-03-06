package net.thumbtack.onlineshop.repos;

import net.thumbtack.onlineshop.entities.PurchaseHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {

    Page<PurchaseHistory> findByPurchaseDateGreaterThanAndCategoriesIdIsIn(
            Date purchaseDate, List<Long> categoriesId, Pageable pageable);

    Page<PurchaseHistory> findByPurchaseDateGreaterThanAndProductIdIn(
            Date purchaseDate, List<Long> productId, Pageable pageable);

    Page<PurchaseHistory> findByPurchaseDateGreaterThanAndPersonIdIn(Date purchaseDate,
            List<Long> personId, Pageable pageable);

    Page<PurchaseHistory> findByPurchaseDateGreaterThan(Date purchaseDate, Pageable pageable);
}
