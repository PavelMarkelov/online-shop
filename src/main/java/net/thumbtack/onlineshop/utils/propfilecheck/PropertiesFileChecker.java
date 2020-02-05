package net.thumbtack.onlineshop.utils.propfilecheck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PropertiesFileChecker {

    private static final Set<String> PROP_DATA = new HashSet() {{
        add("rest_http_port");
        add("max_name_length");
        add("min_password_length");
    }};

    public static void check(String filename) throws CheckerException, IOException {
        String rootDir = System.getProperty("user.dir");
        String fileSep = System.getProperty("file.separator");
        String filePath = rootDir + fileSep + "src" + fileSep + "main" + fileSep + "resources" + fileSep + filename;
        File propFile = new File(filePath);
        if (!propFile.exists())
            throw new CheckerException(CheckerErrorCode.NOT_EXIST);
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        Map<String, Object> result = om.readValue(propFile, Map.class);
        boolean isContains = false;
        for (String prop : PROP_DATA) {
            for (Entry<String, Object> entryToCompare : result.entrySet()) {
                if (prop.equals(entryToCompare.getKey()))
                    isContains = true;
            }
            if (!isContains)
                throw new CheckerException(CheckerErrorCode.MISSING_PROP);
            isContains = false;
        }
    }
}
