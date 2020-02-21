package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.entities.Token;
import net.thumbtack.onlineshop.repos.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Optional<Token> findUserByToken(String tokenName) {
        Token token = tokenRepository.findByToken(tokenName);
        return Optional.ofNullable(token);
    }

    @Transactional
    public String updateToken(Token token) {
        token.setToken(UUID.randomUUID().toString());
        return tokenRepository.save(token).getToken();
    }
}
