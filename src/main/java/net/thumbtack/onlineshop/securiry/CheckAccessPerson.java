package net.thumbtack.onlineshop.securiry;

import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CheckAccessPerson {

    public static void checkAccessAdmin(Authentication auth) {
        if (auth == null)
            throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            throw new AccessDeniedException("Access for admin is denied!");
    }

    public static void checkAccessCustomer(Authentication auth) {
        if (auth == null)
            throw new UsernameNotFoundException(GlobalExceptionErrorCode.NOT_LOGIN.getErrorString());
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("USER")))
            throw new AccessDeniedException("Access for customer is denied!");
    }
}
