package net.thumbtack.onlineshop.utils.parsercl;

import org.apache.commons.cli.*;
import org.springframework.stereotype.Component;

public class ParserOfCommandLine {

    public static String  parseCommandLine(String... args) throws ParserException {
        Options options = new Options();
        options.addRequiredOption("p", "properties",true, "Application properties file");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("file name", options);
            throw new ParserException(ParserErrorCode.WRONG_ARGS);
        }
        String propertiesFileName = cmd.getOptionValue("p");
        if (propertiesFileName != null) {
            if (!propertiesFileName.matches(".+(\\.yml)$"))
                throw new ParserException(ParserErrorCode.WRONG_FILE_EXTENSION);
        }
        return propertiesFileName;
    }


}
