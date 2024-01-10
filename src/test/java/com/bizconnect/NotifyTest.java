package com.bizconnect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class NotifyTest {

    @Test
    public void notifyPaymentSiteInfoTest() throws GeneralSecurityException {
        Map<String, String> notifyPaymentSiteInfoMap = new HashMap<>();
        notifyPaymentSiteInfoMap.put("agencyId", "agencyId");
        notifyPaymentSiteInfoMap.put("siteId", "siteId");
        notifyPaymentSiteInfoMap.put("startDate", "2023-12-01");
        notifyPaymentSiteInfoMap.put("endDate", "2023-12-31");
        notifyPaymentSiteInfoMap.put("rateSel", "lite_1m_200");
        notifyPaymentSiteInfoMap.put("salesPrice", "10000");
        String plainData = mapToJSONString(notifyPaymentSiteInfoMap);

        String agencyId = "SQUARES";
        String msgType = "NotifyPaymentSiteInfo";
        String encryptData = encryptData(plainData);
        String verifyInfo = hmacSHA256(plainData, agencyId);

        Map<String, String> requestNotifyPaymentSiteInfoMap = new HashMap<>();
        requestNotifyPaymentSiteInfoMap.put("agencyId", agencyId);
        requestNotifyPaymentSiteInfoMap.put("msgType", msgType);
        requestNotifyPaymentSiteInfoMap.put("encryptData", encryptData);
        requestNotifyPaymentSiteInfoMap.put("verifyInfo", verifyInfo);
        String requestNotifyPaymentSiteInfoData = mapToJSONString(requestNotifyPaymentSiteInfoMap);
        send("http://127.0.0.1:8080/notifyPaymentSiteInfo.jsp", requestNotifyPaymentSiteInfoData);
    }

    @Test
    public void notifyStatusSiteTest() throws GeneralSecurityException {
        Map<String, String> notifyStatusSiteMap = new HashMap<>();
        notifyStatusSiteMap.put("agencyId", "agencyId");
        notifyStatusSiteMap.put("siteId", "siteId");
        notifyStatusSiteMap.put("siteStatus", "Y");
        String plainData = mapToJSONString(notifyStatusSiteMap);

        String agencyId = "SQUARES";
        String msgType = "NotifyStatusSite";
        String encryptData = encryptData(plainData);
        String verifyInfo = hmacSHA256(plainData, agencyId);

        Map<String, String> requestStatusSiteMap = new HashMap<>();
        requestStatusSiteMap.put("agencyId", agencyId);
        requestStatusSiteMap.put("msgType", msgType);
        requestStatusSiteMap.put("encryptData", encryptData);
        requestStatusSiteMap.put("verifyInfo", verifyInfo);
        String requestStatusSiteData = mapToJSONString(requestStatusSiteMap);
        send("http://127.0.0.1:8080/notifyStatusSite.jsp", requestStatusSiteData);
    }

    public static String mapToJSONString(Map<String, String> map) {
        try {
            /* libs  필요 */
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            System.out.println("mapToJSONString 실패");
            return null;
        }
    }

    private static String encryptData(String targetEncode) throws GeneralSecurityException {
        String AES_CBC_256_KEY = "tmT6HUMU+3FW/RR5fxU05PbaZCrJkZ1wP/k6pfZnSj8=";
        String AES_CBC_256_IV = "/SwvI/9aT7RiMmfm8CfP4g==";

        byte[] key = Base64.getDecoder().decode(AES_CBC_256_KEY);
        byte[] iv = Base64.getDecoder().decode(AES_CBC_256_IV);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] cipherBytes = cipher.doFinal(targetEncode.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(cipherBytes);
    }

    public static String hmacSHA256(String target, String hmacKeyString) {
        try {
            byte[] hmacKey = hmacKeyString.getBytes(StandardCharsets.UTF_8);

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(hmacKey, "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hashed = sha256_HMAC.doFinal(target.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String send(String targetUrl, String data) {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(data.getBytes(StandardCharsets.UTF_8));


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}