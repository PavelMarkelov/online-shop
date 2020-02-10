package net.thumbtack.onlineshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final static String USER_QUERY = "select login, password from person where login=?";
    private final static String ROLE_QUERY = "select role from person where login=?";

    private final PasswordEncoder PasswordEncoder;
    private final DataSource dataSource;

    public WebSecurityConfig(PasswordEncoder PasswordEncoder, DataSource dataSource) {
        this.PasswordEncoder = PasswordEncoder;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(USER_QUERY)
                .authoritiesByUsernameQuery(ROLE_QUERY)
                .dataSource(dataSource)
                .passwordEncoder(PasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST,"/api/admins", "/api/clients", "/api/session").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .usernameParameter("login")
                    .passwordParameter("password")
                    .loginProcessingUrl("/api/session")
                    .permitAll()
                .and()
                    .logout()
                    .logoutUrl("/api/session")
                    .invalidateHttpSession(true)
                    .deleteCookies()
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers( "/static/**", "/css/**", "/js/**", "/images/**");
    }
}
