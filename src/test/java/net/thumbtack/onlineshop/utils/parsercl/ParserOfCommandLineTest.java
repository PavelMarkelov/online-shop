package net.thumbtack.onlineshop.utils.parsercl;

import org.junit.jupiter.api.Test;

import static net.thumbtack.onlineshop.utils.parsercl.ParserOfCommandLine.parseCommandLine;
import static org.junit.jupiter.api.Assertions.*;

class ParserOfCommandLineTest {


    @Test
    public void testParser() throws ParserException {
        String[] argsWithShortOpt = {"-p", "application.yml"};
        String fileName = parseCommandLine(argsWithShortOpt);
        assertEquals("application.yml", fileName);
        String[] argsWithLongOpt = {"--properties", "application.yml"};
        fileName = parseCommandLine(argsWithLongOpt);
        assertEquals("application.yml", fileName);
    }

    @Test
    public void testParserException() {
        String[] emptyArgs = new String[]{};
        ParserException e = assertThrows(ParserException.class, () -> parseCommandLine(emptyArgs));
        assertEquals("Invalid command line arguments", e.getParserErrorCode().getErrorString());
        String[] args = {"-properties", "application.json"};
        e = assertThrows(ParserException.class, () -> parseCommandLine(args));
        assertEquals("Available file extension .yml", e.getParserErrorCode().getErrorString());
        String[] args1 = {"-l"};
        try {
            parseCommandLine(args1);
            fail();
        } catch (Exception e1) { }
        args1 = new String[]{"-p"};
        try {
            parseCommandLine(args1);
            fail();
        } catch (Exception e1) { }
    }
}