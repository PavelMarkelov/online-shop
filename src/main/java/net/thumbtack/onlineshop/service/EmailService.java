package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.entities.Product;
import net.thumbtack.onlineshop.utils.ReportInExcelGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@PropertySource("classpath:/email-requisites.yml")
public class EmailService {

    private final String from;
    private final String subject;
    private final String location;

    private static Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(@Value("${spring.mail.username}") String from,
                        @Value("${report}") String subject,
                        @Value("${location}") String location,
                        JavaMailSender mailSender,
                        @Qualifier("htmlTemplateEngine") TemplateEngine templateEngine
    ) {
        this.from = from;
        this.subject = subject;
        this.location = location;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendMessage(Person person, String emailAddress, List<Product> products
    ) throws MessagingException, IOException {
        logger.info("Sending message to email {}", emailAddress);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariable("fistName", person.getFirstName());
        context.setVariable("lastName", person.getLastName());
        context.setVariable("from", from);
        context.setVariable("location", location);
        String html = templateEngine.process("email-message", context);
        File excelFile = ReportInExcelGenerator.productsToExcel(products);

        helper.addAttachment(excelFile.getName(), excelFile);
        helper.setTo(emailAddress);
        helper.setText(html, true);
        helper.setFrom(from);
        helper.setSubject(subject);

        mailSender.send(message);
        templateEngine.clearTemplateCache();
    }
}
