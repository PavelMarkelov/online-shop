package net.thumbtack.onlineshop.securiry;

import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.entities.Role;
import net.thumbtack.onlineshop.entities.Token;
import net.thumbtack.onlineshop.service.PersonService;
import net.thumbtack.onlineshop.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class CustomFilter extends OncePerRequestFilter {

    private final String cookieName;
    private final TokenService tokenService;

    private static Logger logger = LoggerFactory.getLogger(CustomFilter.class);


    public CustomFilter(@Value("${cookie_name}") String cookieName,
                        TokenService tokenService) {
        this.cookieName = cookieName;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        Cookie cookie = WebUtils.getCookie(httpServletRequest, cookieName);
        if (cookie != null && cookie.getName().equals(cookieName) && !StringUtils.isEmpty(cookie.getValue())) {
            String token = cookie.getValue();
            Optional<Token> tokenOpt = tokenService.findUserByToken(token);
            if (tokenOpt.isPresent()) {
                Person person = tokenOpt.get().getPerson();
                String login = person.getUsername();
                String role = tokenOpt.get().getPerson().getRoles().stream().findFirst().get().getAuthority();
                try {
                    SimpleGrantedAuthority sga = new SimpleGrantedAuthority(role);
                    ArrayList<SimpleGrantedAuthority> list = new ArrayList<>();
                    list.add(sga);
                    UsernamePasswordAuthenticationToken auth
                            = new UsernamePasswordAuthenticationToken(login, null, list);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (Exception e) {
                    logger.debug("Set Authentication from cookie failed {}", tokenOpt.get().getPerson());
                }
                String newToken = tokenService.updateToken(tokenOpt.get());
                cookie.setValue(newToken);
                httpServletResponse.addCookie(cookie);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}


