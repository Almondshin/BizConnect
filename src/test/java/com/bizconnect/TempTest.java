package com.bizconnect;

import com.dsmdb.japi.MagicDBAPI;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class TempTest {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        System.out.println("sdf.format(today) : " + sdf.format(today));

        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse("2024-02-23"));
        Date sample = cal.getTime();

        System.out.println(sdf.format(today).equals(sdf.format(sample)));


    }
}
