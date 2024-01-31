package com.bizconnect;

import com.dsmdb.japi.MagicDBAPI;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TempTest {
    public static void main(String[] args) {
        System.out.println();


        SecureRandom ran = null;
        LocalDateTime ldt = LocalDateTime.now();
        System.out.println("makeTradeNum 1");
        try {
            ran = SecureRandom.getInstanceStrong();
            int randomNum = ran.nextInt(9999);
            String formattedRandomNum = String.format("%04d", randomNum);
            System.out.println("makeTradeNum 2");
            System.out.println(formattedRandomNum);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
