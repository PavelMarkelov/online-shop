package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.entities.Role;
import net.thumbtack.onlineshop.utils.parsercl.ParserException;
import net.thumbtack.onlineshop.utils.parsercl.ParserOfCommandLine;
import net.thumbtack.onlineshop.utils.propfilecheck.CheckerException;
import net.thumbtack.onlineshop.utils.propfilecheck.PropertiesFileChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class OnlineShopServer {

    private static Logger logger = LoggerFactory.getLogger(OnlineShopServer.class);

    public static void main(String... args) {
        String propertiesFileName = null;
        try {
            propertiesFileName = ParserOfCommandLine.parseCommandLine(args);
        } catch (ParserException e) {
            logger.info(e.getParserErrorCode().getErrorString());
            System.exit(1);
        }
        try {
            PropertiesFileChecker.check(propertiesFileName);
        } catch (IOException ex) {
            logger.info("Can't start server with prop file {} {}", propertiesFileName, ex);
            System.exit(1);
        } catch (CheckerException ex) {
            logger.info(ex.getCheckerErrorCode().getErrorString());
            System.exit(1);
        }
        SpringApplication.run(OnlineShopServer.class, args);
    }

}
