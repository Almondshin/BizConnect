package com.bizconnect.application.domain.service;

import com.bizconnect.application.domain.model.AgencyInfoKey;
import com.bizconnect.application.port.in.NotiUseCase;
import com.bizconnect.application.port.out.load.LoadEncryptDataPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class NotiService implements NotiUseCase {

    private final LoadEncryptDataPort loadEncryptDataPort;

    public NotiService(LoadEncryptDataPort loadEncryptDataPort) {
        this.loadEncryptDataPort = loadEncryptDataPort;
    }

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

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getAgencyUrlByAgencyInfoKey(String agencyId, String type) {
        Optional<AgencyInfoKey> agencyInfoKey = loadEncryptDataPort.getAgencyInfoKey(agencyId);
        if (agencyInfoKey.isPresent()) {
            AgencyInfoKey info = agencyInfoKey.get();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> agencyUrlJson = null;
            try {
                agencyUrlJson = mapper.readValue(info.getAgencyUrl(), new TypeReference<>() {
                });
                return agencyUrlJson.get(type);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}
