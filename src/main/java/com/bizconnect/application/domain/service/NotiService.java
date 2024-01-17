package com.bizconnect.application.domain.service;

import com.bizconnect.application.port.in.NotiUseCase;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class NotiService implements NotiUseCase {
    @Override
    public String sendNotification(String targetUrl, String responseData) {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(responseData.getBytes(StandardCharsets.UTF_8));


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            System.out.println("전달 URL 체크 : " + url);
            System.out.println("전달 data 체크 : " + responseData);

            System.out.println("제휴사에 NOTI 발송");

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
