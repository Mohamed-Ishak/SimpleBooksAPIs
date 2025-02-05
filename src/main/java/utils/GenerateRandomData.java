package utils;

import java.util.UUID;

public class GenerateRandomData {

    public static String generateUniqueEmail(){
        return "user_" + UUID.randomUUID().toString().substring(0,8) + "@example.com";
    }

}
