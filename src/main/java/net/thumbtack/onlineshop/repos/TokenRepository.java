package net.thumbtack.onlineshop.repos;

import net.thumbtack.onlineshop.entities.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Long> {
}
