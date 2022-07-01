package com.NoAutenticados.RoyalOak.utils;

import com.NoAutenticados.RoyalOak.models.Cliente;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    private static List<String> createdToken = new ArrayList<>();

    public static String getToken(int leftLimit, int rightLimit,int targetStringLength, Random random){
        String tokenString= "";
        do {
            tokenString = getRandomString(leftLimit,rightLimit, targetStringLength, random);
        } while (createdToken.contains(tokenString));
        createdToken.add(tokenString);
        return tokenString;
    }

    public static String getRandomString(int leftLimit, int rightLimit,int targetStringLength, Random random ) {
        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static void borrarToken(String token){
        createdToken.remove(token);
    }
}
