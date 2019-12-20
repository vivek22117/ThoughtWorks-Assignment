package com.ddsolutions.iot.equipment.data.utility;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    private static PropertyLoader propertyLoader = null;

    private static final String ENVIRONMENT = "environment";
    private static final String SUFFIX = ".properties";
    private static final String PREFFIX = "/application";

    private PropertyLoader() {
    }

    public static PropertyLoader getInstance() {
        if (propertyLoader == null) {
            synchronized (PropertyLoader.class) {
                if (propertyLoader == null) {
                    propertyLoader = new PropertyLoader();
                }
            }
        }
        return propertyLoader;
    }

    public static String getPropValue(String propertyKey) {
        String propFileName = null;

        try {
            String environment = System.getenv(ENVIRONMENT);
            if(environment != null){
                environment = "-" + environment;
                propFileName = PREFFIX + environment + SUFFIX;
            }
           
            Properties prop = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            return prop.getProperty(propertyKey);
        } catch (Exception e) {
            return null;
        }
    }
}
