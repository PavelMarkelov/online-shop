package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.Response.ProductInfoDtoResponse;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.utils.ReportInExcelGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@PropertySource("classpath:/email-requisites.yml")
public class EmailService {

    private final static String EXCEL = "Report.xlsx";

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

    public void sendMessage(Person person, String emailAddress,
                            List<ProductInfoDtoResponse> products
    ) {
        logger.info("Sending message to email {}", emailAddress);

        Context context = new Context();
        context.setVariable("fistName", person.getFirstName());
        context.setVariable("lastName", person.getLastName());
        context.setVariable("from", from);
        context.setVariable("location", location);

        try {
            String html = templateEngine.process("email-message", context);
            InputStreamSource input = ReportInExcelGenerator.productsToExcel(products);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.addAttachment(EXCEL, input);
            helper.setTo(emailAddress);
            helper.setText(html, true);
            helper.setFrom(from);
            helper.setSubject(subject);

            mailSender.send(message);
            templateEngine.clearTemplateCache();
        } catch (Exception ex) {
            logger.warn("Can't send message to email {} {}", emailAddress, ex.getMessage());
        }
    }
}
