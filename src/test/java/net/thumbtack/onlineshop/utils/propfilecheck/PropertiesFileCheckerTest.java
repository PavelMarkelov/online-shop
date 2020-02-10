package net.thumbtack.onlineshop.utils.propfilecheck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.thumbtack.onlineshop.utils.propfilecheck.PropertiesFileChecker.check;
import static net.thumbtack.onlineshop.utils.propfilecheck.PropertiesFileChecker.getAppProperties;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertiesFileCheckerTest {

    private static final String FILE_NAME = "appForTest.yml";

    private static final File TEST_FILE = new File(getPathString(FILE_NAME));

    private static String getPathString(String fileName) {
        String rootDir = System.getProperty("user.dir");
        String fileSep = System.getProperty("file.separator");
        return rootDir + fileSep + "src" + fileSep + "main" + fileSep + "resources" + fileSep + fileName;
    }

    private Map<String, Object> testData = new HashMap<>();

    @BeforeEach
    void createTestFile() throws IOException {
        try {
            TEST_FILE.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        testData.put("rest_http_port", 8888);
        testData.put("max_name_length", 50);
        testData.put("min_password_length", 8);
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        om.writeValue(TEST_FILE, testData);
    }

    @AfterEach
    void deleteTestFile() {
        TEST_FILE.delete();
        testData.clear();
    }

    @Test
    void checkTest() throws CheckerException, IOException {
        check(FILE_NAME);
        Map<String, Integer> prop = getAppProperties();
        assertEquals(testData, prop);
    }

    @Test
    void checkFailEmptyFileTest() {
        deleteTestFile();
        CheckerException e = assertThrows(CheckerException.class, () -> check(FILE_NAME));
        assertEquals("The specified file does not exist", e.getCheckerErrorCode().getErrorString());
    }

    @Test
    void checkFailEmptyOptTest() throws IOException {
        deleteTestFile();
        testData.put("rest_http_port", 8888);
        testData.put("max_name_length", 50);
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        om.writeValue(TEST_FILE, testData);
        CheckerException e = assertThrows(CheckerException.class, () -> check(FILE_NAME));
        assertEquals("Required property not found", e.getCheckerErrorCode().getErrorString());
    }

    @Test
    void checkFailWrongPropKeyTest() throws IOException {
        deleteTestFile();
        testData.put("rest_http_port", 8888);
        testData.put("max_name_length", 50);
        testData.put("min_NoCorrect_length", 8);
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        om.writeValue(TEST_FILE, testData);
        CheckerException e = assertThrows(CheckerException.class, () -> check(FILE_NAME));
        assertEquals("Required property not found", e.getCheckerErrorCode().getErrorString());
    }
}