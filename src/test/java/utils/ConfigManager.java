package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import io.restassured.RestAssured;
public class ConfigManager {

    private static final Properties properties = new Properties() ;
    protected static String BASE_URI;
    static  {

       try {
           FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
           properties.load(fis);
           BASE_URI = properties.getProperty("baseURI");
           RestAssured.baseURI = BASE_URI;
       } catch (IOException e) {
           throw new RuntimeException("Failed to load configuration file.", e);
       }

   }
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
