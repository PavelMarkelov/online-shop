package net.thumbtack.onlineshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    private final String host;
    private final String username;
    private final String password;
    private final int port;
    private final String protocol;
    private final String debug;

    public MailConfig(@Value("${spring.mail.host}") String host,
                      @Value("${spring.mail.username}") String username,
                      @Value("${spring.mail.password}") String password,
                      @Value("${spring.mail.port}") int port,
                      @Value("${spring.mail.protocol}") String protocol,
                      @Value("${mail.debug}") String debug
    ) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.protocol = protocol;
        this.debug = debug;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.debug", debug);
        return javaMailSender;
    }
}
