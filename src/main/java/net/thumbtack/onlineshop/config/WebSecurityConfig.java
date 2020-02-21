package net.thumbtack.onlineshop.config;

import net.thumbtack.onlineshop.entities.Role;
import net.thumbtack.onlineshop.securiry.CustomAuthenticationEntryPoint;
import net.thumbtack.onlineshop.securiry.CustomFilter;
import net.thumbtack.onlineshop.service.PersonService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final static String USER_ROLE = Role.USER.getAuthority();
    private final static String ADMIN_ROLE = Role.ADMIN.getAuthority();

    private final PersonService personService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomFilter customFilter;

    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(PersonService personService, CustomAuthenticationEntryPoint authenticationEntryPoint, CustomFilter customFilter, PasswordEncoder passwordEncoder) {
        this.personService = personService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customFilter = customFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(personService)
                .passwordEncoder(passwordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .cors().and().csrf().disable()
                .formLogin().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.DELETE, "/api/session").authenticated()
                .antMatchers(HttpMethod.GET, "/api/accounts").authenticated()
                .antMatchers(HttpMethod.GET, "/api/clients").hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.PUT, "/api/admins").hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.PUT, "/api/clients").hasRole(USER_ROLE)
                .antMatchers("/api/categories/*").hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.POST, "/api/products/").hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.PUT, "/api/products/*").hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.DELETE, "/api/products/*").hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.GET, "/api/products/*").authenticated()
                .antMatchers("/api/deposits").hasRole(USER_ROLE)
                .antMatchers(HttpMethod.POST, "/api/purchases").hasRole(USER_ROLE)
                .antMatchers("/api/baskets").hasRole(USER_ROLE)
                .antMatchers(HttpMethod.GET, "/api/purchases/*").hasRole(ADMIN_ROLE)
                .anyRequest().permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
