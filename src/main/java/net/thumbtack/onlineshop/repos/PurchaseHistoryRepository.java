package net.thumbtack.onlineshop.repos;

import net.thumbtack.onlineshop.entities.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {
}
