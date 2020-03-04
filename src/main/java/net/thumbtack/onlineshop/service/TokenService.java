package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.entities.Token;
import net.thumbtack.onlineshop.repos.TokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenService(TokenRepository tokenRepository,PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Token> findUserByToken(String tokenName) {
        Iterable<Token> tokens = tokenRepository.findAll();
        return StreamSupport.stream(tokens.spliterator(), false)
                .filter(token -> passwordEncoder.matches(tokenName, token.getToken()))
                .findFirst();
    }

    @Transactional
    public String updateToken(Token token) {
        String tokenString = UUID.randomUUID().toString();
        token.setToken(passwordEncoder.encode(tokenString));
        tokenRepository.save(token);
        return tokenString;
    }
}
